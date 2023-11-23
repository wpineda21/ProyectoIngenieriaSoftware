package com.mcmp2023.s.ui.for_you.product.userProducts.userProducstRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.databinding.ProductItemBinding

class UserProductsVIewAdapter() :
    RecyclerView.Adapter<UserProductsViewHolder>() {

    private val products = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProductsViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserProductsViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size ?: 0

    override fun onBindViewHolder(holder: UserProductsViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    fun setData(productsList: List<Product>) {
        products.clear()
        products.addAll(productsList)
    }

}