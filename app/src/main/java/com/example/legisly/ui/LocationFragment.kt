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
import com.example.legisly.databinding.FragmentLocationBinding

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userPreferences = UserPreferences(requireContext())
        binding.zipCodeInput.setText(userPreferences.getZipCode())

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.currentLocationButton.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "GPS will be connected after the ZIP-code flow is working.",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.continueButton.setOnClickListener {
            val zip = binding.zipCodeInput.text?.toString()?.trim().orEmpty()

            if (!zip.matches(Regex("^\\d{5}$"))) {
                binding.zipCodeInputLayout.error = "Enter a valid 5-digit ZIP code"
                return@setOnClickListener
            }

            binding.zipCodeInputLayout.error = null
            userPreferences.saveZipCode(zip)

            // Temporary display label only. This is not a verified ZIP lookup.
            val region = if (zip == "08901") {
                "New Brunswick, New Jersey"
            } else {
                "ZIP code $zip"
            }
            userPreferences.saveRegionName(region)

            findNavController().navigate(R.id.action_location_to_interests)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
