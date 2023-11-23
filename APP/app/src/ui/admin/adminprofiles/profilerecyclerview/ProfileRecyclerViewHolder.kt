package com.mcmp2023.s.ui.admin.adminprofiles.profilerecyclerview

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mcmp2023.s.R
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.databinding.ProductItemBinding

class ProfileRecyclerViewHolder(private val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(product: Product, clickListener: (Product) -> Unit) {
        binding.cardProductName.text = product.tittle
        binding.cardProductPrice.text = "$ ${product.price}"
        binding.favoriteImageView.setImageResource(R.drawable.trash)

        val imageName = product.image?.substringAfterLast("/")

        Glide
            .with(binding.root)
            .load("https://sybapimarketplace.shop/uploads//${imageName}")
            .error(R.drawable.no_image_icon)
            .into(binding.productImageItem)

        binding.productCard.setOnClickListener {
            clickListener(product)
        }

        binding.favoriteImageView.setOnClickListener {

        }
    }

}