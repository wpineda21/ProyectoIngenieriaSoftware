package com.mcmp2023.s.ui.for_you.product.recyclerview_product

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.R
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.databinding.FragmentProductRecyclerViewBinding
import com.mcmp2023.s.ui.for_you.categories.viewmodel.CategoriesViewModel
import com.mcmp2023.s.ui.for_you.product.descriptionproduct.viewmodel.DescriptionViewModel
import com.mcmp2023.s.ui.for_you.product.recyclerview_product.viewmodel.ProductRecyclerViewModel
import kotlinx.coroutines.launch

class ProductRecyclerViewFragment : Fragment() {

    private lateinit var binding: FragmentProductRecyclerViewBinding

    private lateinit var adapter: ProductRecyclerViewAdapter

    private lateinit var myRecyclerView: RecyclerView


    private val app by lazy {
        requireActivity().application as ProductApplication
    }

    private val productRecyclerViewModel : ProductRecyclerViewModel by activityViewModels {
        ProductRecyclerViewModel.Factory
    }
    private val descriptionViewModel : DescriptionViewModel by activityViewModels()

    private val categoriesViewModel : CategoriesViewModel by activityViewModels {
        CategoriesViewModel.Factory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productRecyclerViewModel.fetchProducts(app.getToken(), categoriesViewModel.name , productRecyclerViewModel.productName )

        setRecyclerView(view)
    }

    private fun setRecyclerView(view: View) {
        binding.productsRecyclerView.layoutManager = GridLayoutManager(view.context, 2)

        adapter = ProductRecyclerViewAdapter ({
            showSelectedPlants(it)
        }, {
            productRecyclerViewModel.toggleFavorites(it)
        }, productRecyclerViewModel)

        binding.productsRecyclerView.adapter = adapter
        displayProducts()


    }

   @SuppressLint("NotifyDataSetChanged")
   private fun displayProducts() {

       productRecyclerViewModel.products.observe(viewLifecycleOwner) {
           adapter.setData(it)
           adapter.notifyDataSetChanged()
       }

   }

   private fun showSelectedPlants(product: Product) {

       val navHost = activity?.findNavController(R.id.nav_host_fragment)

       descriptionViewModel.setSelectedProduct(product)
       navHost?.navigate(R.id.action_forYouFragment_to_productDescriptionFragment)
   }

}