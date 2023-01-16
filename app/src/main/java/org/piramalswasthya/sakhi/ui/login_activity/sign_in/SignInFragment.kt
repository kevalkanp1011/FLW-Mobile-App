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
                State.IDLE -> {}
                State.LOADING -> validateInput()
                State.ERROR_INPUT -> {}
                State.ERROR_SERVER -> {}
                State.ERROR_NETWORK -> {}
                State.SUCCESS -> {
                    binding.clContent.visibility = View.INVISIBLE
                    binding.pbSignIn.visibility = View.VISIBLE
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