package com.mcmp2023.s.repositories

import com.mcmp2023.s.data.db.models.UserModel

class UserRepository(private val users: MutableList<UserModel>) {
    fun getUsers() = users
    fun addUser(newUser: UserModel) = users.add(newUser)


}