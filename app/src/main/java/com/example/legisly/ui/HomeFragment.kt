
package com.example.legisly.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.legisly.R
import com.example.legisly.data.UserPreferences
import com.example.legisly.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())

        loadUserInformation()
        setupListeners()
    }

    private fun loadUserInformation() {
        val zipCode = userPreferences.getZipCode()

        binding.homeLocationText.text =
            if (zipCode.isNotBlank()) {
                "ZIP Code: $zipCode"
            } else {
                "Set your location"
            }
    }

    private fun setupListeners() {
        binding.changeLocationButton.setOnClickListener {

            val bundle = Bundle().apply {
                putBoolean("isEditMode", true)
            }

            findNavController().navigate(R.id.action_completion_to_home)
        }

        binding.viewAllBillsButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_bills)
        }

        binding.recentBillCard.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_bills)
        }

        binding.adjustPreferencesButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Preference settings coming soon!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.profileButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Profile coming soon!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.houseRepresentativeCard.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Representatives coming soon!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.senateRepresentativeCard.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Senators coming soon!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
