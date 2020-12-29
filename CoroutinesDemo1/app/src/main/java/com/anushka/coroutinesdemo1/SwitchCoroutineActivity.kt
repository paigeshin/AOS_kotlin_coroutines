package com.anushka.coroutinesdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class SwitchCoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_coroutine)

        CoroutineScope(Dispatchers.IO).launch {
            downloadUserData()
        }
    }

    //background task to main thread(UI)
    private suspend fun downloadUserData() {
        //It freezes main thread
        for (i in 1..200000) {
            //get Thread name
            Log.i("MyTag", "Downloading user $i in ${Thread.currentThread().name}")


            // If background tasks try to access to Android UI, app will crash
            //❗️It will crash.
            //tvUserMessage.text = "Downloading user $i in ${Thread.currentThread().name}"

            // 🧡 Switch from background thread to main thread to prevent app crash
            withContext(Dispatchers.Main) {
                tvUserMessage.text = "Downloading user $i in ${Thread.currentThread().name}"
                delay(1000)
            }

        }
    }

}