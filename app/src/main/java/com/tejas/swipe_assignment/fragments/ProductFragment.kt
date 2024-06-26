package com.tejas.swipe_assignment.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tejas.swipe_assignment.util.NetworkConnectionLiveData
import com.tejas.swipe_assignment.ui.ProductAdapter
import com.tejas.swipe_assignment.R
import com.tejas.swipe_assignment.databinding.FragmentProductBinding
import com.tejas.swipe_assignment.ui.ProductViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductFragment : Fragment() {
    private val productAdapter: ProductAdapter by inject()
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModel<ProductViewModel>()
    private val networkConnectionLiveData: NetworkConnectionLiveData by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpTextWatcher()
        networkConnectionLiveData.observe(viewLifecycleOwner, Observer {
            if(it) binding.internetStateIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.internet_ok))
            else binding.internetStateIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_internet_disconnected))
        })
        viewModel.productScreenState.observe(viewLifecycleOwner, Observer {
            if(it.isLoading){
                showProgressBar()
            }
            else if(it.error!=null){
                hideProgressBar()
                showSnackbar(it.error!!)
            }
            else{
                hideProgressBar()
                productAdapter.setData(ArrayList(it.data))
            }
        })
        binding.addProductFab.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val bottomSheet = ProductListDialogFragment()
            fragmentManager.let { bottomSheet.show(it, "bottom_sheet") }
        }
    }

    private fun showSnackbar(message: String){
        Snackbar.make(requireView(), message, 2000).show()
    }

    private fun hideProgressBar(){
        binding.progessBar.visibility = View.GONE
        binding.productRcv.visibility = View.VISIBLE
    }
    private fun showProgressBar(){
        binding.productRcv.visibility = View.GONE
        binding.progessBar.visibility = View.VISIBLE
    }
    private fun setUpTextWatcher() {
        binding.searchEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.search(s.toString())
            }
        })
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setUpRecyclerView() {
        binding.productRcv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }
    }
}