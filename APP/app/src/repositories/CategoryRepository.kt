package com.mcmp2023.s.repositories

import com.mcmp2023.s.data.categories
import com.mcmp2023.s.data.db.models.Category

class CategoryRepository(private val category: MutableList<Category>) {

    fun getCategories() = categories

}