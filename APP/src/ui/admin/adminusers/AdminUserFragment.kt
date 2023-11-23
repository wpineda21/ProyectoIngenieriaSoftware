package com.mcmp2023.s.ui.admin.adminusers

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.R
import com.mcmp2023.s.data.db.models.UserModel
import com.mcmp2023.s.databinding.FragmentAdminUserBinding
import com.mcmp2023.s.ui.admin.adminprofiles.viewmodel.AdminProfilesViewModel
import com.mcmp2023.s.ui.admin.adminusers.usersrecyclerview.UsersRecyclerViewAdapter
import com.mcmp2023.s.ui.admin.adminusers.viewmodel.AdminUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdminUserFragment : Fragment() {

    private lateinit var binding: FragmentAdminUserBinding
    private lateinit var adapter: UsersRecyclerViewAdapter

    private val userViewModel : AdminUserViewModel by activityViewModels {
        AdminUserViewModel.Factory
    }

    private val profileViewModel : AdminProfilesViewModel by activityViewModels {
        AdminProfilesViewModel.Factory
    }

    private val app by lazy {
        requireActivity().application as ProductApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.onGetUsers()
        setRecyclerView(view)
        observeStatus()
        addListenners()
    }


    private fun setRecyclerView(view: View) {
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(view.context)

        adapter = UsersRecyclerViewAdapter ({
            showSelectedUser(it)
        }, {
            deleteUser(it)
        })

        binding.usersRecyclerView.adapter = adapter

    }


    private fun showSelectedUser(user: UserModel) {
        profileViewModel.setSelectedUser(user)
        findNavController().navigate(R.id.action_adminUserFragment_to_adminProfilesFragment)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteUser(user: UserModel) {
        CoroutineScope(Dispatchers.Main).launch {
            val token = app.getToken()
            userViewModel.deleteUser("Bearer $token", user.ID)
            userViewModel.onGetUsers()
            adapter.notifyDataSetChanged()
        }
    }

    private fun addListenners(){
        binding.actionAdminSettings.setOnClickListener{
            findNavController().navigate(R.id.action_adminUserFragment_to_adminSettingsFragment)
        }
    }

    private fun observeStatus() {
        userViewModel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
        }
    }

    private fun handleUiStatus(status: AdminUserUiStatus){
        when(status){
            is AdminUserUiStatus.Error -> {
                Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show()
            }
            is AdminUserUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            is AdminUserUiStatus.Success -> {
                adapter.setData(status.response)
                adapter.notifyDataSetChanged()
            }
            else -> {}
        }
    }


}