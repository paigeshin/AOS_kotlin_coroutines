package com.anushka.coroutinesdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*

class ParallelDecompositionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parallel_decomposition)


        // Sequential Operation
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("MyTag", "Calculation started...")
            val stock1 = getStock1()
            val stock2 = getStock2()
            val total = stock1 + stock2
            Log.i("MyTag", "Total is $total")
        }
        // it takes 18 seconds


        // Parallel Decomposition, background task
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("MyTag", "Calculation started...")
            val stock1 = async { getStock1()}
            val stock2 = async {getStock2() }
            val total = stock1.await() + stock2.await()
            Log.i("MyTag", "Total is $total")
        }
        // it takes 10 seconds

        // Parallel Decomposition, Main thread with Background suspend functions
        // Access to Main Thread
        CoroutineScope(Dispatchers.Main).launch {
            Log.i("MyTag", "Calculation started...")
            val stock1 = async(Dispatchers.IO) {
                getStock1()
            }
            val stock2 = async(Dispatchers.IO) {
                getStock2()
            }
            val total = stock1.await() + stock2.await()
            Log.i("MyTag", "Total is $total")
            Toast.makeText(this@ParallelDecompositionActivity, total.toString(), Toast.LENGTH_LONG).show()
        }
        // it takes 10 seconds

    }

    private suspend fun getStock1(): Int {
        delay(10000)
        Log.d("MyTag", "stock 1 returned")
        return 55000
    }

    private suspend fun getStock2(): Int {
        delay(8000)
        Log.d("MyTag", "stock 2 returned")
        return 35000
    }

}