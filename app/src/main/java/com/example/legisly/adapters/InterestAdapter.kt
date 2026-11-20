
package com.example.legisly.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.legisly.databinding.ItemInterestBinding
import com.example.legisly.models.Interest

/**
 * RecyclerView adapter for legislative topics.
 * The Fragment owns the selected interests and saves preferences.
 */
class InterestAdapter(
    private val interests: MutableList<Interest>,
    private val onInterestToggled: (Interest) -> Unit
) : RecyclerView.Adapter<InterestAdapter.InterestViewHolder>() {

    inner class InterestViewHolder(
        private val binding: ItemInterestBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(interest: Interest) {
            binding.interestTitle.text = interest.title
            binding.interestDescription.text = interest.description
            binding.selectedIcon.text =
                if (interest.isSelected) "✓" else ""

            binding.interestCard.isChecked = interest.isSelected

            binding.interestCard.setOnClickListener {
                val position = bindingAdapterPosition

                if (position != RecyclerView.NO_POSITION) {
                    interest.isSelected = !interest.isSelected
                    notifyItemChanged(position)
                    onInterestToggled(interest)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InterestViewHolder {
        val binding = ItemInterestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InterestViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: InterestViewHolder,
        position: Int
    ) {
        holder.bind(interests[position])
    }

    override fun getItemCount(): Int = interests.size

    fun getSelectedInterestIds(): Set<String> {
        return interests
            .filter { it.isSelected }
            .map { it.id }
            .toSet()
    }
}
