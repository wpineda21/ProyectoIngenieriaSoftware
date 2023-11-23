package com.mcmp2023.s.ui.admin.adminprofiles.profilerecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.databinding.ProductItemBinding

class ProfileRecyclerViewAdapter(private val clickListener: (Product) -> Unit) : RecyclerView.Adapter<ProfileRecyclerViewHolder>() {

    private val products = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileRecyclerViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileRecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size ?: 0

    override fun onBindViewHolder(holder: ProfileRecyclerViewHolder, position: Int) {
        val product = products[position]

        holder.bind(product, clickListener)
    }

    fun setData(productsList: List<Product>) {
        products.clear()
        products.addAll(productsList)
    }
}