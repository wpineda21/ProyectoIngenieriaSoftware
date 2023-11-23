package com.mcmp2023.s.ui.for_you.product.userProducts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.R
import com.mcmp2023.s.databinding.FragmentUserProductsBinding
import com.mcmp2023.s.ui.for_you.product.sellproduct.SellProductUiStatus
import com.mcmp2023.s.ui.for_you.product.userProducts.userProducstRecyclerView.UserProductsVIewAdapter
import com.mcmp2023.s.ui.for_you.product.userProducts.viewmodel.UserProductViewmodel

class UserProductsFragment : Fragment() {

    private lateinit var binding: FragmentUserProductsBinding

    private lateinit var adapter: UserProductsVIewAdapter

    private val getUserProductViewmodel: UserProductViewmodel by activityViewModels {
        UserProductViewmodel.Factory
    }

    val app by lazy {
        requireActivity().application as ProductApplication
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserProductViewmodel.onGetUserProducts(app.getToken())

        setRecyclerView(view)
        observeStatus()
    }


    private fun setRecyclerView(view: View){
        binding.userProductRecyclerView.layoutManager = GridLayoutManager(view.context, 2)
        adapter = UserProductsVIewAdapter()
        binding.userProductRecyclerView.adapter = adapter
    }

    private fun observeStatus(){
        getUserProductViewmodel.status.observe(viewLifecycleOwner){ status ->
            handleUiStatus(status)
        }
    }

    private fun handleUiStatus(status: UserProductsUiStatus){
        when(status){
            is UserProductsUiStatus.Error -> {
                Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show()
            }
            is UserProductsUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            is UserProductsUiStatus.Success -> {
                adapter.setData(status.response)
                adapter.notifyDataSetChanged()
            }
            else -> {}
        }
    }

}