package org.piramalswasthya.sakhi.ui.home_activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
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
import timber.log.Timber


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

    private val viewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

    private val imagePickerActivityResult = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let {
                viewModel.saveProfilePicUri(it)
                Glide
                    .with(this)
                    .load(it)
                    .placeholder(R.drawable.ic_person)
                    .circleCrop()
                    .into(binding.navView.getHeaderView(0).findViewById(R.id.iv_profile_pic));
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
        setUpFullLoadPullWorker()
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

    private fun setUpFullLoadPullWorker() {
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
        val dpUri = viewModel.getProfilePicUri()
        dpUri?.let {
            Glide
                .with(this)
                .load(it)
                .placeholder(R.drawable.ic_person)
                .circleCrop()
                .into(binding.navView.getHeaderView(0).findViewById(R.id.iv_profile_pic));
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

        binding.navView.menu.findItem(R.id.menu_logout).setOnMenuItemClickListener {
            val workManager = WorkManager.getInstance(this)
            workManager.cancelAllWork()
            viewModel.logout()
            true

        }
        binding.navView.menu.findItem(R.id.homeFragment).setOnMenuItemClickListener {
            navController.popBackStack(R.id.homeFragment, false)
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

    fun hideKeyboard(activity: Activity) {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun setLogo(resId : Int){
        binding.toolbar.setLogo(resId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}