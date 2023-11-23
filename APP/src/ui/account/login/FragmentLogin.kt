package com.mcmp2023.s.ui.account.login

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.R
import com.mcmp2023.s.databinding.FragmentLoginBinding
import com.mcmp2023.s.ui.account.login.viewmodel.LoginViewModel


class fragmentLogin : Fragment() {
    private lateinit var binding: FragmentLoginBinding // Declaration of the data binding variable

    // Dependency injection to obtain a shared instance of the ViewModel
    private val loginViewModel: LoginViewModel by activityViewModels {
        LoginViewModel.Factory
    }

    //Getting the current application
    val app by lazy {
        requireActivity().application as ProductApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflating the view using data binding
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel() //setting the view model on the data binding
        observeStatus() // Observing change in the login status
        showPassword() // showing or hiding password
        validateLogin()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.otheractivitys)
        }
        //setting click listener for the register button
        binding.actionRegisterTextView.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentLogin_to_createAccount)
        }

        //setting click listener for the restore text view
        binding.forgotPasswordTextView.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentLogin_to_forgotPasswordFragment)
        }


    }


    //function to set the view model on the data binding
    private fun setViewModel(){
        binding.viewmodel = loginViewModel
    }

    //function to observe changes in the login status
    private fun observeStatus(){
        loginViewModel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status) //handling the login status and updating the UI
        }
    }

    private fun handleUiStatus(status: LoginUiStatus){ //different login status
        when(status){
            //case error show An error has occurred
            is LoginUiStatus.Error -> {
                Toast.makeText(requireContext(), "An error has occurred",Toast.LENGTH_SHORT).show()
            }
            //case errorWithMessage show status message
            is LoginUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            //case success clear status and data the save token and pass to foryoufragment
            is LoginUiStatus.Success -> {
                val role = status.response.role
                val name = status.response.name
                app.saveUserRole(role)
                app.saveUserName(name)
                loginViewModel.clearStatus()
                loginViewModel.clearData()

                if (role == "user") {
                    app.saveAuthToken(status.response.token)
                    findNavController().navigate(R.id.forYouFragment)
                }
                else{
                    app.saveAuthToken(status.response.token)
                    findNavController().navigate(R.id.action_fragmentLogin_to_adminUserFragment)
                }
            }
            else -> {}
        }
    }

    //validate there are no empty fields
    private fun validateLogin() {
        val email = binding.TextFieldLoginEmail.text.toString().trim()
        val password = binding.TextFieldLoginPassword.text.toString().trim()

        if (email.isBlank()) {
            binding.TextFieldLoginEmail.error = "Este campo es necesario"
        }
        if (password.isBlank()) {
            binding.TextFieldLoginPassword.error = "Este campo es neceario"
        }
    }


    private fun showPassword() {
        binding.showPasswordImageView.setOnClickListener {
            if (binding.TextFieldLoginPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                // hiding password
                binding.TextFieldLoginPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.showPasswordImageView.setImageResource(R.drawable.eye_closed_icon)
            } else {
                // showing password
                binding.TextFieldLoginPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.showPasswordImageView.setImageResource(R.drawable.eye_icon)
            }
        }
    }


}