package com.anushka.coroutinesdemo1

import kotlinx.coroutines.*

class UserDataMangerStructured {

    var count = 0
    lateinit var deferred: Deferred<Int>

    suspend fun getTotalUserCount(): Int {

        //Structured, guarentees the completion of a task
        coroutineScope {
            launch {
                delay(1000)
                count = 50
            }
            deferred = async (Dispatchers.IO) {
                delay(3000)
                return@async 70
            }
        }

        return count + deferred.await()
    }

}