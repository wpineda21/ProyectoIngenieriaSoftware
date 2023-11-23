package com.mcmp2023.s.ui.for_you.product.descriptionproduct

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mcmp2023.s.R
import com.mcmp2023.s.databinding.FragmentProductDescriptionBinding
import com.mcmp2023.s.ui.for_you.product.descriptionproduct.viewmodel.DescriptionViewModel
import com.mcmp2023.s.ui.for_you.product.recyclerview_product.viewmodel.ProductRecyclerViewModel

class ProductDescriptionFragment : Fragment() {

    private lateinit var binding: FragmentProductDescriptionBinding

    private val viewModel : DescriptionViewModel by activityViewModels()

    private val productViewModel : ProductRecyclerViewModel by activityViewModels {
        ProductRecyclerViewModel.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
        }
        setViewModel()
        renderImage(viewModel.imageUrl, view )
        redirectWhatsApp(viewModel.phoneNumber, viewModel.title)

        binding.arrowBackIcon.setOnClickListener{
            findNavController().popBackStack()
        }
    }
    private fun setViewModel() {
        binding.viewmodel = viewModel
    }


    private fun renderImage(url: String, view: View) {
        val imageName = url.substringAfterLast("/")

        Glide
            .with(view)
            .load("https://sybapimarketplace.shop/uploads//${imageName}")
            .error(R.drawable.no_image_icon)
            .into(binding.productImage)
    }


    private fun redirectWhatsApp(phoneNumberLiveData: MutableLiveData<String>, titleLiveData: MutableLiveData<String>) {
        binding.whatsappButton.setOnClickListener {
            val phoneNumber = phoneNumberLiveData.value
            val title = titleLiveData.value
            if (!phoneNumber.isNullOrEmpty()) {
                val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=Hola,%20sigue%20disponible%20el%20producto%20$title%20de%20SBMarketplace")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
    }
}