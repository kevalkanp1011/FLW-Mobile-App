package org.piramalswasthya.sakhi.ui.abha_id_activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.ActivityAbhaIdBinding
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertAbhaInterceptor
import org.piramalswasthya.sakhi.ui.abha_id_activity.AbhaIdViewModel.State
import org.piramalswasthya.sakhi.ui.abha_id_activity.aadhaar_id.AadhaarIdFragment

@AndroidEntryPoint
class AbhaIdActivity : AppCompatActivity() {

    private var _binding: ActivityAbhaIdBinding? = null
    private val binding: ActivityAbhaIdBinding
        get() = _binding!!

    private val mainViewModel: AbhaIdViewModel by viewModels()
    private val navController by lazy {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_activity_abha_id) as NavHostFragment
        navHostFragment.navController
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAbhaIdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()

        mainViewModel.state.observe(this) { state ->
            when (state) {
                State.LOADING_TOKEN -> {
                    // Show progress bar
                    binding.progressBarAbhaActivity.visibility = View.VISIBLE
                    // Hide other views (if any)
                    binding.navHostActivityAbhaId.visibility = View.GONE
                    binding.clError.visibility = View.GONE
                }
                State.SUCCESS -> {
                    binding.progressBarAbhaActivity.visibility = View.GONE
                    binding.clError.visibility = View.GONE
                    binding.navHostActivityAbhaId.visibility = View.VISIBLE
//                    DO NOT NEED THIS (destination of nav graph is Aadhar Id Fragment --- Our nav graph takes care of transactions
                //                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.nav_host_activity_abha_id, AadhaarIdFragment())
//                        .commit()
                }
                State.ERROR_NETWORK -> {

                    binding.clError.visibility = View.VISIBLE
                    binding.progressBarAbhaActivity.visibility = View.GONE
                    binding.navHostActivityAbhaId.visibility = View.GONE


                }
                else -> {}
            }
        }
    }


    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbar)
        NavigationUI.setupWithNavController(binding.toolbar, navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        TokenInsertAbhaInterceptor.setToken("")
    }
}