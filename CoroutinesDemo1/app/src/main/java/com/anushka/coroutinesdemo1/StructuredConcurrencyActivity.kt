package com.anushka.coroutinesdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class StructuredConcurrencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_structured_concurrency)

        btnDownloadUserData.setOnClickListener {

            /*** Calling structured Concurrency Function ***/
            CoroutineScope(Dispatchers.Main).launch {
                tvUserMessage.text = getTotalUserCount().toString() //it returns 0
            }

        }

    }

    suspend fun getTotalUserCount(): Int {

        var count = 0
        var deferred: Deferred<Int>?

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

        return count + deferred!!.await()
    }

}