package com.dotrothschild.drive.ui.feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dotrothschild.drive.DriveApp
import com.dotrothschild.drive.databinding.FragmentFeedbackBinding
import kotlinx.coroutines.launch


class FeedbackFragment : Fragment() {

    private  val feedbackViewModel: FeedbackViewModel by activityViewModels {
        FeedbackViewModel.FeedbackViewModelFactory(
            (activity?.application as DriveApp).database.driveDao()
        )
    }
    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val listFeedbackAdapter = ListFeedbackAdapter {

        }
        recyclerView.adapter = listFeedbackAdapter
        lifecycle.coroutineScope.launch{
            feedbackViewModel.allFeedback().collect() {
                listFeedbackAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}