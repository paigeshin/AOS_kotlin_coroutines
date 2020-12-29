package com.paigesoftware.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.paigesoftware.myapplication.model.User
import com.paigesoftware.myapplication.model.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

