package com.dotrothschild.drive.ui.feedback

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView 
import com.dotrothschild.drive.database.models.Feedback
import com.dotrothschild.drive.databinding.ListFeebackItemBinding

class ListFeedbackAdapter(
    private val onItemClicked: (Feedback) -> Unit
) : ListAdapter<Feedback, ListFeedbackAdapter.ListFeedbackViewHolder>(DiffCallback)
{
    companion object {
    private val DiffCallback = object : DiffUtil.ItemCallback<Feedback>() {
        override fun areItemsTheSame(
            oldItem: Feedback,
            newItem: Feedback
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Feedback,
            newItem: Feedback
        ): Boolean {
            return oldItem == newItem
        }
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFeedbackViewHolder {
        val viewHolder = ListFeedbackViewHolder(
            ListFeebackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ListFeedbackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListFeedbackViewHolder(
        private var binding: ListFeebackItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(feedback: Feedback) {
            binding.textViewFeedbackTitle.text = feedback.user_name
            binding.textViewRDescription.text = feedback.comment
            binding.textViewFeedbackSubTitle1.text = feedback.place_id.toString()

        }
    }
}