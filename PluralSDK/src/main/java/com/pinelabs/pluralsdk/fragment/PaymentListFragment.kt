package com.pinelabs.pluralsdk.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.pinelabs.pluralsdk.PaymentModesViewModel
import com.pinelabs.pluralsdk.R
import com.pinelabs.pluralsdk.adapter.DividerItemDecorator
import com.pinelabs.pluralsdk.adapter.PaymentOptionsAdapter
import com.pinelabs.pluralsdk.api.RecyclerViewPaymentOptionData

class PaymentListFragment : Fragment(), PaymentOptionsAdapter.OnItemClickListener {

    private lateinit var recyclerPaymentOptions: RecyclerView
    private lateinit var shimmerLayout: ShimmerFrameLayout

    private lateinit var viewModel: PaymentModesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(PaymentModesViewModel::class.java)

        recyclerPaymentOptions = view.findViewById(R.id.recycler_payment_options)
        shimmerLayout = view.findViewById(R.id.shimmerFrameLayout)
        shimmerLayout.startShimmer()
        viewModel.selectedItem.observe(viewLifecycleOwner, Observer { data->
            shimmerLayout.stopShimmer()
            shimmerLayout.isVisible = false
            recyclerPaymentOptions.isVisible = true
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val myRecyclerViewAdapter = PaymentOptionsAdapter(data, this)
            recyclerPaymentOptions.adapter = myRecyclerViewAdapter
            recyclerPaymentOptions.layoutManager = layoutManager
            val dividerItemDecoration: RecyclerView.ItemDecoration = DividerItemDecorator(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
            recyclerPaymentOptions.addItemDecoration(dividerItemDecoration)
            myRecyclerViewAdapter.notifyDataSetChanged()
        })
    }

    override fun onItemClick(item: RecyclerViewPaymentOptionData?) {
        Toast.makeText(activity, item!!.payment_option, Toast.LENGTH_SHORT).show()
        loadFragment()
    }

    fun loadFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.details_fragment, CardFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

}