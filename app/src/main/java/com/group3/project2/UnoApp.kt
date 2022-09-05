package com.group3.project2

import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.group3.project2.common.snackbar.SnackbarManager
import com.group3.project2.screens.game.GameScreen
import com.group3.project2.screens.new_game.NewGameScreen
import com.group3.project2.screens.login.LoginScreen
import com.group3.project2.screens.settings.SettingsScreen
import com.group3.project2.screens.sign_up.SignUpScreen
import com.group3.project2.screens.splash.SplashScreen
import com.group3.project2.screens.lobby.LobbyScreen
import com.group3.project2.theme.UnoTheme
import kotlinx.coroutines.CoroutineScope

@Composable
@ExperimentalMaterialApi
fun UnoApp() {
    UnoTheme {
        Surface(color = MaterialTheme.colors.background) {
            val appState = rememberAppState()

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colors.onPrimary)
                        }
                    )
                },
                scaffoldState = appState.scaffoldState
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) { unoGraph(appState) }
            }
        }
    }
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
    UnoAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@ExperimentalMaterialApi
fun NavGraphBuilder.unoGraph(appState: UnoAppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) }
        )
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                    openScreen = { route -> appState.navigate(route) }
        )
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(LOBBY_SCREEN) {
        LobbyScreen(openScreen = { route -> appState.navigate(route) })
    }

    composable(GAME_SCREEN) {
        GameScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(
        route = "$NEW_GAME_SCREEN$TASK_ID_ARG",
        arguments = listOf(navArgument(TASK_ID) { defaultValue = TASK_DEFAULT_ID })
    ) {
        NewGameScreen(
            popUpScreen = { appState.popUp() },
            taskId = it.arguments?.getString(TASK_ID) ?: TASK_DEFAULT_ID
        )
    }
}