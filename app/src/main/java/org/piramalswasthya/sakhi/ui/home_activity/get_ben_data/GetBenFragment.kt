package org.piramalswasthya.sakhi.ui.home_activity.get_ben_data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.BenListAdapter
import org.piramalswasthya.sakhi.adapters.GetBenPageNumberAdapter
import org.piramalswasthya.sakhi.databinding.FragmentGetBenBinding
import org.piramalswasthya.sakhi.ui.home_activity.get_ben_data.GetBenViewModel.State.*
import timber.log.Timber

@AndroidEntryPoint
class GetBenFragment : Fragment() {

    companion object {
        fun newInstance() = GetBenFragment()
    }

    private val binding: FragmentGetBenBinding by lazy{
        FragmentGetBenBinding.inflate(layoutInflater)
    }

    private val viewModel: GetBenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val benAdapter = BenListAdapter()
        binding.rvBenServer.adapter = benAdapter

        viewModel.state.observe(viewLifecycleOwner){
            when(it) {
                IDLE -> {} //TODO()
                LOADING -> {
                    binding.clError.visibility = View.GONE
                    binding.clContent.visibility = View.GONE
                    binding.flLoading.visibility = View.VISIBLE
                }
                ERROR_SERVER -> {
                    binding.clError.visibility = View.VISIBLE
                    binding.clContent.visibility = View.GONE
                    binding.flLoading.visibility = View.GONE
                }
                ERROR_NETWORK -> {} //TODO()
                SUCCESS -> {
                    setUpPagesAdapter()
                    benAdapter.submitList(viewModel.benDataList)
                    binding.clError.visibility = View.GONE
                    binding.clContent.visibility = View.VISIBLE
                    binding.flLoading.visibility = View.GONE
                }
            }
        }
    }

    private fun setUpPagesAdapter() {
        if (binding.rvPage.adapter == null) {
            Timber.d("Num of pages : ${viewModel.numPages}")
            val pageAdapter =
                GetBenPageNumberAdapter(
                    viewModel.numPages,
                    GetBenPageNumberAdapter.PageClickListener { page ->
                        Toast.makeText(context, "Page : $page clicked", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.getBeneficiaries(page - 1)
                    })
            binding.rvPage.adapter = pageAdapter
        }
    }
}