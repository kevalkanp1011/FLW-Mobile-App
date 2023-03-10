package org.piramalswasthya.sakhi.ui.abha_id_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.ActivityAbhaIdBinding

@AndroidEntryPoint
class AbhaIdActivity : AppCompatActivity() {

    private var _binding : ActivityAbhaIdBinding? = null

    private val binding  : ActivityAbhaIdBinding
        get() = _binding!!

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
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbar)


        NavigationUI.setupWithNavController(binding.toolbar, navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
}