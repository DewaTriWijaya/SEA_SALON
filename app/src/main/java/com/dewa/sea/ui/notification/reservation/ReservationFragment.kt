package com.dewa.sea.ui.notification.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.data.model.DataReservation
import com.dewa.sea.databinding.FragmentReservationBinding
import com.google.firebase.firestore.FirebaseFirestore

class ReservationFragment : Fragment(), AdapterReservation.OnTimeSelectedListener {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!
    private val fireStore = FirebaseFirestore.getInstance()
    private val reservation = mutableListOf<DataReservation>()
    private lateinit var adapterReservation: AdapterReservation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataReservationFromFireStore()
        recyclerviewReservation()
    }

    private fun getDataReservationFromFireStore() {
        fireStore.collection("reservation")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val id = document.id
                    val name = document.getString("name")
                    val phone = document.getString("phone")
                    val service = document.getString("service")
                    val date = document.getString("date")
                    val time = document.getString("time")
                    val barcode = document.getString("barcode")
                    val status = document.getString("status")

                    reservation.add(
                        DataReservation(
                            id,
                            name.toString(),
                            phone.toString(),
                            service.toString(),
                            date.toString(),
                            time.toString(),
                            barcode.toString(),
                            status.toString()
                        )
                    )
                }
                adapterReservation.submitList(reservation)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error getting documents: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun recyclerviewReservation(){
        adapterReservation = AdapterReservation(this)
        binding.rvReservation.layoutManager = LinearLayoutManager(context)
        binding.rvReservation.adapter = adapterReservation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTimeSelected(id: String) {
        deleteReservationFromFireStore(id)
    }

    private fun deleteReservationFromFireStore(reservation: String) {
        //binding.progressBar.visibility = View.VISIBLE

        fireStore.collection("reservation")
            .document(reservation)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Reservation deleted successfully", Toast.LENGTH_SHORT).show()
                getDataReservationFromFireStore()
                //binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Error deleting reservation: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}