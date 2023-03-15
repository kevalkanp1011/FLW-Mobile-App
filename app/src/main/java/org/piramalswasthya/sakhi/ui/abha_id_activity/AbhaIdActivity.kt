package org.piramalswasthya.sakhi.ui.abha_id_activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.ActivityAbhaIdBinding
import org.piramalswasthya.sakhi.network.interceptors.TokenInsertAbhaInterceptor
import org.piramalswasthya.sakhi.ui.abha_id_activity.AbhaIdViewModel.State
import timber.log.Timber

@AndroidEntryPoint
class AbhaIdActivity : AppCompatActivity() {

    private var _binding: ActivityAbhaIdBinding? = null
    private val binding: ActivityAbhaIdBinding
        get() = _binding!!

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val mainViewModel: AbhaIdViewModel by viewModels()
    private val navController by lazy {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_abha_id) as NavHostFragment
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
                    binding.navHostFragmentAbhaId.visibility = View.GONE
                    binding.clError.visibility = View.GONE
                }
                State.SUCCESS -> {
                    binding.progressBarAbhaActivity.visibility = View.GONE
                    binding.clError.visibility = View.GONE
                    binding.navHostFragmentAbhaId.visibility = View.VISIBLE
                }
                State.ERROR_NETWORK -> {
                    binding.clError.visibility = View.VISIBLE
                    binding.progressBarAbhaActivity.visibility = View.GONE
                    binding.navHostFragmentAbhaId.visibility = View.GONE
                }
                State.ERROR_SERVER -> {
                    binding.clError.visibility = View.VISIBLE
                    binding.progressBarAbhaActivity.visibility = View.GONE
                    binding.navHostFragmentAbhaId.visibility = View.GONE
                }
            }
        }
        mainViewModel.errorMessage.observe(this) {
            binding.textView5.text = it
        }
        binding.btnTryAgain.setOnClickListener {
            mainViewModel.generateAccessToken()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        Timber.d("On up pressed")
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.d("On up pressed")
//        when (item.itemId) {
//            android.R.id.home -> {
//                onBackPressed()
//                return true
//            }
//        }
        return super.onOptionsItemSelected(item)
    }


    private fun setUpActionBar() {
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(binding.toolbar)
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onDestroy() {
        super.onDestroy()
        TokenInsertAbhaInterceptor.setToken("")
    }
}