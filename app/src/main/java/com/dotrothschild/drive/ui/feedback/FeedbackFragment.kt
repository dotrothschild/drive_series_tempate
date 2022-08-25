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

    private var placeId: Int = 0 // cannot use late init on primitive.

    private  val feedbackViewModel: FeedbackViewModel by activityViewModels {
        FeedbackViewModel.FeedbackViewModelFactory(
            (activity?.application as DriveApp).database.driveDao()
        )
    }
    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            placeId = it.getInt("place_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val listFeedbackAdapter = ListFeedbackAdapter {
        }
        recyclerView.adapter = listFeedbackAdapter
        lifecycle.coroutineScope.launch{
            feedbackViewModel.feedbackByPlace(placeId).collect {
                listFeedbackAdapter.submitList(it)
                if (it.isEmpty()) {
                    _binding!!.noFeedbackRecordsToShow.text  = "No feedback records on this place."
                } else {
                    _binding!!.noFeedbackRecordsToShow.text  = "Total feedback: ${listFeedbackAdapter.itemCount}"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}