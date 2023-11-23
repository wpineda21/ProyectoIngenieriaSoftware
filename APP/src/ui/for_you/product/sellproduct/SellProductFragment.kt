package com.mcmp2023.s.ui.for_you.product.sellproduct

import android.Manifest
import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.data.db.models.Category
import androidx.core.content.ContextCompat
import com.mcmp2023.s.databinding.FragmentSellProductBinding
import com.mcmp2023.s.ui.for_you.categories.viewmodel.CategoriesViewModel
import com.mcmp2023.s.ui.for_you.product.sellproduct.viewmodel.SellProductViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SellProductFragment : Fragment() {

    private val REQUEST_IMAGE_CAPTURE = 1

    private lateinit var spinner: Spinner

    private lateinit var binding: FragmentSellProductBinding

    private val categoryViewModel : CategoriesViewModel by activityViewModels {
        CategoriesViewModel.Factory
    }

    private val sellProductViewmodel: SellProductViewmodel by activityViewModels {
        SellProductViewmodel.Factory
    }

    val app by lazy {
        requireActivity().application as ProductApplication
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted ->
            val message = if(isGranted) "Permission Granted" else "Permission Rejected"
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            if(isGranted){
                app.savePermissionStatus(true)
            }else {
                app.savePermissionStatus(false)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        sellProductViewmodel.token.value = app.getToken()
        //find Spinner
        spinner = binding.categorySpinnerView
        val opciones = listOf("Opción 1", "Opción 2", "Opción 3")

        //spinner
        CoroutineScope(Dispatchers.Main).launch {
            getCategoryAndLaunchSpinner()
        }

        //viewmodel
        setViewModel()

        //status
        observeStatus()

        //
        addListenners()

        validateData()


    }



    private fun setViewModel() {
        binding.viewmodel = sellProductViewmodel
    }

    private fun observeStatus(){
        sellProductViewmodel.status.observe(viewLifecycleOwner){status ->
            handleUiStatus(status)
        }
    }

    private fun handleUiStatus(status: SellProductUiStatus) { //different login status
        when (status) {
            //case error show An error has occurred
            is SellProductUiStatus.Error -> {
                Toast.makeText(requireContext(), "An error has occurred", Toast.LENGTH_SHORT).show()
            }
            //case errorWithMessage show status message
            is SellProductUiStatus.ErrorWithMessage -> {
                Toast.makeText(requireContext(), status.message, Toast.LENGTH_SHORT).show()
            }
            //case success clear status and data the save token and pass to foryoufragment
            is SellProductUiStatus.Success -> {
                sellProductViewmodel.clearStatus()
                sellProductViewmodel.clearData()
                Toast.makeText(requireContext(), "Se ha publicado exitosamente su producto", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            else -> {}
        }
    }

    private fun addListenners(){
        binding.addImageCard.setOnClickListener{
            if(app.getPermissionStatus()){
                dispatchTakePictureIntent()
            }
            else{
                Toast.makeText(requireContext(), "No tienes pemiso para realizar esta acction", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val imageBitmap = data.extras?.get("data") as? Bitmap

            if (imageBitmap != null) {
                sellProductViewmodel.setBitmap(imageBitmap)
                binding.imageForProduct?.setImageBitmap(imageBitmap)
            } else {
                Toast.makeText(requireContext(), "No has seleccionado ninguna imagen para subir", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "No has seleccionado una imagen", Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun getCategoryAndLaunchSpinner() {
        setCategorySpinner(categoryViewModel.getCategories())
    }

    private fun setCategorySpinner(categoryList: List<Category>){
        val categoryAdapter = ArrayAdapter(requireContext(),  R.layout.simple_spinner_item, categoryList.map { it.name })
        binding.categorySpinnerView.adapter = categoryAdapter

        binding.categorySpinnerView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = categoryList[position]
                sellProductViewmodel.category.value = selectedCategory.name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

    }


    fun validateData(){
        val tittle = binding.titleEditTextView.text.toString().trim()
        val description = binding.descriptionEditTextView.text.toString().trim()
        val phoneNumber = binding.statusEditTextView.text.toString().trim()
        val priceText = binding.priceEditTextView.text.toString().trim()
        val price: Float

        if (priceText.isNotEmpty()) {
            price = priceText.toFloat()
            if(price <= 0.0){
                binding.priceEditTextView.error = "Este campo no debe ser menor o igual a cero"
            }
        } else {
            // Manejar el caso de que el precio sea una cadena vacía
        }

        if(tittle.isBlank()){
            binding.titleEditTextView.error = "Este campo no debe ir vacio"
        }
        if(description.isBlank()){
            binding.descriptionEditTextView.error = "Este campo no debe ir vacio"
        }
        if(phoneNumber.isBlank()){
            binding.statusEditTextView.error = "Este campo no debe ir vacio"
        }
        if (priceText.isBlank()){
            binding.priceEditTextView.error = "Este campo no debe ir vacio"
        }

    }
}