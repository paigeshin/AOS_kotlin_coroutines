Related document - [https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/)

# What is coroutines?

- Coroutines are software components that create sub routines for cooperative multitasking.
- Coroutines were first used in 1958 for the assembly language.
- A coroutine can be introduced as **a sequence of well managed sub tasks.**
- To some extent a coroutine can be considered as **a light weight thread.**
- You can execute **many coroutines in a single thread.**
- **A coroutine can switch between threads.**
- A coroutine can suspend from one thread and resume from another thread.
- Coroutine replaces `RxJava` , `AsyncTask` , `Executors` , `HandlerThreads` , `IntentServices`
- **Coroutines API allows us to write asynchronous codes in a sequential manner.**
- It avoids unnecessary boilerplate codes comes with callbacks(callback hell).
- Coroutines make our code more readable and maintainable.

### On official doc

A *coroutine* is a concurrency design pattern that you can use on Android to simplify code that executes asynchronously.[Coroutines](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html) were added to Kotlin in version 1.3 and are based on established concepts from other languages.

On Android, coroutines help to manage long-running tasks that might otherwise block the main thread and cause your app to become unresponsive. Over 50% of professional developers who use coroutines have reported seeing increased productivity. This topic describes how you can use Kotlin coroutines to address these problems, enabling you to write cleaner and more concise app code.

# Importance of Asynchronous Programming

### Android Task

- Parse XML
- Inflate views
- Draw the screen
- Listen to users

### Errors

- Performance Errors
- Unpredictable Behaviors
- Freeze the screen
- ANR (Application Not Responding Error)

⇒ We should always implement long running tasks asynchronously in a separate thread

⇒ Use `Coroutines`

### Get dependency

