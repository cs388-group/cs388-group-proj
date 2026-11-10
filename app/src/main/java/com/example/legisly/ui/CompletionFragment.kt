package com.example.legisly.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.legisly.R
import com.example.legisly.data.UserPreferences
import com.example.legisly.databinding.FragmentCompletionBinding

class CompletionFragment : Fragment() {

    private var _binding: FragmentCompletionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val preferences = UserPreferences(requireContext())
        val topics = preferences.getInterests().sorted().joinToString(", ")

        binding.summaryText.text =
            "Location: ${preferences.getRegionName()} (${preferences.getZipCode()})\n\n" +
            "Your selected topics: $topics"

        binding.backToStartButton.setOnClickListener {
            findNavController().navigate(
                R.id.welcomeFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.welcomeFragment, true)
                    .build()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
