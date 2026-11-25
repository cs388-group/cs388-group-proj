package com.example.legisly

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.legisly.adapters.BillAdapter
import com.example.legisly.databinding.FragmentBillsBinding
import com.example.legisly.models.Bill
import com.example.legisly.network.ApiClient
import kotlinx.coroutines.launch
import android.util.Log
class BillsFragment : Fragment(R.layout.fragment_bills) {

    private var _binding: FragmentBillsBinding? = null
    private val binding get() = _binding!!

    private lateinit var billAdapter: BillAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentBillsBinding.bind(view)

        billAdapter = BillAdapter()

        binding.billsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = billAdapter
        }

        loadNewJerseyBills()
    }

    private fun loadNewJerseyBills() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            binding.statusText.visibility = View.GONE

            try {
                val apiKey = BuildConfig.LEGISCAN_API_KEY

                if (apiKey.isBlank()) {
                    showError("LegiScan API key is missing.")
                    return@launch
                }

                // STEP 1: Retrieve New Jersey sessions
                val sessionResponse =
                    ApiClient.legiscanApi.getSessionList(
                        apiKey = apiKey,
                        state = "NJ"
                    )
                Log.d("LEGISCAN_DEBUG", "Session response: $sessionResponse")
                Log.d(
                    "LEGISCAN_DEBUG",
                    "Session status: ${sessionResponse.get("status")}"
                )

                if (sessionResponse.get("status")?.asString != "OK") {
                    showError("Unable to retrieve New Jersey sessions.")
                    return@launch
                }

                val sessions = sessionResponse
                    .getAsJsonArray("sessions")
                    ?: run {
                        showError("No legislative sessions were returned.")
                        return@launch
                    }

                if (sessions.size() == 0) {
                    showError("No New Jersey sessions were found.")
                    return@launch
                }

                // STEP 2: Find the newest session
                val latestSession = sessions
                    .map { it.asJsonObject }
                    .maxWithOrNull(
                        compareBy(
                            { it.get("year_end")?.asInt ?: 0 },
                            { it.get("year_start")?.asInt ?: 0 }
                        )
                    ) ?: run {
                    showError("Unable to identify the latest session.")
                    return@launch
                }

                val sessionId =
                    latestSession.get("session_id")?.asInt ?: 0

                val sessionName =
                    latestSession.get("session_name")?.asString
                        ?: "New Jersey Legislative Session"

                if (sessionId == 0) {
                    showError("The session ID was missing.")
                    return@launch
                }

                binding.sessionTitle.text = sessionName

                // STEP 3: Retrieve the session's bills
                val billResponse =
                    ApiClient.legiscanApi.getMasterList(
                        apiKey = apiKey,
                        sessionId = sessionId
                    )

                if (billResponse.get("status")?.asString != "OK") {
                    showError("Unable to retrieve New Jersey bills.")
                    return@launch
                }

                val masterList =
                    billResponse.getAsJsonObject("masterlist")
                        ?: run {
                            showError("The bill list was empty.")
                            return@launch
                        }
                Log.d("LEGISCAN_DEBUG", "Bill response: $billResponse")
                Log.d(
                    "LEGISCAN_DEBUG",
                    "Bill response status: ${billResponse.get("status")}"
                )
                Log.d(
                    "LEGISCAN_DEBUG",
                    "Masterlist: ${billResponse.get("masterlist")}"
                )

                // STEP 4: Convert the JSON objects into Bill models
                val bills = masterList.entrySet()
                    .filter { it.key != "session" }
                    .mapNotNull { entry ->
                        val item = entry.value

                        if (!item.isJsonObject) return@mapNotNull null

                        val billData = item.asJsonObject

                        // Helper function safely reads JSON strings, including JsonNull
                        fun getString(field: String): String {
                            val value = billData.get(field)

                            return if (value == null || value.isJsonNull) {
                                ""
                            } else {
                                value.asString
                            }
                        }

                        val billIdValue = billData.get("bill_id")

                        if (billIdValue == null || billIdValue.isJsonNull) {
                            return@mapNotNull null
                        }

                        Bill(
                            billId = billIdValue.asInt,
                            number = getString("number").ifBlank { "Unknown bill number" },
                            title = getString("title").ifBlank { "Title unavailable" },
                            description = getString("description"),
                            lastAction = getString("last_action"),
                            lastActionDate = getString("last_action_date")
                        )
                    }
                    .sortedBy { it.number }
                Log.d("LEGISCAN_DEBUG", "Converted bill count: ${bills.size}")
                Log.d("LEGISCAN_DEBUG", "First bill: ${bills.firstOrNull()}")
                // STEP 5: Display the bills
                if (bills.isEmpty()) {
                    showError("No bills were found in this session.")
                } else {
                    billAdapter.updateBills(bills)
                    Log.d("LEGISCAN_DEBUG", "Adapter received: ${bills.size} bills")
                    Log.d("LEGISCAN_DEBUG", "Adapter item count: ${billAdapter.itemCount}")

                    binding.resultsSubtitle.text =
                        "${bills.size} bills found in New Jersey"

                    binding.statusText.visibility = View.GONE
                }

            } catch (e: Exception) {
                Log.e("LEGISCAN_DEBUG", "Bill loading failed", e)
                showError("Unable to load bills: ${e.message}")
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showError(message: String) {
        binding.statusText.text = message
        binding.statusText.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
