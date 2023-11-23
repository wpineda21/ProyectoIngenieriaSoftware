package com.mcmp2023.s.ui.for_you.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcmp2023.s.R
import com.mcmp2023.s.data.db.models.Category
import com.mcmp2023.s.databinding.FragmentCategoriesBinding
import com.mcmp2023.s.ui.for_you.categories.recyclerview.CategoryRecyclerViewAdapter
import com.mcmp2023.s.ui.for_you.categories.viewmodel.CategoriesViewModel
import com.mcmp2023.s.ui.for_you.product.recyclerview_product.viewmodel.ProductRecyclerViewModel

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    private lateinit var adapter: CategoryRecyclerViewAdapter

    private val CategoryViewModel : CategoriesViewModel by activityViewModels {
        CategoriesViewModel.Factory
    }

    private val ProductViewModel : ProductRecyclerViewModel by activityViewModels {
        ProductRecyclerViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)
        setAdapter(view)
    }

    private fun setAdapter(view: View) {
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(view.context)

        adapter = CategoryRecyclerViewAdapter{
            showSelectedCategory(it)
        }

        binding.categoryRecyclerView.adapter = adapter
        displayCategories()

    }

    private fun displayCategories() {
        adapter.setData(CategoryViewModel.getCategories())
        adapter.notifyDataSetChanged()
    }

    private fun showSelectedCategory(category: Category) {
        CategoryViewModel.setSelectedCategory(category)
        ProductViewModel.clearProducts()
        findNavController().navigate(R.id.action_categoriesFragment_to_productRecyclerViewFragment)
    }


}
