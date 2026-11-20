package com.example.legisly.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.legisly.R
import com.example.legisly.adapters.InterestAdapter
import com.example.legisly.data.UserPreferences
import com.example.legisly.databinding.FragmentInterestsBinding
import com.example.legisly.models.Interest

class InterestsFragment : Fragment() {

    private var _binding: FragmentInterestsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: InterestAdapter
    private lateinit var userPreferences: UserPreferences


    private val interestList = mutableListOf(
        Interest("healthcare", "Healthcare", "Health services and insurance"),
        Interest("education", "Education", "Schools and higher education"),
        Interest("housing", "Housing", "Rent and affordable housing"),
        Interest("taxes", "Taxes", "State and local taxes"),
        Interest("transportation", "Transportation", "Roads and public transit"),
        Interest("environment", "Environment", "Climate and energy"),
        Interest("public_safety", "Public Safety", "Police and emergency services"),
        Interest("economy", "Economy", "Jobs and business")
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())

        val previouslySelected = userPreferences.getInterests()
        interestList.forEach { interest ->
            interest.isSelected = previouslySelected.contains(interest.id)
        }

        adapter = InterestAdapter(interestList) {
            updateSelectedCount()
        }

        binding.interestsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.interestsRecyclerView.adapter = adapter
        updateSelectedCount()

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.continueButton.setOnClickListener {
            val selectedIds = adapter.getSelectedInterestIds()
            if (selectedIds.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Select at least one topic to continue.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            userPreferences.saveInterests(selectedIds.toList())
            findNavController().navigate(R.id.action_interests_to_complete)
        }
    }

    private fun updateSelectedCount() {
        val count = adapter.getSelectedInterestIds().size
        binding.selectedCountText.text = when (count) {
            0 -> "Select the topics you care about"
            1 -> "1 topic selected"
            else -> "$count topics selected"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
