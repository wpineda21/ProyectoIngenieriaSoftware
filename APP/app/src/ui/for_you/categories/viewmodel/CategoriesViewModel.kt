package com.mcmp2023.s.ui.for_you.categories.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.data.db.models.Category
import com.mcmp2023.s.repositories.CategoryRepository

class CategoriesViewModel(private val repository: CategoryRepository) : ViewModel() {

    var name = ""

    fun setSelectedCategory(category: Category) {
        name = category.name
    }

    fun clearData() {
        name = ""
    }

    fun getCategories() = repository.getCategories()


    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as ProductApplication
               CategoriesViewModel(app.categoryRepository)
            }
        }
    }
}