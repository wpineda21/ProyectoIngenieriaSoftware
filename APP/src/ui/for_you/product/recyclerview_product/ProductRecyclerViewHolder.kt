package com.mcmp2023.s.ui.for_you.product.recyclerview_product

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mcmp2023.s.R
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.databinding.ProductItemBinding
import com.mcmp2023.s.ui.for_you.product.recyclerview_product.viewmodel.ProductRecyclerViewModel

class ProductRecyclerViewHolder(private val binding: ProductItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(
        product: Product,
        descriptionClickListener: (Product) -> Unit,
        productViewModel: ProductRecyclerViewModel
    ) {
        binding.cardProductName.text = product.tittle
        binding.cardProductPrice.text = "$ ${product.price.toString()}"

        val imageName = product.image?.substringAfterLast("/")

        Glide
            .with(binding.root)
            .load("https://sybapimarketplace.shop/uploads//${imageName}")
            .error(R.drawable.no_image_icon)
            .into(binding.productImageItem)

        binding.productCard.setOnClickListener {
            descriptionClickListener(product)
        }

        val isFavorite = productViewModel.favoriteProduct.contains(product)
        if (isFavorite) {
            binding.favoriteImageView.setImageResource(R.drawable.baseline_bookmark)
        } else {
            binding.favoriteImageView.setImageResource(R.drawable.bookmark)
        }

        binding.favoriteImageView.setOnClickListener {
            productViewModel.toggleFavorites(product)

            if (isFavorite) {
                binding.favoriteImageView.setImageResource(R.drawable.baseline_bookmark)
            } else {
                binding.favoriteImageView.setImageResource(R.drawable.bookmark)
            }

        }
    }
}