package org.piramalswasthya.sakhi.ui.home_activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.ActivityHomeBinding
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val viewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

    private val navController by lazy {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setUpActionBar()
        setUpNavHeader()

        // Snackbar.make(binding.root, intent.data.toString(), Snackbar.LENGTH_LONG).show()
    }

    private fun setUpNavHeader() {
        val headerView = binding.navView.getHeaderView(0)
        viewModel.currentUser.observe(this) {
            it?.let {
                headerView.findViewById<TextView>(R.id.tv_nav_name).text =
                    getString(R.string.nav_item_1_text, it.userName)
                headerView.findViewById<TextView>(R.id.tv_nav_role).text =
                    getString(R.string.nav_item_2_text, it.userType)
                headerView.findViewById<TextView>(R.id.tv_nav_id).text =
                    getString(R.string.nav_item_3_text, it.userId)
                headerView.findViewById<TextView>(R.id.tv_nav_version).text =
                    getString(R.string.version)
            }
        }
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbar)

        binding.navView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration
            .Builder(
                setOf(
                    R.id.homeFragment,
                    R.id.allHouseholdFragment,
                    R.id.allBenFragment
                )
            )
            .setOpenableLayout(binding.drawerLayout)
            .build()

        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

/*    override fun onSupportNavigateUp(): Boolean {
        Timber.i( "onSupportNavigateUp() called! ${navController.currentDestination?.displayName}")
        return when (navController.currentDestination?.id) {
            R.id.homeFragment -> {
                return if (viewModel.isLocationSet()) {
                    NavigationUI.navigateUp(
                        navController,
                        binding.drawerLayout
                    ) || super.onSupportNavigateUp()
                }
                else
                    false
            }
            else -> NavigationUI.navigateUp(
                navController,
                binding.drawerLayout
            ) || super.onSupportNavigateUp()
        }
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            Toast.makeText(this,"onOptionsItemSelected called!", Toast.LENGTH_SHORT).show()
            onBackPressedDispatcher.onBackPressed()
            return true // must return true to consume it here

        }
        return super.onOptionsItemSelected(item)
    }


}