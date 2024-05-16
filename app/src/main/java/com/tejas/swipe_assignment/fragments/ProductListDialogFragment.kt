package com.tejas.swipe_assignment.fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.tejas.swipe_assignment.R
import com.tejas.swipe_assignment.databinding.FragmentItemListDialogListDialogBinding
import com.tejas.swipe_assignment.repositories.ProductRepository
import com.tejas.swipe_assignment.room.ProductDatabase
import com.tejas.swipe_assignment.ui.ProductViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.lang.NumberFormatException

class ProductListDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentItemListDialogListDialogBinding? = null
    private val binding get() = _binding!!

    private val IMAGE_REQUEST_CODE = 100
    private val image: MutableLiveData<Uri> = MutableLiveData()
    private val viewModel: ProductViewModel by viewModel<ProductViewModel>()

    private var selectedType: String? = null

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = resources.displayMetrics.heightPixels
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDropdown()
        addTextWatcherToTaxEditText()
        binding.addImageFab.setOnClickListener {
            openGallery()
        }
        image.observe(viewLifecycleOwner, Observer{
            binding.image.setImageURI(it)
        })

        binding.addBtn.setOnClickListener {
            uploadData()
        }
        viewModel.addProductResponse.observe(viewLifecycleOwner, Observer {
            Log.w("add-Product", "here: $it")
            if(it.first){
                showSnackbar("Success ${it.second}")
            }else{
                showSnackbar("Failed")
            }
        })
    }

    private fun showSnackbar(message: String){
        Snackbar.make(binding.addBtn, message, 2000).show()
    }
    private fun uploadData() {
        val productName = binding.productNameEdit.text.toString()
        var allGood = true
        if(productName.isEmpty()){
            binding.productNameEdit.error = "Enter product Name"
            allGood = false
        }
        if(selectedType.isNullOrEmpty()){
            binding.productTypeInput.error = "Select product type"
            allGood = false
        }
        val tax = binding.productTaxEdit.text.toString()
        if(tax.isEmpty()){
            binding.productTaxInput.error = "Tax field cannot be empty"
            allGood = false
        }
        val amount = binding.priceEdit.text.toString()
        if(amount.isEmpty()){
            binding.priceInput.error = "Amount cannot be empty"
            allGood = false
        }
        if(!allGood) return

        startUpload(
            productName = productName,
            tax = tax,
            amount = amount
        )
    }

    private fun startUpload(
        productName: String,
        tax: String,
        amount: String
    ){
        //image null not handled
        val file = uriToFile(image.value!!)
        viewModel.addProduct(name = productName, tax = tax, price = amount, type = selectedType!!, file = file)
    }

    private fun addTextWatcherToTaxEditText() {
        binding.productTaxEdit.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let{
                    val input = s.toString()
                    if(input.isNotEmpty()){
                        try{
                            val value = input.toDouble()
                            if(value>100.0){
                                binding.productTaxEdit.error = "Tax cannot be more than 100%."
                            }else{
                                binding.productTaxEdit.error = null
                            }
                        }catch(e: NumberFormatException){
                            binding.productTaxEdit.error = "Invalid input"
                        }
                    }else{
                        binding.productTaxEdit.error = null
                    }
                }
            }
        })
    }


    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            data.data?.let{
                image.postValue(it)
            }
        }
    }
    private fun uriToFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return if (filePath != null) File(filePath) else null
    }

    private fun setupDropdown() {
        val types = resources.getStringArray(R.array.product_types)
        val typeAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, types)
        binding.productTypeDropdown.setAdapter(typeAdapter)
        binding.productTypeDropdown.setOnItemClickListener { _, _, position, _ ->
            selectedType = types[position]
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}