package com.example.legisly

import com.example.legisly.BuildConfig
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.legisly.databinding.ActivityMainBinding
import com.example.legisly.network.ApiClient
import kotlinx.coroutines.launch
/**
 * The one Activity that hosts all Legisly onboarding Fragments.
 * heree Navigation between screens is defined in res/navigation/nav_graph.xml.
 */


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LEGISLY_TEST", "MAIN ACTIVITY STARTED!")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            try {

                // Check that the API key exists
                if (BuildConfig.LEGISCAN_API_KEY.isBlank()) {
                    Log.e("LEGISLY_TEST", "API KEY IS EMPTY!")
                    return@launch
                }

                Log.d("LEGISLY_TEST", "Connecting to LegiScan...")

                // Request Connecticut legislative sessions
                val response = ApiClient.legiscanApi.getSessionList(
                    apiKey = BuildConfig.LEGISCAN_API_KEY,
                    state = "CT"
                )

                // Print the response in Logcat
                Log.d("LEGISLY_TEST", "API RESPONSE: $response")

                // Check API status
                val status = response.get("status")?.asString

                if (status == "OK") {
                    Log.d("LEGISLY_TEST", "SUCCESS! LegiScan connected.")
                } else {
                    Log.e("LEGISLY_TEST", "API ERROR: $status")
                }

            } catch (e: Exception) {
                Log.e("LEGISLY_TEST", "CONNECTION FAILED", e)
            }
        }


        val navHost = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHost.navController


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
