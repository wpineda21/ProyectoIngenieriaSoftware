package com.mcmp2023.s.ui.account.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.R
import com.mcmp2023.s.databinding.FragmentProfileBinding
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    val app by lazy {
        requireActivity().application as ProductApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.usernameTextView.text = app.getUsername()

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        binding.productsButton.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_userProductsFragment)
        }

        binding.profileBackArrowImageView.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_forYouFragment)
        }
    }

}