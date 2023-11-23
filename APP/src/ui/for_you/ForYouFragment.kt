package com.mcmp2023.s.ui.for_you

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.R
import com.mcmp2023.s.databinding.FragmentForYouBinding
import com.mcmp2023.s.ui.for_you.categories.viewmodel.CategoriesViewModel
import com.mcmp2023.s.ui.for_you.product.recyclerview_product.viewmodel.ProductRecyclerViewModel

class ForYouFragment : Fragment() {

    //binding Instance
    private lateinit var binding: FragmentForYouBinding

    private val categoriesViewModel: CategoriesViewModel by activityViewModels()

    private val productViewModel: ProductRecyclerViewModel by activityViewModels {
        ProductRecyclerViewModel.Factory
    }

    //Getting application
    val app by lazy {
        requireActivity().application as ProductApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        navigation()
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)
        binding.forYouTextView.setBackgroundResource(R.drawable.textview_hover)
        binding.categoryTextView.setBackgroundResource(0)

        binding.searchImageView.setOnClickListener {
            productViewModel.productName = binding.searchEditText.text.toString()
            productViewModel.fetchProducts(token = "", categoryName = "", name = productViewModel.productName)
            Log.d("string", productViewModel.productName)
        }


    }

    private fun navigation() {

        val navHost = childFragmentManager.findFragmentById(R.id.for_you_nav_host) as NavHostFragment
        val navController = navHost.navController

        //Go to SellProductFragment
        binding.sellTextView.setOnClickListener {
            findNavController().navigate(R.id.action_forYouFragment_to_sellProductFragment)

        }

        binding.forYouTextView.setOnClickListener {

            val currentDestination =  navController.currentDestination
            val currentDestinationId = currentDestination?.id

            categoriesViewModel.clearData()
            productViewModel.productName = ""


            if (currentDestinationId != R.id.productRecyclerViewFragment) {
                navController.navigate(R.id.action_categoriesFragment_to_productRecyclerViewFragment)
                binding.forYouTextView.setBackgroundResource(R.drawable.textview_hover)
                binding.categoryTextView.setBackgroundResource(0)
            }
        }

        binding.categoryTextView.setOnClickListener {

            val currentDestination =  navController.currentDestination
            val currentDestinationId = currentDestination?.id

            if (currentDestinationId != R.id.categoriesFragment) {
                navController.navigate(R.id.action_productRecyclerViewFragment_to_categoriesFragment)
                binding.categoryTextView.setBackgroundResource(R.drawable.textview_hover)
                binding.forYouTextView.setBackgroundResource(0)
            }
        }

        binding.userImageView.setOnClickListener {
            findNavController().navigate(R.id.action_forYouFragment_to_profileFragment)

        }

    }

    private fun setViewModel() {
        binding.productViewModel = productViewModel
    }



}