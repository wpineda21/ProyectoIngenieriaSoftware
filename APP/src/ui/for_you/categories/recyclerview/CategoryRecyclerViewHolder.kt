package com.mcmp2023.s.ui.for_you.categories.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.mcmp2023.s.data.db.models.Category
import com.mcmp2023.s.databinding.CategoryItemBinding

class CategoryRecyclerViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(category: Category, clickListener: (Category) ->  Unit) {
        binding.categoryNameTextView.text = category.name
        binding.categoryIcon.setImageResource(category.icon)

        binding.categoryItem.setOnClickListener {
            clickListener(category)
        }
    }

}