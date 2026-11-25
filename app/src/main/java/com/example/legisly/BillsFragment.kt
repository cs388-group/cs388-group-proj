package com.example.legisly

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.legisly.adapters.BillAdapter
import com.example.legisly.data.UserPreferences
import com.example.legisly.databinding.FragmentBillsBinding
import com.example.legisly.models.Bill
import com.example.legisly.network.ApiClient
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch

class BillsFragment : Fragment(R.layout.fragment_bills) {

    private var _binding: FragmentBillsBinding? = null
    private val binding get() = _binding!!

    private lateinit var billAdapter: BillAdapter
    private lateinit var userPreferences: UserPreferences

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentBillsBinding.bind(view)
        userPreferences = UserPreferences(requireContext())

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

                val statusStr = try {
                    sessionResponse.get("status")?.asString
                } catch (e: Exception) {
                    null
                }

                if (statusStr != "OK") {
                    showError("Unable to retrieve New Jersey sessions.")
                    return@launch
                }

                val sessions = try {
                    sessionResponse.getAsJsonArray("sessions")
                } catch (e: Exception) {
                    null
                } ?: run {
                    showError("No legislative sessions were returned.")
                    return@launch
                }

                if (sessions.size() == 0) {
                    showError("No New Jersey sessions were found.")
                    return@launch
                }

                // STEP 2: Find the newest session
                val latestSession = sessions
                    .mapNotNull { if (it.isJsonObject) it.asJsonObject else null }
                    .maxWithOrNull(
                        compareBy(
                            { it.get("year_end")?.asInt ?: 0 },
                            { it.get("year_start")?.asInt ?: 0 }
                        )
                    ) ?: run {
                    showError("Unable to identify the latest session.")
                    return@launch
                }

                val sessionId = latestSession.get("session_id")?.asInt ?: 0

                val sessionName = latestSession.get("session_name")?.asString
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

                val billStatusStr = try {
                    billResponse.get("status")?.asString
                } catch (e: Exception) {
                    null
                }

                if (billStatusStr != "OK") {
                    showError("Unable to retrieve New Jersey bills.")
                    return@launch
                }

                val masterList = try {
                    billResponse.getAsJsonObject("masterlist")
                } catch (e: Exception) {
                    null
                } ?: run {
                    showError("The bill list was empty.")
                    return@launch
                }

                Log.d("LEGISCAN_DEBUG", "Masterlist count: ${masterList.entrySet().size}")

                // STEP 4: Convert JSON objects into Bill models
                val allBills = masterList.entrySet()
                    .filter { it.key != "session" }
                    .mapNotNull { entry ->
                        val item = entry.value
                        if (!item.isJsonObject) return@mapNotNull null

                        val billData = item.asJsonObject

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
                            number = getString("number").ifBlank { "Unknown bill" },
                            title = getString("title").ifBlank { "Title unavailable" },
                            description = getString("description"),
                            lastAction = getString("last_action"),
                            lastActionDate = getString("last_action_date")
                        )
                    }

                // STEP 5: Filter bills based on selected interests
                val selectedInterests = userPreferences.getInterests()
                val topicKeywords = mapOf(
                    "healthcare" to listOf("health", "medicaid", "medicare", "hospital", "doctor", "medical", "insurance", "care", "patient"),
                    "education" to listOf("school", "education", "student", "college", "university", "teacher", "academic", "tuition"),
                    "housing" to listOf("house", "housing", "rent", "tenant", "mortgage", "property", "residential", "zoning"),
                    "taxes" to listOf("tax", "revenue", "fee", "fiscal", "assessment", "income", "sales"),
                    "transportation" to listOf("transit", "road", "highway", "traffic", "vehicle", "driver", "transportation", "rail", "bus", "bridge"),
                    "environment" to listOf("environment", "climate", "energy", "water", "clean", "pollution", "solar", "conservation", "waste"),
                    "public_safety" to listOf("police", "fire", "safety", "crime", "court", "justice", "emergency", "security", "firearm"),
                    "economy" to listOf("job", "business", "economy", "economic", "employment", "wage", "commerce", "labor", "workforce")
                )

                val activeKeywords = selectedInterests.flatMap { topicKeywords[it] ?: emptyList() }

                val displayBills = if (activeKeywords.isNotEmpty()) {
                    val matching = allBills.filter { bill ->
                        val text = "${bill.title} ${bill.description}".lowercase()
                        activeKeywords.any { keyword -> text.contains(keyword) }
                    }
                    if (matching.isNotEmpty()) matching else allBills
                } else {
                    allBills
                }.sortedBy { it.number }

                Log.d("LEGISCAN_DEBUG", "Total bills: ${allBills.size}, Displayed bills: ${displayBills.size}")

                // STEP 6: Display the bills
                if (displayBills.isEmpty()) {
                    showError("No bills were found for your selected topics.")
                } else {
                    billAdapter.updateBills(displayBills)
                    binding.resultsSubtitle.text = "${displayBills.size} bills found in New Jersey"
                    binding.statusText.visibility = View.GONE
                }

            } catch (e: JsonSyntaxException) {
                Log.e("LEGISCAN_DEBUG", "JSON parse error", e)
                showError("Server response format error. Please try again.")
            } catch (e: Exception) {
                Log.e("LEGISCAN_DEBUG", "Bill loading failed", e)
                showError("Unable to load bills: ${e.localizedMessage ?: e.message}")
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
