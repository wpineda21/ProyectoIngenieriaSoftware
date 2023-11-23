package com.mcmp2023.s.ui.account.register

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
import com.mcmp2023.s.databinding.FragmentCreateAccountBinding
import com.mcmp2023.s.ui.account.register.viewmodel.RegisterViewModel

class createAccount : Fragment() {
    private lateinit var binding: FragmentCreateAccountBinding // Declaration of the data binding variable

    // Dependency injection to obtain a shared instance of the ViewModel
    private val registerViewModel: RegisterViewModel by activityViewModels {
        RegisterViewModel.Factory
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
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel() //setting the view model on the data binding
        setObserver() // Observing change in the login status
        showPassword() // showing or hiding password
        validate()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.otheractivitys)
        }
        //setting click listener to login fragment
        binding.registerButton.setOnClickListener {
            validate()
            findNavController().navigate(R.id.action_createAccount_to_fragmentLogin)
        }

        //case have account click listener to login fragment
        binding.haveAccountTextView.setOnClickListener {
            findNavController().navigate(R.id.action_createAccount_to_fragmentLogin)
        }
    }

    //function to set view model on the data binding
    private fun setViewModel() {
        binding.viewmodel = registerViewModel
    }

    //function to observe changer on the register status
    private fun setObserver() {
        registerViewModel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
        }
    }

    private fun handleUiStatus(status: RegisterUiStatus) {
        when (status) {
            //case error show An error has occurred
            is RegisterUiStatus.Error -> {
                Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show()
            }
            //case errorWithMessage show status message
            is RegisterUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            //case success clear status and data then go to login fragment
            is RegisterUiStatus.Success -> {
                registerViewModel.clearStatus()
                registerViewModel.clearData()
                findNavController().navigate(R.id.action_createAccount_to_fragmentLogin)
                Log.d("APP REGISTER", "Succeeeeeeeeeeeeeeeees")
            }

            else -> {}
        }
    }

    private fun validate() {

        val name = binding.nameTextField.text.toString().trim()
        val email = binding.outlinedTextFieldEmail.text.toString().trim()
        val password = binding.outlinedTextFieldPassword.text.toString().trim()

        if (name.isBlank()) {
            binding.nameTextField.error = "Este campo es obligatorio"
        }
        if (email.isBlank()) {
            binding.outlinedTextFieldEmail.error = "Este campo es obligatorio"
        }
        if (password.isBlank()) {
            binding.outlinedTextFieldPassword.error = "Este campo es obligatorio"
        }
    }

    private fun showPassword() {
        binding.passwordHideImageView.setOnClickListener {
            if (binding.outlinedTextFieldPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                // hiding password
                binding.outlinedTextFieldPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.passwordHideImageView.setImageResource(R.drawable.eye_closed_icon)
            } else  {
                // showing password
                binding.outlinedTextFieldPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.passwordHideImageView.setImageResource(R.drawable.eye_icon)
            }
        }

        binding.confirmPasswordHideImageView.setOnClickListener {
            if (binding.confirmPasswordEditText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                // hiding password
                binding.confirmPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.confirmPasswordHideImageView.setImageResource(R.drawable.eye_closed_icon)
            } else  {
                // showing password
                binding.confirmPasswordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.confirmPasswordHideImageView.setImageResource(R.drawable.eye_icon)
            }
        }
    }

}