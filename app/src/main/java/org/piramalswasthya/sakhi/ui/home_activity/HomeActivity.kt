package org.piramalswasthya.sakhi.ui.home_activity

import android.os.Bundle
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.ActivityHomeBinding
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityHomeBinding.inflate(layoutInflater)}

    private val viewModel by lazy{ViewModelProvider(this)[HomeViewModel::class.java]}

    private val navController by lazy{
        val navHostFragment : NavHostFragment =supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
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

        NavigationUI.setupWithNavController(binding.toolbar,navController, appBarConfiguration)
        NavigationUI.setupActionBarWithNavController(this,navController, appBarConfiguration)

        Snackbar.make(binding.root,intent.data.toString(),Snackbar.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        Timber.i( "Inside onSupportNavigateUp() ${viewModel.location}")
        return if(viewModel.location)
            NavigationUI.navigateUp(navController,binding.drawerLayout) || super.onSupportNavigateUp()
        else
            false
    }



}