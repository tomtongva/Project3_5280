package com.group3.project2.model.service

interface AccountService {
    fun hasUser(): Boolean
    fun getUserId(): String
    fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun createAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun sendRecoveryEmail(email: String, onResult: (Throwable?) -> Unit)
    fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
    fun deleteAccount(onResult: (Throwable?) -> Unit)
    fun signOut()
}
