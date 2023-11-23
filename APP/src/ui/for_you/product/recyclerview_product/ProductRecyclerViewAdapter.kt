package com.mcmp2023.s.ui.for_you.product.recyclerview_product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.databinding.ProductItemBinding
import com.mcmp2023.s.ui.for_you.product.recyclerview_product.viewmodel.ProductRecyclerViewModel

class ProductRecyclerViewAdapter(
    private val descriptionClickListener: (Product) -> Unit,
    private val favClickListener: (Product) -> Unit,
    private val productViewModel: ProductRecyclerViewModel
) : RecyclerView.Adapter<ProductRecyclerViewHolder>() {

    private val products = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRecyclerViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductRecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size ?: 0

    override fun onBindViewHolder(holder: ProductRecyclerViewHolder, position: Int) {
        val product = products[position]

        holder.bind(product, descriptionClickListener, productViewModel)
    }

    fun setData(productsList: List<Product>) {
        products.clear()
        products.addAll(productsList)

    }
}