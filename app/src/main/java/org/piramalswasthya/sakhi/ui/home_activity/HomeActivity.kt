package org.piramalswasthya.sakhi.ui.home_activity

import android.content.Context
import android.content.Intent
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
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.databinding.ActivityHomeBinding
import org.piramalswasthya.sakhi.helpers.MyContextWrapper
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel
import org.piramalswasthya.sakhi.ui.login_activity.LoginActivity
import org.piramalswasthya.sakhi.work.PullFromAmritFullLoadWorker
import org.piramalswasthya.sakhi.work.PushToAmritWorker

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WrapperEntryPoint {
        val preferenceDao: PreferenceDao
    }

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

    private val viewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

    private val navController by lazy {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navHostFragment.navController
    }

    override fun attachBaseContext(newBase: Context) {
        val pref = EntryPointAccessors.fromApplication(
            newBase,
            WrapperEntryPoint::class.java
        ).preferenceDao
        super.attachBaseContext(MyContextWrapper.wrap(newBase, pref.getCurrentLanguage().symbol))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setUpActionBar()
        setUpNavHeader()
        setUpFullLoadPullWorker()

        viewModel.navigateToLoginPage.observe(this) {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java))
                viewModel.navigateToLoginPageComplete()
                finish()
            }
        }
    }

    private fun setUpFullLoadPullWorker() {
        if (viewModel.checkIfFullLoadCompletedBefore())
            return
        val workRequest = OneTimeWorkRequestBuilder<PullFromAmritFullLoadWorker>()
            .setConstraints(PullFromAmritFullLoadWorker.constraint)
            .build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniqueWork(
            PushToAmritWorker.name,
            ExistingWorkPolicy.KEEP,
            workRequest
        )
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

//                headerView.findViewById<TextView>(R.id.tv_nav_version).text =
//                    getString(R.string.version)
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

        binding.navView.menu.findItem(R.id.menu_logout).setOnMenuItemClickListener {
            viewModel.logout()
            true

        }
        binding.navView.menu.findItem(R.id.homeFragment).setOnMenuItemClickListener {
            navController.popBackStack(R.id.homeFragment, false)
            binding.drawerLayout.close()
            true

        }
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
            Toast.makeText(this, "onOptionsItemSelected called!", Toast.LENGTH_SHORT).show()
            onBackPressedDispatcher.onBackPressed()
            return true // must return true to consume it here

        }
        return super.onOptionsItemSelected(item)
    }
}