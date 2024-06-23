package com.dewa.sea.admin.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.admin.ui.AdapterReservationAdmin
import com.dewa.sea.data.Repository
import com.dewa.sea.databinding.FragmentAdminAccBinding
import com.dewa.sea.data.ViewModelFactory

class AccAdminFragment(private val status: String) : Fragment(), AdapterReservationAdmin.OnTimeSelectedListener  {

    private var _binding: FragmentAdminAccBinding? = null
    private val binding get() = _binding!!
    private val accViewModel: AccAdminViewModel by viewModels {
        ViewModelFactory(Repository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAccBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        data()
    }

    private fun data(){
        binding.rvReservation.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AdapterReservationAdmin(this)
        binding.rvReservation.adapter = adapter
        accViewModel.services.observe(viewLifecycleOwner) { services ->
            services?.let { data ->
                adapter.submitList(data.filter { it.statusData == status })
            }
        }
        accViewModel.fetchServices()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTimeSelected(id: String) {
        when (status) {
            "reservation" -> {
                accViewModel.updateReservationStatus(id, "proses") { success ->
                    if (success) {
                        data()
                        Toast.makeText(binding.root.context, "Status updated to proses", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(binding.root.context, "Failed to update status", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            "proses" -> {
                accViewModel.updateReservationStatus(id, "done") { success ->
                    if (success) {
                        data()
                        Toast.makeText(binding.root.context, "Status updated to done", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(binding.root.context, "Failed to update status", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            "done" -> {
                data()
                Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show()
            }
        }
    }
}