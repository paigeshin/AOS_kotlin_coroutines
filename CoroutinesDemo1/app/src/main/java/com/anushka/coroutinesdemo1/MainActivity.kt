package com.anushka.coroutinesdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCount.setOnClickListener {
            tvCount.text = count++.toString()
        }

        /*** Your first coroutine ***/
        CoroutineScope(Dispatchers.IO).launch {
            downloadUserData()
        }

        // background thread
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..200000) {
                //get Thread name
                Log.i("Background", "Downloading user $i in ${Thread.currentThread().name}")
            }
        }


        // main thread
        CoroutineScope(Dispatchers.Main).launch {
            for (i in 1..200000) {
                //get Thread name
                Log.i("Main", "Downloading user $i in ${Thread.currentThread().name}")
            }
        }

        btnDownloadUserData.setOnClickListener {

            /*** Your first coroutine ***/
            CoroutineScope(Dispatchers.IO).launch {
                downloadUserData()
            }

        }

    }




    private fun downloadUserData() {
        //It freezes main thread
        for (i in 1..200000) {
            //get Thread name
            Log.i("MyTag", "Downloading user $i in ${Thread.currentThread().name}")
        }
    }

}

