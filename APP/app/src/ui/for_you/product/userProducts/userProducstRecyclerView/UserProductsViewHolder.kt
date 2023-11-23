package com.mcmp2023.s.ui.for_you.product.userProducts.userProducstRecyclerView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mcmp2023.s.R
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.databinding.ProductItemBinding

class UserProductsViewHolder (private val binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product){
        binding.cardProductName.text = product.tittle
        binding.cardProductPrice.text = "$ ${product.price.toString()}"

        val imageName = product.image?.substringAfterLast("/")

        Glide
            .with(binding.root)
            .load("https://sybapimarketplace.shop/uploads//${imageName}")
            .error(R.drawable.no_image_icon)
            .into(binding.productImageItem)


    }

}