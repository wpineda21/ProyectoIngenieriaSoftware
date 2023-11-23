package com.mcmp2023.s.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.R

class StartFragment : Fragment() {

    val app by lazy {
        requireActivity().application as ProductApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(app.getToken() != "" && app.getUsername() != "" && app.getUserRole() != ""){
            if (app.getUserRole() == "user"){
                findNavController().navigate(R.id.action_startFragment_to_forYouFragment)
            }
            else{
                findNavController().navigate(R.id.action_startFragment_to_adminUserFragment)
            }
        }else{
            view.findViewById<Button>(R.id.btn_start_to_login).setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_createAccount)
            }
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.tertiary)
        }
    }
}