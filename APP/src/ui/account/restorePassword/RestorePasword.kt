package com.mcmp2023.s.ui.account.restorePassword

import android.os.Bundle
import android.text.InputType
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
import com.mcmp2023.s.databinding.FragmentRestorePaswordBinding
import com.mcmp2023.s.ui.account.forgotPassword.ForgotPasswordUiStatus
import com.mcmp2023.s.ui.account.forgotPassword.viewmodel.ForgotPasswordViewmodel
import com.mcmp2023.s.ui.account.restorePassword.viewmodel.RestorePasswordViewmodel


class restorePasword : Fragment() {

    private lateinit var binding: FragmentRestorePaswordBinding

    val app by lazy {
        requireActivity().application as ProductApplication
    }

    private val restorePasswordViewmodel: RestorePasswordViewmodel by activityViewModels {
        RestorePasswordViewmodel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestorePaswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateData()
        showPassword()
        setViewmodel()
        observeStatus()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.otheractivitys)
        }

        binding.actionBacktologin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setViewmodel() {
        binding.viewmodel = restorePasswordViewmodel
    }

    private fun observeStatus() {
        restorePasswordViewmodel.status.observe(viewLifecycleOwner) { status ->
            handleUiStatus(status)
        }
    }

    private fun handleUiStatus(status: RestorePasswordUiStatus) { //different login status
        when (status) {
            //case error show An error has occurred
            is RestorePasswordUiStatus.Error -> {
                Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show()
            }
            //case errorWithMessage show status message
            is RestorePasswordUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            //case success clear status and data the save token and pass to foryoufragment
            is RestorePasswordUiStatus.Success -> {
                cleanApplication()
                restorePasswordViewmodel.clearStatus()
                restorePasswordViewmodel.clearData()
                Toast.makeText(
                    requireContext(),
                    "Se ha actualizado su contrasena exitosamente",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_restorePasword_to_fragmentLogin)
            }

            else -> {}
        }
    }

    private fun validateData() {
        val email = binding.TextFieldRestoreEmail.text.toString().trim()
        val codigo = binding.TextFieldCodeVerify.text.toString().trim()
        val newPassword = binding.TextFieldNewPass.text.toString().trim()
        val restorePassword = binding.TextFieldConfirmPass.text.toString().trim()

        if (email.isBlank()) {
            binding.TextFieldRestoreEmail.error = "Este campo es obligatorio"
        }
        if (codigo.isBlank()) {
            binding.TextFieldCodeVerify.error = "Este campo es obligatorio"
        }
        if (newPassword.isBlank()) {
            binding.TextFieldNewPass.error = "Este campo es obligatorio"
        }
        if (restorePassword.isBlank()) {
            binding.TextFieldConfirmPass.error = "Este campo es obligatorio"
        }
    }


    private fun showPassword() {
        binding.newPasswordHideImageView.setOnClickListener {
            if (binding.TextFieldNewPass.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                // hiding password
                binding.TextFieldNewPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.newPasswordHideImageView.setImageResource(R.drawable.eye_closed_icon)
            } else {
                // showing password
                binding.TextFieldNewPass.inputType =
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.newPasswordHideImageView.setImageResource(R.drawable.eye_icon)
            }
        }
        binding.ConfirmNewPasswordHideImageView.setOnClickListener {
            if (binding.TextFieldConfirmPass.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                binding.TextFieldConfirmPass.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.ConfirmNewPasswordHideImageView.setImageResource(R.drawable.eye_closed_icon)
            } else {
                binding.TextFieldConfirmPass.inputType =
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.ConfirmNewPasswordHideImageView.setImageResource(R.drawable.eye_icon)
            }
        }
    }

    private fun cleanApplication() {
        app.saveUserName("")
        app.saveUserRole("")
        app.saveAuthToken("")
    }

}