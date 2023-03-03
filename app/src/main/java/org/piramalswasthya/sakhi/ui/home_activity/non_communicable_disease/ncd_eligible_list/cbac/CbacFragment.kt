package org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.ncd_eligible_list.cbac

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.FragmentCbacBinding
import org.piramalswasthya.sakhi.model.Gender
import timber.log.Timber

@AndroidEntryPoint
class CbacFragment : Fragment() {


    private val binding by lazy { FragmentCbacBinding.inflate(layoutInflater) }

    private val viewModel: CbacViewModel by viewModels()

    private val alertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle("MISSING FIELD")
            .create()
    }

    private val raAlertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle("SUSPECTED NCD CASE!")
            .setMessage(context?.getString(R.string.ncd_sus_valid))
            .setPositiveButton("Ok"){dialog,_ -> dialog.dismiss() }
            .create()
    }
    private val ast1AlertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle("SUSPECTED HRP AND NCD CASE!")
            .setMessage(context?.getString(R.string.hrpncd_sus_valid))
            .setPositiveButton("Ok"){dialog,_ -> dialog.dismiss() }
            .create()
    }
    private val ast2AlertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle("SUSPECTED HRP CASE!")
            .setMessage(context?.getString(R.string.hrp_sug_valid))
            .setPositiveButton("Ok"){dialog,_ -> dialog.dismiss() }
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                CbacViewModel.State.SAVING -> {
                    binding.llContent.visibility = View.GONE
                    binding.pbCbac.visibility = View.VISIBLE
                }
                CbacViewModel.State.SAVE_FAIL -> {
                    binding.llContent.visibility = View.VISIBLE
                    binding.pbCbac.visibility = View.GONE
                    Timber.d("Ran into error! Cbac data not saved!")
                    viewModel.resetState()
                }
                CbacViewModel.State.SAVE_SUCCESS -> {
                    Timber.d("CBAC form saved successfully!")
                    viewModel.resetState()
                    findNavController().navigateUp()
                }
                CbacViewModel.State.MISSING_FIELD -> {
                    binding.llContent.visibility = View.VISIBLE
                    binding.pbCbac.visibility = View.GONE
                    alertDialog.setMessage(viewModel.missingFieldString)
                    alertDialog.show()
                }

                else -> {
                    Timber.d("IDLE!")
                }
            }
        }
        binding.btnSave.setOnClickListener {
            viewModel.submitForm()
        }
        viewModel.benName.observe(viewLifecycleOwner) {
            binding.tvBenName.text = it
        }
        viewModel.benAgeGender.observe(viewLifecycleOwner) {
            binding.tvAgeGender.text = it
        }
        //RA START
        viewModel.gender.observe(viewLifecycleOwner) {
            when (it) {
                Gender.MALE -> {
                    binding.actvWaistDropdown.setSimpleItems(R.array.cbac_waist_mes_male)
                    binding.cbacLlEdWomen.visibility = View.GONE
                }
                else -> binding.actvWaistDropdown.setSimpleItems(R.array.cbac_waist_mes_female)
            }
        }
        viewModel.raAgeText.observe(viewLifecycleOwner) {
            binding.actvAgeDropdown.setText(it)
        }
        viewModel.raAgeScore.observe(viewLifecycleOwner) {
            binding.ddAgeScore.text = it
        }
        viewModel.raSmokeScore.observe(viewLifecycleOwner) {
            binding.ddSmokeScore.text = it
        }
        viewModel.raAlcoholScore.observe(viewLifecycleOwner) {
            binding.ddAlcoholScore.text = it
        }
        viewModel.raWaistScore.observe(viewLifecycleOwner) {
            binding.ddWaistScore.text = it
        }
        viewModel.raPaScore.observe(viewLifecycleOwner) {
            binding.ddPaScore.text = it
        }
        viewModel.raFhScore.observe(viewLifecycleOwner) {
            binding.ddFhScore.text = it
        }
        viewModel.raTotalScore.observe(viewLifecycleOwner) {
            if (it.substring(it.lastIndexOf(' ') + 1).toInt() >= 4) {
                binding.ncdSusValidDisplay.visibility = View.VISIBLE
                viewModel.setFlagForNcd(true)
                raAlertDialog.show()
            } else {
                if (binding.ncdSusValidDisplay.visibility != View.GONE) {
                    binding.ncdSusValidDisplay.visibility = View.GONE
                    viewModel.setFlagForNcd(false)
                }
            }
            binding.cbacTvRaTotalScore.text = it
        }


        binding.actvSmokeDropdown.setOnItemClickListener { _, _, i, _ -> viewModel.setSmoke(i) }
        binding.actvAlcoholDropdown.setOnItemClickListener { _, _, i, _ -> viewModel.setAlcohol(i) }
        binding.actvWaistDropdown.setOnItemClickListener { _, _, i, _ -> viewModel.setWaist(i) }
        binding.actvPaDropdown.setOnItemClickListener { _, _, i, _ -> viewModel.setPa(i) }
        binding.actvFhDropdown.setOnItemClickListener { _, _, i, _ -> viewModel.setFh(i) }
        //RA END

        //ED START
        viewModel.ast1.observe(viewLifecycleOwner) {
            Timber.d("value of ast1 : $it")
            if (it > 0) {
                binding.tvTbSputumCollect.visibility = View.VISIBLE
                viewModel.setCollectSputum(1)
                ast1AlertDialog.show()
            } else {
                binding.tvTbSputumCollect.visibility = View.GONE
                viewModel.setCollectSputum(1)
            }
        }
        viewModel.ast2.observe(viewLifecycleOwner) {
            if (it > 0) {
                binding.tbSusValidDisplay.visibility = View.VISIBLE
                viewModel.setTraceAllMembers(1)
                ast2AlertDialog.show()
            } else {
                binding.tbSusValidDisplay.visibility = View.GONE
                viewModel.setTraceAllMembers(0)
            }
        }
        viewModel.astMoic.observe(viewLifecycleOwner) {
            if (it > 0) {
                binding.tvTbMoicVisit.visibility = View.VISIBLE
                viewModel.setReferMoic(1)
            } else {
                binding.tvTbMoicVisit.visibility = View.GONE
                viewModel.setReferMoic(0)
            }
        }
        binding.cbacFhTb.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setFhTb(1)
                R.id.rb_no -> viewModel.setFhTb(2)

            }
        }
        binding.cbacTakingTbDrug.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setTakingTbDrug(1)
                R.id.rb_no -> viewModel.setTakingTbDrug(2)

            }
        }
        binding.cbacHistb.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setHisTb(1)
                R.id.rb_no -> viewModel.setHisTb(2)
            }
        }
        binding.cbacCoughing.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setCoughing(1)
                R.id.rb_no -> viewModel.setCoughing(2)
            }
        }
        binding.cbacBlsputum.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setBloodSputum(1)
                R.id.rb_no -> viewModel.setBloodSputum(2)
            }
        }
        binding.cbacFeverwks.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setFeverWks(1)
                R.id.rb_no -> viewModel.setFeverWks(2)
            }
        }
        binding.cbacLsweight.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setLsWt(1)
                R.id.rb_no -> viewModel.setLsWt(2)
            }
        }
        binding.cbacNtswets.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setNtSwets(1)
                R.id.rb_no -> viewModel.setNtSwets(2)
            }
        }
        binding.cbacRecurrentUlceration.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setRecurrentUlceration(1)
                R.id.rb_no -> viewModel.setRecurrentUlceration(2)
            }
        }
        binding.cbacRecurrentCloudy.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setRecurrentCloudy(1)
                R.id.rb_no -> viewModel.setRecurrentCloudy(2)
            }
        }
        binding.cbacRecurrentDiffcultyReading.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setDiffReading(1)
                R.id.rb_no -> viewModel.setDiffReading(2)
            }
        }

        binding.cbacRecurrentPainEyes.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setPainEyes(1)
                R.id.rb_no -> viewModel.setPainEyes(2)
            }
        }
        binding.cbacRecurrentRednessEyes.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setRedEyes(1)
                R.id.rb_no -> viewModel.setRedEyes(2)
            }
        }
        binding.cbacRecurrentDiffHearing.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setDiffHearing(1)
                R.id.rb_no -> viewModel.setDiffHearing(2)
            }
        }
        binding.cbacBreath.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setBreathe(1)
                R.id.rb_no -> viewModel.setBreathe(2)
            }
        }
        binding.cbacRecurrentTingling.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setTing(1)
                R.id.rb_no -> viewModel.setTing(2)
            }
        }
        binding.cbacHifits.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setHisFits(1)
                R.id.rb_no -> viewModel.setHisFits(2)
            }
        }
        binding.cbacDifmouth.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setDiffMouth(1)
                R.id.rb_no -> viewModel.setDiffMouth(2)
            }
        }
        binding.cbacHeald.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setHealed(1)
                R.id.rb_no -> viewModel.setHealed(2)
            }
        }
        binding.cbacVoice.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setVoice(1)
                R.id.rb_no -> viewModel.setVoice(2)
            }
        }
        binding.cbacAnyGrowth.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setAnyGrowth(1)
                R.id.rb_no -> viewModel.setAnyGrowth(2)
            }
        }
        binding.cbacAnyWhite.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setAnyWhite(1)
                R.id.rb_no -> viewModel.setAnyWhite(2)
            }
        }
        binding.cbacPainWhileChewing.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setPainChew(1)
                R.id.rb_no -> viewModel.setPainChew(2)
            }
        }
        binding.cbacAnyHyperPigmented.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setHyperPig(1)
                R.id.rb_no -> viewModel.setHyperPig(2)
            }
        }
        binding.cbacAnyThickendSkin.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setThickSkin(1)
                R.id.rb_no -> viewModel.setThickSkin(2)
            }
        }
        binding.cbacAnyNodulesSkin.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setNoduleSkin(1)
                R.id.rb_no -> viewModel.setNoduleSkin(2)
            }
        }
        binding.cbacRecurrentNumbness.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setNumb(1)
                R.id.rb_no -> viewModel.setNumb(2)
            }
        }
        binding.cbacClawingOfFingers.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setClaw(1)
                R.id.rb_no -> viewModel.setClaw(2)
            }
        }
        binding.cbacTinglingOrNumbness.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setTingNumb(1)
                R.id.rb_no -> viewModel.setTingNumb(2)
            }
        }
        binding.cbacInabilityCloseEyelid.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setCloseEyelid(1)
                R.id.rb_no -> viewModel.setCloseEyelid(2)
            }
        }
        binding.cbacDiffHoldingObjects.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setHoldObj(1)
                R.id.rb_no -> viewModel.setHoldObj(2)
            }
        }
        binding.cbacWeeknessInFeet.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setWeakFeet(1)
                R.id.rb_no -> viewModel.setWeakFeet(2)
            }
        }
        binding.cbacLumpbrest.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setLumpB(1)
                R.id.rb_no -> viewModel.setLumpB(2)
            }
        }
        binding.cbacNipple.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setNipple(1)
                R.id.rb_no -> viewModel.setNipple(2)
            }
        }
        binding.cbacBreast.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setBreast(1)
                R.id.rb_no -> viewModel.setBreast(2)
            }
        }
        binding.cbacBlperiods.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setBlP(1)
                R.id.rb_no -> viewModel.setBlP(2)
            }
        }
        binding.cbacBlmenopause.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> {
                    viewModel.setBlM(1)
                    binding.tvBlMenopause.visibility = View.VISIBLE
                }
                R.id.rb_no -> {
                    viewModel.setBlM(2)
                    binding.tvBlMenopause.visibility = View.GONE
                }
            }
        }
        binding.cbacBlintercorse.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setBlI(1)
                R.id.rb_no -> viewModel.setBlI(2)
            }
        }
        binding.cbacFouldis.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setFoulD(1)
                R.id.rb_no -> viewModel.setFoulD(2)
            }
        }
        binding.cbacUnsteady.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setUnsteady(1)
                R.id.rb_no -> viewModel.setUnsteady(2)
            }
        }
        binding.cbacPdRm.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setPdRm(1)
                R.id.rb_no -> viewModel.setPdRm(2)
            }
        }
        binding.cbacNhop.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setNhop(1)
                R.id.rb_no -> viewModel.setNhop(2)
            }
        }
        binding.cbacForgetNames.cbacEdRg.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_yes -> viewModel.setForgetNames(1)
                R.id.rb_no -> viewModel.setForgetNames(2)
            }
        }
        //ED END

        //RF COPD START
        binding.actvFuelDropdown.setOnItemClickListener { _, _, i, _ -> viewModel.setFuelType(i) }
        binding.actvExposureDropdown.setOnItemClickListener { _, _, i, _ ->
            viewModel.setOccExposure(
                i
            )
        }
        //RF COPD END

        //PHQ2 START
        binding.actvLiDropdown.setOnItemClickListener { _, _, i, _ -> viewModel.setLi(i) }
        binding.actvFdDropdown.setOnItemClickListener { _, _, i, _ -> viewModel.setFd(i) }
        viewModel.phq2LittleInterestScore.observe(viewLifecycleOwner) {
            binding.ddLiScore.text = it
        }
        viewModel.phq2FeelDownDepScore.observe(viewLifecycleOwner) {
            binding.ddFdScore.text = it
        }
        viewModel.phq2TotalScore.observe(viewLifecycleOwner) {
            if (it.substring(it.lastIndexOf(' ') + 1).toInt() > 3) {
                binding.tvTbMoicVisit.visibility = View.VISIBLE
                viewModel.setFlagForPhQ2(true)
            } else {
                if (binding.tvTbMoicVisit.visibility != View.GONE) {
                    binding.tvTbMoicVisit.visibility = View.GONE
                    viewModel.setFlagForPhQ2(false)
                }
            }
            binding.cbacPhq2TotalScore.text = it
        }
        viewModel.age.observe(viewLifecycleOwner) {
            if (it >= 60)
                binding.cbacLlEdElderly.visibility = View.VISIBLE
            else
                if (binding.cbacLlEdElderly.visibility != View.GONE)
                    binding.cbacLlEdElderly.visibility = View.GONE
        }


    }

}