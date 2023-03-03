package org.piramalswasthya.sakhi.ui.login_activity.sign_in

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.database.shared_preferences.PreferenceDao
import org.piramalswasthya.sakhi.databinding.FragmentSignInBinding
import org.piramalswasthya.sakhi.helpers.Languages.*
import org.piramalswasthya.sakhi.ui.login_activity.LoginActivity
import org.piramalswasthya.sakhi.ui.login_activity.sign_in.SignInViewModel.State
import org.piramalswasthya.sakhi.work.GenerateBenIdsWorker
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class SignInFragment : Fragment() {

    @Inject
    lateinit var prefDao: PreferenceDao

    private val binding by lazy {
        FragmentSignInBinding.inflate(layoutInflater)
    }

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = this.viewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (prefDao.getCurrentLanguage()) {
            ENGLISH -> binding.rgLangSelect.check(binding.rbEng.id)
            HINDI -> binding.rgLangSelect.check(binding.rbHindi.id)
            ASSAMESE -> binding.rgLangSelect.check(binding.rbAssamese.id)
        }

        binding.rgLangSelect.setOnCheckedChangeListener { _, i ->
            val currentLanguage = when (i) {
                binding.rbEng.id -> ENGLISH
                binding.rbHindi.id -> HINDI
                binding.rbAssamese.id -> ASSAMESE
                else -> ENGLISH
            }
            prefDao.saveSetLanguage(currentLanguage)
            val refresh = Intent(requireContext(), LoginActivity::class.java)
            //Timber.d("refresh Called!-${Locale.getDefault().language}-${savedLanguage.symbol}-")
            requireActivity().finish()
            startActivity(refresh)
            activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)


        }


        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state!!) {
                State.IDLE -> {
                    var hasRememberMeUsername = false
                    var hasRememberMePassword = false
                    viewModel.fetchRememberedUserName()?.let {
                        binding.etUsername.setText(it)
                        hasRememberMeUsername = true
                    }
                    viewModel.fetchRememberedPassword()?.let {
                        binding.etPassword.setText(it)
                        binding.cbRemember.isChecked = true
                        hasRememberMePassword = true
                    }
                    if(hasRememberMeUsername && hasRememberMePassword)
                        validateInput()
                }
                State.LOADING -> validateInput()
                State.ERROR_INPUT -> {
                    binding.pbSignIn.visibility = View.GONE
                    binding.clContent.visibility = View.VISIBLE
                    binding.tvError.text = "Invalid Username / password !"
                    binding.tvError.visibility = View.VISIBLE
                }
                State.ERROR_SERVER -> {
                    binding.pbSignIn.visibility = View.GONE
                    binding.clContent.visibility = View.VISIBLE
                    binding.tvError.text = "Server timed out, try again!"
                    binding.tvError.visibility = View.VISIBLE
                }
                State.ERROR_NETWORK -> {
                    binding.pbSignIn.visibility = View.GONE
                    binding.clContent.visibility = View.VISIBLE
                    binding.tvError.text = "Unable to connect to network!"
                    binding.tvError.visibility = View.VISIBLE
                }
                State.SUCCESS -> {
                    val firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
                    firebaseAnalytics.logEvent("Click_Dice_track_2") {
                        param(
                            "LOG STATE",
                            "${binding.etUsername.text} logged in!"
                        ) // send predefined parameters
                    }
                    if (binding.cbRemember.isChecked) {
                        val username = binding.etUsername.text.toString()
                        val password = binding.etPassword.text.toString()
                        viewModel.rememberUser(username, password)
                    } else {
                        viewModel.forgetUser()
                    }
                    binding.clContent.visibility = View.INVISIBLE
                    binding.pbSignIn.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    triggerGenBenIdWorker()
                    findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeActivity())
                    activity?.finish()
                }
            }
        }

    }

    private fun validateInput() {
        binding.clContent.visibility = View.INVISIBLE
        binding.pbSignIn.visibility = View.VISIBLE
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        Timber.d("Username : $username \n Password : $password")
        viewModel.authUser(username, password)
    }

    private fun triggerGenBenIdWorker() {
        val workRequest = OneTimeWorkRequestBuilder<GenerateBenIdsWorker>()
            .setConstraints(GenerateBenIdsWorker.constraint)
            .build()
        WorkManager.getInstance(requireContext())
            .enqueueUniqueWork(GenerateBenIdsWorker.name, ExistingWorkPolicy.KEEP, workRequest)
    }
}