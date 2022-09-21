package com.group3.project2.model.service.impl

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.group3.project2.model.Game
import com.group3.project2.model.service.FunctionService
import java.util.Objects
import javax.inject.Inject

class FunctionServiceImpl @Inject constructor() : FunctionService{

    private var functions: FirebaseFunctions

    init {
        functions = Firebase.functions
    }

    override fun createNewGame(gameTitle: String) : Task<HashMap<*, *>> {
        val data = hashMapOf(
            "gameTitle" to gameTitle
        )

        return functions
            .getHttpsCallable("createNewGame")
            .call(data)
            .continueWith { task ->
                val result = task.result?.data as HashMap<*,*>
                result
            }
    }

    override fun joinGame(hostId: String, guestId: String) : Task<HashMap<*, *>>{
        val data = hashMapOf(
            "hostId" to hostId,
            "guestId" to guestId
        )

        return functions
            .getHttpsCallable("joinGame")
            .call(data)
            .continueWith { task ->
                val result = task.result?.data as HashMap<*,*>
                result
            }
    }

    override fun drawCard(text: String) : Task<String>{
        val data = hashMapOf(
            "text" to text,
            "push" to true
        )

        return functions
            .getHttpsCallable("drawCard")
            .call(data)
            .continueWith { task ->
                val result = task.result?.data as String
                result
            }
    }

    override fun playCard(text: String) : Task<String>{
        val data = hashMapOf(
            "text" to text,
            "push" to true
        )

        return functions
            .getHttpsCallable("playCard")
            .call(data)
            .continueWith { task ->
                val result = task.result?.data as String
                result
            }
    }

    override fun leaveGame(gameId: String) : Task<HashMap<*, *>>{
        val data = hashMapOf(
            "gameId" to gameId
        )
        return functions
            .getHttpsCallable("leaveGame")
            .call(data)
            .continueWith { task ->
                val result = task.result?.data as HashMap<*, *>
                result
            }
    }
}
