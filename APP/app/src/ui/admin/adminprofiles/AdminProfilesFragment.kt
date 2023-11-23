package com.mcmp2023.s.ui.admin.adminprofiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.databinding.FragmentAdminprofilesViewBinding
import com.mcmp2023.s.ui.admin.adminprofiles.profilerecyclerview.ProfileRecyclerViewAdapter
import com.mcmp2023.s.ui.admin.adminprofiles.viewmodel.AdminProfilesViewModel

class AdminProfilesFragment : Fragment() {

    private lateinit var binding: FragmentAdminprofilesViewBinding

    private lateinit var adapter: ProfileRecyclerViewAdapter

    private val viewModel: AdminProfilesViewModel by activityViewModels {
        AdminProfilesViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminprofilesViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileNameTextView.text = viewModel.name
        binding.actionBackfpLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.onGetProductsByUser()
        setRecyclerView(view)
        observeStatus()

    }

    private fun setRecyclerView(view: View) {
        binding.profileRecyclerView.layoutManager = GridLayoutManager(view.context, 2)

        adapter = ProfileRecyclerViewAdapter {
            showSelectedProduct(it)
        }

        binding.profileRecyclerView.adapter = adapter
    }


    private fun showSelectedProduct(product: Product) {
        viewModel.setSelectedProduct(product)
        findNavController()
    }

    private fun observeStatus() {
        viewModel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
        }
    }

    private fun handleUiStatus(status: AdminProfilesUiStatus){
        when(status){
            is AdminProfilesUiStatus.Error -> {
                Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show()
            }
            is AdminProfilesUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            is AdminProfilesUiStatus.Success -> {
                adapter.setData(status.response)
                adapter.notifyDataSetChanged()
            }
            else -> {}
        }
    }


}