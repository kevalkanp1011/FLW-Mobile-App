package org.piramalswasthya.sakhi.ui.login_activity.sign_in

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentSignInBinding
import org.piramalswasthya.sakhi.ui.login_activity.sign_in.SignInViewModel.*
import timber.log.Timber

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val binding by lazy{
        FragmentSignInBinding.inflate(layoutInflater)
    }

    private val viewModel : SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = this.viewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.state.observe(viewLifecycleOwner){state->
            when(state!!){
                State.IDLE -> {
                    viewModel.fetchRememberedUserName()?.let {
                        binding.etUsername.setText(it)
                    }
                    viewModel.fetchRememberedPassword()?.let {
                        binding.etPassword.setText(it)
                        binding.cbRemember.isChecked = true
                    }

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
                    if(binding.cbRemember.isChecked) {
                        val username = binding.etUsername.text.toString()
                        val password = binding.etPassword.text.toString()
                        viewModel.rememberUser(username,password)
                    }
                    else{
                        viewModel.forgetUser()
                    }
                    binding.clContent.visibility = View.INVISIBLE
                    binding.pbSignIn.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE

                    findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeActivity())
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
}