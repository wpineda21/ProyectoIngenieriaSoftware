package com.mcmp2023.s.ui.admin.adminusers.usersrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcmp2023.s.data.db.models.UserModel
import com.mcmp2023.s.databinding.UserItemBinding

class UsersRecyclerViewAdapter(
    private val productsClickListener: (UserModel) -> Unit,
    private val deleteClickListener: (UserModel) -> Unit
) : RecyclerView.Adapter<UsersRecyclerViewHolder>() {

    private val users = ArrayList<UserModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersRecyclerViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersRecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size ?: 0

    override fun onBindViewHolder(holder: UsersRecyclerViewHolder, position: Int) {
        val user = users[position]

        holder.bind(user, productsClickListener, deleteClickListener)
    }

    fun setData(usersList: List<UserModel>) {
        users.clear()
        users.addAll(usersList)
    }


}