[https://developer.android.com/kotlin/coroutines](https://developer.android.com/kotlin/coroutines)

```kotlin
dependencies {
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9'
}
```

### First coroutines

```kotlin
CoroutineScope(Dispatchers.IO).launch {
    downloadUserData()
}
```

```kotlin
class MainActivity : AppCompatActivity() {
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCount.setOnClickListener {
            tvCount.text = count++.toString()
        }
        btnDownloadUserData.setOnClickListener {

            /*** Your first coroutine ***/
            CoroutineScope(Dispatchers.IO).launch {
                downloadUserData()
            }

        }

    }

    //this function freezes other main thread operation
    private fun downloadUserData() {
        //It freezes main thread
        for (i in 1..200000) {
            //get Thread name
            Log.i("MyTag", "Downloading user $i in ${Thread.currentThread().name}")
        }
    }
}
```

# Scopes, Dispatchers & Builders

### Scopes

- GlobalScope
- Defines the scope of the coroutine
- Scope is an interface
- You define scope with `Dispatchers.` series.
- `Dispatchers.` is a context.
- `Dispatchers.IO + job`

### Dispatchers.Main 🧡🧡🧡

- Coroutine will run in the Main(UI) thread.

### [Dispatchers.IO](http://dispatchers.IO) 🧡🧡🧡

- Coroutine will run in a background thread from a shared pool of on-demand created threads.

### Dispatchers.Default

- For CPU intensive tasks such as sorting a large list.

### Dispatchers.Unconfined

- Coroutine will run in the current thread, but if it is suspended and resumed it will run on suspending function's thread.

### Builders

- Extension function for Coroutine.
- `launch()`

### Launch

- **Launch builder launches a new coroutine without blocking the current thread.**
- Returns an instance of Job, which can be used as a reference to the coroutine.
- We use this builder for coroutines that does not have any result as the return value.

### Async

- **Async builder launches a new coroutine without blocking the current thread.**
- Returns an instance of Deferred<T>. We need to invoke await() to get the value.
- We use this builder for coroutines that does have a result as the return value.

### Produce

- Produce builder is for coroutines which produces a stream of elements.
- Returns an instance of ReceiveChannel.

### RunBlocking

- The coroutine we create using this thread will block the thread while the coroutine is executing.
- Returns a result of type T.

[Coroutine](https://www.notion.so/9144ade9a114444588cc52e8af6748e0)

### Structured Concurrency

- Structured concurrency is a set of language features and best practices introduced for Kotlin Coroutines to avoid leaks and to manage them productively.

### Main Thread, Coroutine

```kotlin
// main thread
CoroutineScope(Dispatchers.Main).launch {
    for (i in 1..200000) {
        //get Thread name
        Log.i("Main", "Downloading user $i in ${Thread.currentThread().name}")
    }
}
```

### Background Thread, Coroutine

```kotlin
// background thread
CoroutineScope(Dispatchers.IO).launch {
    for (i in 1..200000) {
        //get Thread name
        Log.i("Background", "Downloading user $i in ${Thread.currentThread().name}")
    }
}
```

# Switch The Thread Of A Coroutine

- `suspend fun`
- `withContext(Dispatchers.Main)`
- If background tasks try to access to Android UI, app will crash
- You need to switch to Main Thread

```kotlin
//this function freezes other main thread operation
  private suspend fun downloadUserData() {
      //It freezes main thread
      for (i in 1..200000) {
          //get Thread name
          //Log.i("MyTag", "Downloading user $i in ${Thread.currentThread().name}")

          // If background tasks try to access to Android UI, app will crash
          //❗️It will crash.
          //tvUserMessage.text = "Downloading user $i in ${Thread.currentThread().name}"

          // 🧡 Switch from background thread to main thread to prevent app crash
          withContext(Dispatchers.Main) {
              tvUserMessage.text = "Downloading user $i in ${Thread.currentThread().name}"
          }

      }
  }
```

# Suspending Functions

### Explanation

- In Kotlin Coroutines, whenever a coroutine is suspended, the current stack frame of the function is copied and saved in the memory.
- When the function resumes after completing its task, the stack frame is copied back from where it was saved and starts running again.

### Suspending Functions

- withContext
- withTimeout
- withTimeoutOrNull
- join
- delay
- await
- supervisorScope
- coroutineScope

⇒ 위와 같은 suspend 함수들은 coroutine 내부에서만 사용이 가능하다.

### `suspend` keyword

- If we are going to use a suspending function, we have to mark out calling function with `suspend` modifier.
- A suspending function, can be invoked from a coroutine block or from an another suspending function only.

⇒ We can't use `suspend` functions which are not marked with `suspend` modifier.

### Coroutine and Suspend Functions Characteristics

- A coroutine can invoke both suspending and non-suspending functions.
- But a suspending function can be invoked from a coroutine block or from an another suspending function only.

### Code Example

```kotlin
private suspend fun getStock1(): Int {
    delay(10000)
    Log.d("MyTag", "stock 1 returned")
    return 55000
}
```

# How Suspending Functions Work

A suspending function doesn't block a thread,

**but only suspends the coroutine itself.** (one thread can have more coroutines)

The thread is returned to the pool while the coroutine is waiting,

and when the waiting is done,

the coroutine resumes on a free thread in the pool.

# Async & Await

### Before refactoring

```kotlin
fun main() {
	CoroutineScope(Dispatchers.IO).launch {
	      val stock1 = getStock1()
	      val stock2 = getStock2()
				val total = stock1 + stock2
				Log.i("MyTag", "Total is $total")
				//Total is 90000
	}
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

```

⇒ Concurrently하게 되지 않음.

⇒ Takes 18 seconds

### After refactoring

- Parallel Decomposition

```kotlin
fun main() {
	// Parallel Decomposition
    CoroutineScope(Dispatchers.IO).launch {
        Log.i("MyTag", "Calculation started...")
        val stock1 = async { getStock1()}
        val stock2 = async {getStock2() }
        val total = stock1.await() + stock2.await()
        Log.i("MyTag", "Total is $total")
    }
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
```

⇒ Takes 10 seconds 

### Access to Main Thread

- Parallel Decomposition, Async Await access to UI Thread

```kotlin
fun main() {
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
        Toast.makeText(this@MainActivity, total.toString(), Toast.LENGTH_LONG).show()
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
```

# Unstructured Concurrency, `CoroutineScope(Dispatchers.).launch`

ℹ️ Unstructured concurrency does not guarantee to complete all the tasks of the suspending function, before it returns.

- Unstructured Concurrency example

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    btnDownloadUserData.setOnClickListener {

        /*** Unstructured Concurrency ***/
        CoroutineScope(Dispatchers.Main).launch {
            tvUserMessage.text = getTotalUserCount().toString() //it returns 0
        }//=> this displays zero. It doesnt wait `return value` to be `50` as intented.
				

    }

}

suspend fun getTotalUserCount(): Int {
        var count = 0
        CoroutineScope(Dispatchers.IO).launch {
            //simulate long running task
            delay(1000)
            count = 50
        }
      return count
}
```

- prevent unstructured concurrency with async await returned `deferred`

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    btnDownloadUserData.setOnClickListener {

        /*** Unstructured Concurrency ***/
        CoroutineScope(Dispatchers.Main).launch {
            tvUserMessage.text = getTotalUserCount().toString() //it returns 0
        }//=> this displays 70. 
				

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
        return@async 70
    }

    return count + deferred.await()
}
```

# Structured Concurrency, `coroutineScope`

ℹ️ coroutineScope 

- Can use suspending function.
- coroutineScope guarantees the completion of a task.
- Structured concurrency guarantees to complete all the works started by coroutines within the child scope before the return of the suspending function.
- Structured concurrency helps us to keep track of tasks we started and to cancel them when needed.

### Code Example

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_structured_concurrency)

        btnDownloadUserData.setOnClickListener {

            /*** Calling structured Concurrency Function ***/
            CoroutineScope(Dispatchers.Main).launch {
                tvUserMessage.text = getTotalUserCount().toString() //it returns 0
            }
						// it returns 120

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
```

# CoroutineScope vs coroutineScope

- Unstructured vs Structured
- CoroutineScope, unstructured concurrency

    ⇒ does not guarantee the completion of a task

- coroutineScope, structured concurrency

    ⇒ guarantees the completion of a task

- CoroutineScope is an interface
- coroutineScope is a suspending function

### Best Practice

- Unstructured Coroutine으로 structured `coroutine suspend function` 을 호출하는 방법이 가장 좋다.

# ViewModel Scope

ℹ️ viewModelScope is a `CoroutineScope` tied to a ViewModel.

ℹ️ You can use all `CoroutineScope` features with `viewModelScope`

### viewModel Scope

A **`ViewModelScope`** is defined for each **`[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)`** in your app. Any coroutine launched in this scope is automatically canceled if the **`ViewModel`** is cleared. Coroutines are useful here for when you have work that needs to be done only if the **`ViewModel`** is active. For example, if you are computing some data for a layout, you should scope the work to the **`ViewModel`** so that if the **`ViewModel`**is cleared, the work is canceled automatically to avoid consuming resources.

You can access the **`CoroutineScope`** of a **`ViewModel`** through the**`viewModelScope`** property of the ViewModel, as shown in the following example:

### Dependency

```kotlin
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0'
```

### MainActivityViewModel without `ViewModelScope`

```kotlin
class MainActivityViewModel: ViewModel() {

    // prevent leaking memory
    private val job = Job()
    
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    fun getUserData(){

        coroutineScope.launch {
            //write some code

        }

    }

    // prevent leaking memory
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}
```

### MainActivityViewModel with `ViewModelScope`

```kotlin
class MainActivityViewModel: ViewModel() {

    fun getUserData(){
       viewModelScope.launch {

       }
    }

}
```

### Small Project example

- User.kt (model)

```kotlin
data class User(val id: Int, val name: String) 
```

- UserRepository.kt

```kotlin
class UserRepository {

    suspend fun getUsers(): List<User> {
        delay(8000) // simulate long operation
        val users: List<User> = listOf(
            User(1, "Shin"),
            User(1, "Lee"),
            User(1, "Kim"),
            User(1, "Park")
        )
        return users
    }

}
```

- ViewModel

```kotlin
class MainActivityViewModel: ViewModel() {

    private var userRepository = UserRepository()

    var users: MutableLiveData<List<User>> = MutableLiveData()

    fun getUserData(){
       viewModelScope.launch {
           var result: List<User>? = null

           //Learn on the background
           withContext(Dispatchers.IO) {
               result = userRepository.getUsers()
           }
           users.value = result
       }
    }

}
```

- Activity

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.getUserData()
        mainActivityViewModel.users.observe(this, Observer { users ->
            users.forEach {
                Log.i("MyTag", it.name)
            }
        })
    }

}
```

# LifeCycle Scope

ℹ️ lifecycleScope

ℹ️ You can use all `CoroutineScope` features with `lifeCycleScope` for specific moment

### LifecycleScope

- A **`LifecycleScope`** is defined for each **`[Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)`** object. Any coroutine launched in this scope is canceled when the **`Lifecycle`** is destroyed. You can access the **`CoroutineScope`** of the **`Lifecycle`** either via**`lifecycle.coroutineScope`** or **`lifecycleOwner.lifecycleScope`**properties.
- The example below demonstrates how to use **`lifecycleOwner.lifecycleScope`** to create precomputed text asynchronously:

### Dependency

```kotlin
implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.2.0'
```

### Small Project

- ViewModel

```kotlin
class MainViewModel: ViewModel() {
}
```

- Activity

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }

        lifecycleScope.launch {
            delay(5000)
            progressBar.visibility = View.VISIBLE
            delay(10000)
            progressBar.visibility = View.GONE
        }

        lifecycleScope.launch(Dispatchers.IO) {
            Log.i("MyTag", "Thread is : ${Thread.currentThread().name}")
        }

        lifecycleScope.launchWhenCreated {

        }

        lifecycleScope.launchWhenStarted {

        }

        lifecycleScope.launchWhenResumed {

        }

    }
}
```

- fragment which contains `viewModel`

```kotlin
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
```

# Live Data Builder

### Official doc's explanation

## Use coroutines with LiveData

When using **`[LiveData](https://developer.android.com/topic/libraries/architecture/livedata)`**, you might need to calculate values asynchronously. For example, you might want to retrieve a user's preferences and serve them to your UI. In these cases, you can use the **`liveData`** builder function to call a **`suspend`** function, serving the result as a **`LiveData`** object.

In the example below, **`loadUser()`** is a suspend function declared elsewhere. Use the **`liveData`** builder function to call **`loadUser()`** asynchronously, and then use **`emit()`** to emit the result:

```kotlin
val user: LiveData<User> = liveData {
    val data = database.loadUser() // loadUser is a suspend function.
    emit(data)
}
```

The **`liveData`** building block serves as a [structured concurrency primitive](https://medium.com/@elizarov/structured-concurrency-722d765aa952)between coroutines and **`LiveData`**. The code block starts executing when**`LiveData`** becomes active and is automatically canceled after a configurable timeout when the **`LiveData`** becomes inactive. If it is canceled before completion, it is restarted if the **`LiveData`** becomes active again. If it completed successfully in a previous run, it doesn't restart. Note that it is restarted only if canceled automatically. If the block is canceled for any other reason (e.g. throwing a **`CancellationException`**), it is **not** restarted.

You can also emit multiple values from the block. Each **`emit()`** call suspends the execution of the block until the **`LiveData`** value is set on the main thread.

```kotlin
val user: LiveData<Result> = liveData {
    emit(Result.loading())
    try {
        emit(Result.success(fetchUser()))
    } catch(ioException: Exception) {
        emit(Result.error(ioException))
    }

```

You can also combine **`liveData`** with **`[Transformations](https://developer.android.com/reference/androidx/lifecycle/Transformations)`**, as shown in the following example:

```kotlin
class MyViewModel: ViewModel() {
    private val userId: LiveData<String> = MutableLiveData()
    val user = userId.switchMap { id ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(database.loadUserById(id))
        }
    }
}
```

You can emit multiple values from a **`LiveData`** by calling the **`emitSource()`**function whenever you want to emit a new value. Note that each call to **`emit()`**or **`emitSource()`** removes the previously-added source.

```kotlin
class UserDao: Dao {
    @Query("SELECT * FROM User WHERE id = :id")
    fun getUser(id: String): LiveData<User>
}

class MyRepository {
    fun getUser(id: String) = liveData<User> {
        val disposable = emitSource(
            userDao.getUser(id).map {
                Result.loading(it)
            }
        )
        try {
            val user = webservice.fetchUser(id)
            // Stop the previous emission to avoid dispatching the updated user
            // as `loading`.
            disposable.dispose()
            // Update the database.
            userDao.insert(user)
            // Re-establish the emission with success type.
            emitSource(
                userDao.getUser(id).map {
                    Result.success(it)
                }
            )
        } catch(exception: IOException) {
            // Any call to `emit` disposes the previous one automatically so we don't
            // need to dispose it here as we didn't get an updated value.
            emitSource(
                userDao.getUser(id).map {
                    Result.error(exception, it)
                }
            )
        }
    }
}
```

For more coroutines-related information, see the following links:

- [Improve app performance with Kotlin coroutines](https://developer.android.com/kotlin/coroutines)
- [Coroutines overview](https://kotlinlang.org/docs/reference/coroutines-overview.html)
- [Threading in CoroutineWorker](https://developer.android.com/topic/libraries/architecture/workmanager/advanced/coroutineworker)

### Dependency

```kotlin
implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
```

### Project

- model

```kotlin
data class User(val id: Int, val name: String)
```

- repository

```kotlin
class UserRepository {

    suspend fun getUsers(): List<User> {
        delay(8000) // simulate long operation
        val users: List<User> = listOf(
            User(1, "Shin"),
            User(1, "Lee"),
            User(1, "Kim"),
            User(1, "Park")
        )
        return users
    }

}
```

- viewmodel before refactoring

```kotlin
class MainActivityViewModel: ViewModel() {

    private var userRepository = UserRepository()

    var users: MutableLiveData<List<User>> = MutableLiveData()

    fun getUserData(){
       viewModelScope.launch {
           var result: List<User>? = null

           //Learn on the background
           withContext(Dispatchers.IO) {
               result = userRepository.getUsers()
           }
           users.value = result
       }
    }

}
```

- viewmodel refactored with `LiveData builder`

```kotlin
class MainActivityViewModel: ViewModel() {

    private var userRepository = UserRepository()
    var users = liveData(Dispatchers.IO) {
        val result = userRepository.getUsers()
        emit(result)
    }

//    var users: MutableLiveData<List<User>> = MutableLiveData()
//
//    fun getUserData(){
//       viewModelScope.launch {
//           var result: List<User>? = null
//
//           //Learn on the background
//           withContext(Dispatchers.IO) {
//               result = userRepository.getUsers()
//           }
//           users.value = result
//       }
//    }

}
```

- acitivity

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
//        mainActivityViewModel.getUserData()
        mainActivityViewModel.users.observe(this, Observer { users ->
            users.forEach {
                Log.i("MyTag", it.name)
            }
        })
    }

}
```