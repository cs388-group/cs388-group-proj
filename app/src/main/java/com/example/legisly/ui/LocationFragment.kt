
package com.example.legisly.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.legisly.R
import com.example.legisly.data.UserPreferences
import com.example.legisly.databinding.FragmentLocationBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences

    private var isEditMode = false

    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val granted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
                        permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

            if (granted) {
                getCurrentLocation()
            } else {
                showToast("Location permission denied. Enter your ZIP code manually.")
            }
        }

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

        isEditMode = arguments?.getBoolean("isEditMode") ?: false
        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        binding.zipCodeInput.setText(userPreferences.getZipCode())

        if (isEditMode) {
            binding.stepLabel.text = "LOCATION SETTINGS"
            binding.locationTitle.text = "Change your location."
            binding.locationSubtitle.text =
                "Update your ZIP code to see legislation affecting your community."
            binding.continueButton.text = "Save Location"
        } else {
            binding.stepLabel.text = "SETUP 1 OF 2"
            binding.locationTitle.text = "Start with where you live."
            binding.locationSubtitle.text =
                "Find the state and local decisions that affect your community."
            binding.continueButton.text = "Continue"
        }
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.currentLocationButton.setOnClickListener {
            requestLocationPermission()
        }

        binding.continueButton.setOnClickListener {
            saveLocation()
        }
    }

    private fun requestLocationPermission() {
        val coarseGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val fineGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (coarseGranted || fineGranted) {
            getCurrentLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    private fun getCurrentLocation() {
        showToast("Getting your location...")

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        val cancellationTokenSource = CancellationTokenSource()

        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->

                if (location == null) {
                    showToast("Unable to get location. Try again or enter ZIP manually.")
                    return@addOnSuccessListener
                }

                convertLocationToZip(
                    location.latitude,
                    location.longitude
                )

            }.addOnFailureListener {
                showToast("Location failed. Enter your ZIP code manually.")
            }

        } catch (e: SecurityException) {
            showToast("Location permission is required.")
        }
    }

    private fun convertLocationToZip(
        latitude: Double,
        longitude: Double
    ) {
        viewLifecycleOwner.lifecycleScope.launch {

            val zipCode = withContext(Dispatchers.IO) {
                try {
                    val geocoder = Geocoder(
                        requireContext(),
                        Locale.getDefault()
                    )

                    val addresses = geocoder.getFromLocation(
                        latitude,
                        longitude,
                        1
                    )

                    addresses?.firstOrNull()?.postalCode

                } catch (e: Exception) {
                    null
                }
            }

            if (!zipCode.isNullOrBlank() &&
                zipCode.matches(Regex("^\\d{5}$"))
            ) {
                binding.zipCodeInput.setText(zipCode)
                binding.zipCodeInputLayout.error = null

                showToast("ZIP code found: $zipCode")
            } else {
                showToast("Couldn't find a ZIP code. Please enter it manually.")
            }
        }
    }

    private fun saveLocation() {
        val zipCode = binding.zipCodeInput.text
            ?.toString()
            ?.trim()
            .orEmpty()

        if (!zipCode.matches(Regex("^\\d{5}$"))) {
            binding.zipCodeInputLayout.error =
                "Enter a valid 5-digit ZIP code"
            return
        }

        binding.zipCodeInputLayout.error = null

        userPreferences.saveZipCode(zipCode)

        val region = if (zipCode == "08901") {
            "New Brunswick, New Jersey"
        } else {
            "ZIP code $zipCode"
        }

        userPreferences.saveRegionName(region)

        if (isEditMode) {
            // User changed location from Home or Bills.
            // Return to the screen they came from.
            findNavController().navigateUp()

        } else {
            // First-time onboarding.
            // Continue to interests.
            findNavController().navigate(
                R.id.action_location_to_interests
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
