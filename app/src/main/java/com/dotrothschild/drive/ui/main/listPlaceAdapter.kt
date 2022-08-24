package com.dotrothschild.drive.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dotrothschild.drive.database.models.Place
import com.dotrothschild.drive.databinding.ListPlaceItemBinding


class ListPlaceAdapter (
    private val onItemClicked: (Place) -> Unit
) : ListAdapter<Place, ListPlaceAdapter.ListPlaceViewHolder>(DiffCallback)
{
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(
                oldItem: Place,
                newItem: Place
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Place,
                newItem: Place
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPlaceViewHolder {
        val viewHolder = ListPlaceViewHolder(
            ListPlaceItemBinding.inflate(
                LayoutInflater.from( parent.context),
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

    override fun onBindViewHolder(holder: ListPlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListPlaceViewHolder(
        private var binding: ListPlaceItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(place: Place) {
            binding.textViewPlaceTitle.text = place.title
            binding.textViewRDescription.text = place.description
            binding.textViewPlaceSubTitle1.text = place.address + ", " + place.city + ", " + place.region +", " + place.state + ", " + place.country
            binding.textViewPlaceSubTitle2.text = place.keywords
            binding.textViewPlaceSubTitle3.text = place.place_number
            binding.textViewPlaceSubTitle4.text = place.x.toString() + ", " + place.y.toString()
        }
    }
}