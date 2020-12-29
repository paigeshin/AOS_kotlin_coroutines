package com.anushka.coroutinesdemo1

import kotlinx.coroutines.*

class UserDataManagerUnstructured {

    suspend fun getTotalUserCount(): Int {
        var count = 0
        CoroutineScope(Dispatchers.IO).launch {
            //simulate long running task
            delay(1000)
            count = 50
        }

        /*** Use async build to prevent unstructured concurrency ***/
        val deferred = CoroutineScope(Dispatchers.IO).async {
            delay(3000)
            return@async 80
        }

        return count + deferred.await()
    }

}