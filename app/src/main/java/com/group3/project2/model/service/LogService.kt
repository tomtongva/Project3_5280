package com.group3.project2.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
