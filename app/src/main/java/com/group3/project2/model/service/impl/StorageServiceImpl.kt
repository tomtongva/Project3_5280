package com.group3.project2.model.service.impl

import com.group3.project2.model.service.StorageService
import com.google.firebase.firestore.DocumentChange.Type.REMOVED
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.group3.project2.model.Game
import javax.inject.Inject

class StorageServiceImpl @Inject constructor() : StorageService {
    private var listenerRegistration: ListenerRegistration? = null

    override fun addListener(
        onDocumentEvent: (Boolean, Game) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val query = Firebase.firestore.collection(GAME_COLLECTION)

        listenerRegistration = query.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            value?.documentChanges?.forEach {
                val wasDocumentDeleted = it.type == REMOVED
                val game = it.document.toObject<Game>().copy(hostId = it.document.id)
                onDocumentEvent(wasDocumentDeleted, game)
            }
        }
    }

    override fun removeListener() {
        listenerRegistration?.remove()
    }

    override fun getGame(
        gameId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (Game) -> Unit
    ) {
        Firebase.firestore
            .collection(GAME_COLLECTION)
            .document(gameId)
            .get()
            .addOnFailureListener { error -> onError(error) }
            .addOnSuccessListener { result ->
                val game = result.toObject<Game>()?.copy(hostId = result.id)
                onSuccess(game ?: Game())
            }
    }

    override fun saveGame(game: Game, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(GAME_COLLECTION)
            .document(game.hostId)
            .set(game)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun updateGame(game: Game, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(GAME_COLLECTION)
            .document(game.hostId)
            .set(game)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteGame(gameId: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(GAME_COLLECTION)
            .document(gameId)
            .delete()
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteAllForUser(userId: String, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(GAME_COLLECTION)
            .whereEqualTo(HOST_ID, userId)
            .get()
            .addOnFailureListener { error -> onResult(error) }
            .addOnSuccessListener { result ->
                for (document in result) document.reference.delete()
                onResult(null)
            }
    }

    override fun updateUserId(
        oldUserId: String,
        newUserId: String,
        onResult: (Throwable?) -> Unit
    ) {
        Firebase.firestore
            .collection(GAME_COLLECTION)
            .whereEqualTo(HOST_ID, oldUserId)
            .get()
            .addOnFailureListener { error -> onResult(error) }
            .addOnSuccessListener { result ->
                for (document in result) document.reference.update(HOST_ID, newUserId)
                onResult(null)
            }
    }

    companion object {
        private const val GAME_COLLECTION = "Games"
        private const val HOST_ID = "hostId"
    }
}
