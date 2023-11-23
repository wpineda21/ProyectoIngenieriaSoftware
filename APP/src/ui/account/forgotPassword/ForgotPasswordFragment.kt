package com.mcmp2023.s.ui.account.forgotPassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.R
import com.mcmp2023.s.databinding.FragmentForgotPasswordBinding
import com.mcmp2023.s.ui.account.forgotPassword.viewmodel.ForgotPasswordViewmodel
import com.mcmp2023.s.ui.account.login.LoginUiStatus


class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding

    private val forgotPasswordViewmodel: ForgotPasswordViewmodel by activityViewModels {
        ForgotPasswordViewmodel.Factory
    }

    val app by lazy {
        requireActivity().application as ProductApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        observeStatus()
        addListenners()
        validateData()
    }

    private fun setViewModel() {
        binding.viewmodel = forgotPasswordViewmodel
    }

    private fun observeStatus() {
        forgotPasswordViewmodel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
        }
    }

    private fun handleUiStatus(status: ForgotPasswordUiStatus) { //different login status
        when (status) {
            //case error show An error has occurred
            is ForgotPasswordUiStatus.Error -> {
                Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show()
            }
            //case errorWithMessage show status message
            is ForgotPasswordUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            //case success clear status and data the save token and pass to foryoufragment
            is ForgotPasswordUiStatus.Success -> {
                cleanApplication()
                forgotPasswordViewmodel.clearStatus()
                forgotPasswordViewmodel.clearData()
                Toast.makeText(requireContext(), "Se ha enviado un codigo a su correo electronico", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_forgotPasswordFragment_to_restorePasword)
            }

            else -> {}
        }
    }

    private fun addListenners(){
        binding.actionBackfpLogin.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun cleanApplication(){
        app.saveUserName("")
        app.saveUserRole("")
        app.saveAuthToken("")
    }

    private fun validateData(){
        if (binding.emailForgotEdittext.text.isNullOrEmpty()){
            binding.emailForgotEdittext.error = "Este campo no debe ir vacio"
        }
    }


}