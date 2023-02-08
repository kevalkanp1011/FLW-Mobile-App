package org.piramalswasthya.sakhi.ui.home_activity.get_ben_data

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.adapters.BenListAdapter
import org.piramalswasthya.sakhi.adapters.GetBenPageNumberAdapter
import org.piramalswasthya.sakhi.databinding.FragmentGetBenBinding
import org.piramalswasthya.sakhi.ui.home_activity.all_ben.AllBenFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.get_ben_data.GetBenViewModel.State.*

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
        val benAdapter = BenListAdapter(BenListAdapter.BenClickListener(
            {
                Toast.makeText(context,"Ben : $it clicked", Toast.LENGTH_SHORT).show()
            },
            {Toast.makeText(context,"Household : $it clicked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(AllBenFragmentDirections.actionAllBenFragmentToNewBenRegTypeFragment(it))
            }
        ))
        binding.rvBenServer.adapter = benAdapter

        val pageAdapter = GetBenPageNumberAdapter(10, GetBenPageNumberAdapter.PageClickListener{
            Toast.makeText(context,"Page : $it clicked", Toast.LENGTH_SHORT).show()
            viewModel.getBeneficiaries(it)
        })
        binding.rvPage.adapter = pageAdapter

        viewModel.state.observe(viewLifecycleOwner){
            when(it) {
                IDLE -> {} //TODO()
                LOADING -> {
                    binding.clContent.visibility = View.GONE
                    binding.flLoading.visibility = View.VISIBLE}
                ERROR_SERVER -> {

                }
                ERROR_NETWORK -> {} //TODO()
                SUCCESS -> {
                    benAdapter.submitList(viewModel.benDataList)
                    binding.clContent.visibility = View.VISIBLE
                    binding.flLoading.visibility = View.GONE
                }
        }
        }
    }
}