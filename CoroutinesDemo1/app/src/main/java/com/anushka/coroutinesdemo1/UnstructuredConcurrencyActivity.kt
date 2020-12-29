package com.anushka.coroutinesdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class UnstructuredConcurrencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unstructured_concurrency)

        btnDownloadUserData.setOnClickListener {

            /*** Calling Unstructured Concurrency Function ***/
            CoroutineScope(Dispatchers.Main).launch {
                tvUserMessage.text = getTotalUserCount().toString() //it returns 0
            }

        }

    }

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