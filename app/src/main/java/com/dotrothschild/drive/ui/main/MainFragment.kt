package com.dotrothschild.drive.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dotrothschild.drive.DriveApp
import com.dotrothschild.drive.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private  val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            (activity?.application as DriveApp).database.driveDao()
        )
    }
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // the onClick event added
        val listPlaceAdapter = ListPlaceAdapter {
            val action = MainFragmentDirections
                .actionNavigationMainToNavigationFeedback(
                    placeId = it.id
                )
            view.findNavController().navigate(action)
        }

        recyclerView.adapter = listPlaceAdapter
        lifecycle.coroutineScope.launch{
            mainViewModel.allPlaces().collect {
                listPlaceAdapter.submitList(it)
                if (it.isEmpty()) {
                    _binding!!.noPlacesToShow.text  = "No places to display."
                } else {
                    _binding!!.noPlacesToShow.text  = "Total places: ${listPlaceAdapter.itemCount}"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}