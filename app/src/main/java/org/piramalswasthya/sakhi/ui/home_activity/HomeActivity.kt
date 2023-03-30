package org.piramalswasthya.sakhi.ui.home_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.databinding.ActivityHomeBinding
import org.piramalswasthya.sakhi.helpers.MyContextWrapper
import org.piramalswasthya.sakhi.ui.abha_id_activity.AbhaIdActivity
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeViewModel
import org.piramalswasthya.sakhi.ui.login_activity.LoginActivity
import org.piramalswasthya.sakhi.work.WorkerUtils


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {


    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WrapperEntryPoint {
        val pref: PreferenceDao
    }
    private var _binding : ActivityHomeBinding? = null

    private val binding  : ActivityHomeBinding
    get() = _binding!!

    private val viewModel : HomeViewModel by viewModels()


    private val logoutAlert by lazy{
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("${if(viewModel.unprocessedRecords>0) "${viewModel.unprocessedRecords} not Processed." else "All records synced"} Are you sure to logout?")
            .setPositiveButton("YES"){
                    dialog,_->
                viewModel.logout()
                WorkerUtils.cancelAllWork(this)
                dialog.dismiss()
            }
            .setNegativeButton("NO"){
                    dialog,_->

                dialog.dismiss()
            }
            .create()
    }

    private val imagePickerActivityResult = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let {
                viewModel.saveProfilePicUri(it)
                Glide
                    .with(this)
                    .load(it)
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(binding.navView.getHeaderView(0).findViewById(R.id.iv_profile_pic))
//                binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.iv_profile_pic).setImageURI(it)
//                Glide.with(this)
//                    .load(it)
//                    .into(binding.navView.getHeaderView(0).findViewById(R.id.iv_profile_pic))
            }
    }

    private val navController by lazy {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navHostFragment.navController
    }

    var showMenuHome : Boolean = false

    override fun attachBaseContext(newBase: Context) {
        val pref = EntryPointAccessors.fromApplication(
            newBase,
            WrapperEntryPoint::class.java
        ).pref
        super.attachBaseContext(MyContextWrapper.wrap(newBase, pref.getCurrentLanguage().symbol))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()
        setUpNavHeader()
        setUpSyncWorker()
        setUpMenu()



        viewModel.navigateToLoginPage.observe(this) {
            if (it) {
                startActivity(Intent(this, LoginActivity::class.java))
                viewModel.navigateToLoginPageComplete()
                finish()
            }
        }
    }

    private fun setUpMenu() {

        val menu = object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_toolbar, menu)
                val homeMenu = menu.findItem(R.id.toolbar_menu_home)
                homeMenu.isVisible = showMenuHome
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.toolbar_menu_home -> {
                        navController.popBackStack(R.id.homeFragment, false)
                        return true
                    }
                }
                return false
            }

        }
        addMenuProvider(menu)

    }

    fun setHomeMenuItemVisibility(show  : Boolean){
        showMenuHome = show
        invalidateOptionsMenu()
    }

    private fun setUpSyncWorker() {
        WorkerUtils.triggerAmritSyncWorker(this)
        WorkerUtils.triggerD2dSyncWorker(this)
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
        val dpUri = viewModel.getProfilePicUri()
        dpUri?.let {
            Glide
                .with(this)
                .load(it)
                .placeholder(R.drawable.ic_person)
                .circleCrop()
                .into(binding.navView.getHeaderView(0).findViewById(R.id.iv_profile_pic))
        }
//

        binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.iv_profile_pic).setOnClickListener {
//            val galleryIntent = Intent(Intent.ACTION_PICK)
//            galleryIntent.type = "image/*"
            imagePickerActivityResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

        binding.navView.menu.findItem(R.id.homeFragment).setOnMenuItemClickListener {
            navController.popBackStack(R.id.homeFragment, false)
            binding.drawerLayout.close()
            true

        }
        binding.navView.menu.findItem(R.id.sync_pending_records).setOnMenuItemClickListener {
            setUpSyncWorker()
            binding.drawerLayout.close()
            true

        }
        binding.navView.menu.findItem(R.id.menu_logout).setOnMenuItemClickListener {
            logoutAlert.show()
            true

        }

        binding.navView.menu.findItem(R.id.abha_id_activity).setOnMenuItemClickListener {
            navController.popBackStack(R.id.homeFragment, false)
            startActivity(Intent(this, AbhaIdActivity::class.java))
            binding.drawerLayout.close()
            true

        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            hideKeyboard(this)
//            Toast.makeText(this, "onOptionsItemSelected called!", Toast.LENGTH_SHORT).show()
//            onBackPressedDispatcher.onBackPressed()
//            return true // must return true to consume it here
//
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    fun hideKeyboard(activity: Activity) {
//        this.currentFocus?.let { view ->
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
//            imm?.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }

    fun setLogo(resId : Int){
        binding.toolbar.setLogo(resId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}