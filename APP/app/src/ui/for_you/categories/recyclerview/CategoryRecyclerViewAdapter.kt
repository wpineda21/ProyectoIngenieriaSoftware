package com.mcmp2023.s.ui.for_you.categories.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcmp2023.s.data.db.models.Category
import com.mcmp2023.s.databinding.CategoryItemBinding

class CategoryRecyclerViewAdapter(private val clickListener : (Category) -> Unit) : RecyclerView.Adapter<CategoryRecyclerViewHolder>() {

    private val categories = ArrayList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryRecyclerViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryRecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size ?: 0

    override fun onBindViewHolder(holder: CategoryRecyclerViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, clickListener)
    }

    fun setData(categoriesList: List<Category>) {
        categories.clear()
        categories.addAll(categoriesList)
    }
}