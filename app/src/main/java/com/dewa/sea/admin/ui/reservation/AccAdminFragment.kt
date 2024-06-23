package com.dewa.sea.admin.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dewa.sea.admin.ui.AdapterReservationAdmin
import com.dewa.sea.data.Repository
import com.dewa.sea.databinding.FragmentAdminAccBinding
import com.dewa.sea.ui.ViewModelFactory

class AccAdminFragment : Fragment(), AdapterReservationAdmin.OnTimeSelectedListener  {

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

        binding.rvReservation.layoutManager = LinearLayoutManager(requireContext())

        val adapter = AdapterReservationAdmin(this)
        binding.rvReservation.adapter = adapter
        accViewModel.services.observe(viewLifecycleOwner) { services ->
            adapter.submitList(services)
        }
        accViewModel.fetchServices()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTimeSelected(id: String) {
        TODO("Not yet implemented")
    }
}