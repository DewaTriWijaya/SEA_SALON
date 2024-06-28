package com.dewa.sea.ui.notification.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.data.Repository
import com.dewa.sea.databinding.FragmentReservationBinding
import com.dewa.sea.data.ViewModelFactory
import com.dewa.sea.databinding.DialogReviewBinding
import com.dewa.sea.utils.SharedPreferences

class ReservationFragment(private val status: String) : Fragment(),
    AdapterReservation.OnSelectedListener {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterReservation: AdapterReservation
    private lateinit var pref: SharedPreferences

    private val reservationViewModel: ReservationViewModel by viewModels {
        ViewModelFactory(Repository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPreferences(requireContext())

        getDataReservationFromFireStore()
        recyclerviewReservation()
        deleteReservationFromFireStore()
    }

    private fun getDataReservationFromFireStore() {
        reservationViewModel.services.observe(viewLifecycleOwner) { services ->
            services?.let { data ->
                adapterReservation.submitList(data.filter { it.statusData == status })
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
        val uid = pref.getUid().toString()
        reservationViewModel.fetchServices(uid)

        binding.swipeRefreshLayout.setOnRefreshListener {
            reservationViewModel.fetchServices(uid)
            recyclerviewReservation()
        }
    }

    private fun recyclerviewReservation() {
        adapterReservation = AdapterReservation(this, reservationViewModel)
        binding.rvReservation.layoutManager = LinearLayoutManager(context)
        binding.rvReservation.adapter = adapterReservation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSelected(id: String, service: String, date: String) {
        when (status) {
            "reservation" -> {
                reservationViewModel.deleteReservation(id)
            }

            "proses" -> {
                Toast.makeText(requireContext(), "Proses", Toast.LENGTH_SHORT).show()
            }

            "done" -> {
                reservationViewModel.checkIfReviewed(id) { isReviewed ->
                    if (isReviewed) {
                        Toast.makeText(requireContext(), "This reservation has already been reviewed.", Toast.LENGTH_SHORT).show()
                    } else {
                        reviewDialog(id, service, date)
                        recyclerviewReservation()
                    }
                }
            }
        }

    }

    private fun reviewDialog(id: String, service: String, date: String) {
        val dialogBinding = DialogReviewBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        
        dialogBinding.apply {
            btnSave.setOnClickListener {
                val rating = rating.rating.toString()
                val review = tvReview.text.toString()
                val name = pref.getName().toString()

                if (review.isNotEmpty()) {
                    reservationViewModel.addReview(id, name, service, date, rating, review) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Review added successfully", Toast.LENGTH_SHORT).show()
                            builder.dismiss()
                        } else {
                            Toast.makeText(requireContext(), "Failed to add review", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialogBinding.btnClose.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    private fun deleteReservationFromFireStore() {
        reservationViewModel.deleteResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Reservation deleted successfully", Toast.LENGTH_SHORT).show()
                getDataReservationFromFireStore()
            }
            result.onFailure { exception ->
                Toast.makeText(requireContext(), "Failed to delete reservation: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}