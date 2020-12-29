package com.paigesoftware.myapplication.model

import kotlinx.coroutines.delay

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