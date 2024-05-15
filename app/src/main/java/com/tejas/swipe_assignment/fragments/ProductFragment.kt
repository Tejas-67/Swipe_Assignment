package com.tejas.swipe_assignment.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tejas.swipe_assignment.ProductAdapter
import com.tejas.swipe_assignment.R
import com.tejas.swipe_assignment.Resource
import com.tejas.swipe_assignment.databinding.FragmentProductBinding
import com.tejas.swipe_assignment.repositories.ProductRepository
import com.tejas.swipe_assignment.room.ProductDatabase
import com.tejas.swipe_assignment.ui.ProductViewModel

class ProductFragment : Fragment() {
    private lateinit var productAdapter: ProductAdapter
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProductViewModel

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
        viewModel = ProductViewModel(
            ProductRepository(
                ProductDatabase.getDatabase(requireContext())
            )
        )
        viewModel.productScreenState.observe(viewLifecycleOwner, Observer {
            if(it.isLoading){
                showToast("Loading")
            }
            else if(it.error!=null){
                showToast(it.error!!)
            }
            else{
                productAdapter.setData(ArrayList(it.data))
            }
        })

        binding.fab.setOnClickListener {
            viewModel.getProducts(fetchFromRemote = true)
        }
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setUpRecyclerView() {
        productAdapter = ProductAdapter()
        binding.productRcv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }
    }
}