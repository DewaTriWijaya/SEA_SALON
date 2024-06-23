package com.dewa.sea.ui.notification.reservation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.data.Repository
import com.dewa.sea.data.model.DataReservation
import com.dewa.sea.databinding.FragmentReservationBinding
import com.dewa.sea.ui.ViewModelFactory
import com.dewa.sea.utils.SharedPreferences

class ReservationFragment : Fragment(), AdapterReservation.OnTimeSelectedListener {

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
            adapterReservation.submitList(services)
        }
        val uid = pref.getUid().toString()
        reservationViewModel.fetchServices(uid)
    }

    private fun recyclerviewReservation() {
        adapterReservation = AdapterReservation(this)
        binding.rvReservation.layoutManager = LinearLayoutManager(context)
        binding.rvReservation.adapter = adapterReservation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTimeSelected(id: String) {
        reservationViewModel.deleteReservation(id)
    }

    private fun deleteReservationFromFireStore() {
        reservationViewModel.deleteResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Reservation deleted successfully", Toast.LENGTH_SHORT).show()
                getDataReservationFromFireStore()
            }
            result.onFailure { exception ->
                Toast.makeText(
                    requireContext(),
                    "Failed to delete reservation: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}