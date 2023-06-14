package org.piramalswasthya.sakhi.configuration

import android.content.res.Resources
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.ChildImmunizationCategory
import org.piramalswasthya.sakhi.model.Icon
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import org.piramalswasthya.sakhi.ui.home_activity.child_care.ChildCareFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.eligible_couple.EligibleCoupleFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.immunization_due.ImmunizationDueTypeFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.immunization_due.child_immunization.categories.ChildImmunizationCategoriesFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.mother_care.MotherCareFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.NcdFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.village_level_forms.VillageLevelFormsFragmentDirections
import javax.inject.Inject

@ActivityRetainedScoped
class IconDataset @Inject constructor(private val recordsRepo: RecordsRepo) {

    fun getHomeIconDataset(resources: Resources) = listOf(
        Icon(
            R.drawable.ic__hh,
            resources.getString(R.string.icon_title_household),
            recordsRepo.getHhListCount(),
            HomeFragmentDirections.actionNavHomeToAllHouseholdFragment()
        ),
        Icon(
            R.drawable.ic__ben,
            resources.getString(R.string.icon_title_ben),
            recordsRepo.getBenListCount(),
            HomeFragmentDirections.actionNavHomeToAllBenFragment()
        ),
        Icon(
            R.drawable.ic__eligible_couple,
            resources.getString(R.string.icon_title_ec),
            recordsRepo.getEligibleCoupleListCount(),
            HomeFragmentDirections.actionNavHomeToEligibleCoupleFragment()
        ),
        Icon(
            R.drawable.ic__mother_care,
            resources.getString(R.string.icon_title_mc),
            null,
            HomeFragmentDirections.actionNavHomeToMotherCareFragment()
        ),
        Icon(
            R.drawable.ic__child_care,
            resources.getString(R.string.icon_title_cc),
            null,
            HomeFragmentDirections.actionNavHomeToChildCareFragment()
        ),
        Icon(
            R.drawable.ic__ncd,
            resources.getString(R.string.icon_title_ncd),
            null,
            HomeFragmentDirections.actionNavHomeToNcdFragment()
        ),
        Icon(
            R.drawable.ic__immunization,
            resources.getString(R.string.icon_title_imm),
            null,
            HomeFragmentDirections.actionNavHomeToImmunizationDueFragment()
        ),
        Icon(
            R.drawable.ic__hrp,
            resources.getString(R.string.icon_title_hrp),
            recordsRepo.hrpListCount,
            HomeFragmentDirections.actionNavHomeToHrpCasesFragment()
        ),
        Icon(
            R.drawable.ic__general_op,
            resources.getString(R.string.icon_title_gop),
            null,
            HomeFragmentDirections.actionNavHomeToGeneralOpCareFragment()
        ),
        Icon(
            R.drawable.ic__menopause,
            resources.getString(R.string.icon_title_msl),
            recordsRepo.menopauseListCount,
            HomeFragmentDirections.actionNavHomeToMenopauseStageFragment()
        ),
        Icon(
            R.drawable.ic__death,
            resources.getString(R.string.icon_title_dr),
            null,
            HomeFragmentDirections.actionNavHomeToDeathReportsFragment()
        ),
        Icon(
            R.drawable.ic__village_level_form,
            resources.getString(R.string.icon_title_vlf),
            null,
            HomeFragmentDirections.actionNavHomeToVillageLevelFormsFragment()
        ),
    )

    fun getChildCareDataset(resources: Resources) = listOf(
        Icon(
            R.drawable.ic__infant,
            resources.getString(R.string.icon_title_icc),
            recordsRepo.infantListCount,
            ChildCareFragmentDirections.actionChildCareFragmentToInfantListFragment()
        ), Icon(
            R.drawable.ic__child,
            resources.getString(R.string.icon_title_ccc),
            recordsRepo.childListCount,
            ChildCareFragmentDirections.actionChildCareFragmentToChildListFragment()
        ), Icon(
            R.drawable.ic__adolescent,
            resources.getString(R.string.icon_title_acc),
            recordsRepo.adolescentListCount,
            ChildCareFragmentDirections.actionChildCareFragmentToAdolescentListFragment()
        )
    )

    fun getEligibleCoupleDataset(resources: Resources) = listOf(
        Icon(
            R.drawable.ic__eligible_couple,
            resources.getString(R.string.icon_title_ecr),
            null,
            EligibleCoupleFragmentDirections.actionEligibleCoupleFragmentToEligibleCoupleListFragment()
        ),
        Icon(
            R.drawable.ic__eligible_couple,
            resources.getString(R.string.icon_title_ect),
//            recordsRepo.pregnantListCount,
            null,
            EligibleCoupleFragmentDirections.actionEligibleCoupleFragmentToEligibleCoupleTrackingListFragment()
        )
    )

    fun getMotherCareDataset(resources: Resources) = listOf(
        Icon(
            R.drawable.ic__pregnancy,
            resources.getString(R.string.icon_title_pmr),
//            recordsRepo.pregnantListCount,
            null,
            MotherCareFragmentDirections.actionMotherCareFragmentToPwRegistrationFragment()
        ),
        Icon(
            R.drawable.ic__pregnancy,
            resources.getString(R.string.icon_title_pmt),
//            recordsRepo.pregnantListCount,
            null,
            MotherCareFragmentDirections.actionMotherCareFragmentToPwAncVisitsFragment()
        ),
//        , Icon(
//            R.drawable.ic__delivery,
//            resources.getString(R.string.icon_title_dmc),
//            recordsRepo.deliveryListCount,
//            MotherCareFragmentDirections.actionMotherCareFragmentToDeliveryStageListFragment()
//        ), Icon(
//            R.drawable.ic__pnc,
//            resources.getString(R.string.icon_title_pncmc),
//            recordsRepo.pncMotherListCount,
//            MotherCareFragmentDirections.actionMotherCareFragmentToPncMotherListFragment()
//        ), Icon(
//            R.drawable.ic__reproductive_age,
//            resources.getString(R.string.icon_title_rmc),
//            recordsRepo.reproductiveAgeListCount,
//            MotherCareFragmentDirections.actionMotherCareFragmentToReproductiveAgeListFragment()
//        )
    )

    fun getNCDDataset(resources: Resources) = listOf(
        Icon(
            R.drawable.ic__ncd_list,
            resources.getString(R.string.icon_title_ncd_list),
            recordsRepo.ncdListCount,
            NcdFragmentDirections.actionNcdFragmentToNcdListFragment()
        ), Icon(
            R.drawable.ic__ncd_eligibility,
            resources.getString(R.string.icon_title_ncd_eligible_list),
            recordsRepo.ncdEligibleListCount,
            NcdFragmentDirections.actionNcdFragmentToNcdEligibleListFragment()
        ), Icon(
            R.drawable.ic__ncd_priority,
            resources.getString(R.string.icon_title_ncd_non_eligible_list),
            recordsRepo.ncdPriorityListCount,
            NcdFragmentDirections.actionNcdFragmentToNcdPriorityListFragment()
        ), Icon(
            R.drawable.ic_ncd_noneligible,
            resources.getString(R.string.icon_title_ncd_priority_list),
            recordsRepo.ncdNonEligibleListCount,
            NcdFragmentDirections.actionNcdFragmentToNcdNonEligibleListFragment()
        ), Icon(
            R.drawable.ic__ncd_eligibility,
            resources.getString(R.string.icon_title_ncd_tb_screening),
            recordsRepo.tbScreeningListCount,
            NcdFragmentDirections.actionNcdFragmentToTBScreeningListFragment()
        ), Icon(
            R.drawable.ic__death,
            resources.getString(R.string.icon_title_ncd_tb_suspected),
            recordsRepo.ncdNonEligibleListCount,
            NcdFragmentDirections.actionNcdFragmentToTBSuspectedListFragment()
        )
    )

    fun getImmunizationDataset() = listOf(
        Icon(
            R.drawable.ic__immunization,
            "Child Immunization",
            recordsRepo.childrenImmunizationListCount,
            ImmunizationDueTypeFragmentDirections.actionImmunizationDueTypeFragmentToChildImmunizationFragment()
        ),
        Icon(
            R.drawable.ic__immunization,
            "Mother Immunization",
            recordsRepo.motherImmunizationListCount,
            ImmunizationDueTypeFragmentDirections.actionImmunizationDueTypeFragmentToMotherImmunizationFragment()
        ),

        )


    fun getVillageLevelFormsDataset(resources: Resources) = listOf(
        Icon(
            R.drawable.ic_person,
            resources.getString(R.string.icon_title_sr),
            null,
            VillageLevelFormsFragmentDirections.actionVillageLevelFormsFragmentToSurveyRegisterFragment()
        )
    )

    fun getChildImmunizationCategories(resources: Resources) = listOf(
        Icon(
            R.drawable.ic__infant,
            "Birth Dose Vaccines Babies",
            null,
            ChildImmunizationCategoriesFragmentDirections.actionChildImmunizationFragmentToChildImmunizationListFragment(
                ChildImmunizationCategory.BIRTH
            )
        ),
        Icon(
            R.drawable.ic__infant,
            "6 Weeks Vaccines Children",
            null,
            ChildImmunizationCategoriesFragmentDirections.actionChildImmunizationFragmentToChildImmunizationListFragment(
                ChildImmunizationCategory.WEEK_6
            )
        ),
        Icon(
            R.drawable.ic__infant,
            "10 Weeks Vaccines Children",
            null,
            ChildImmunizationCategoriesFragmentDirections.actionChildImmunizationFragmentToChildImmunizationListFragment(
                ChildImmunizationCategory.WEEK_10
            ),
        ),
        Icon(
            R.drawable.ic__infant,
            "14 Weeks Vaccines Children",
            null,
            ChildImmunizationCategoriesFragmentDirections.actionChildImmunizationFragmentToChildImmunizationListFragment(
                ChildImmunizationCategory.WEEK_14
            )
        ),
        Icon(
            R.drawable.ic__infant,
            "9-12 Months Vaccines Children",
            null,
            ChildImmunizationCategoriesFragmentDirections.actionChildImmunizationFragmentToChildImmunizationListFragment(
                ChildImmunizationCategory.MONTH_9_12
            )
        ),
        Icon(
            R.drawable.ic__infant,
            "16-24 Months Vaccines Children",
            null,
            ChildImmunizationCategoriesFragmentDirections.actionChildImmunizationFragmentToChildImmunizationListFragment(
                ChildImmunizationCategory.MONTH_16_24
            )
        ),
        Icon(
            R.drawable.ic__infant,
            "5-6 Years Vaccine Children",
            null,
            ChildImmunizationCategoriesFragmentDirections.actionChildImmunizationFragmentToChildImmunizationListFragment(
                ChildImmunizationCategory.YEAR_5_6
            )
        ),
        Icon(
            R.drawable.ic__infant,
            "10 Years Vaccine Children",
            null,
            ChildImmunizationCategoriesFragmentDirections.actionChildImmunizationFragmentToChildImmunizationListFragment(
                ChildImmunizationCategory.YEAR_10
            )
        ),
        Icon(
            R.drawable.ic__infant,
            "16 Years Vaccine Children",
            null,
            ChildImmunizationCategoriesFragmentDirections.actionChildImmunizationFragmentToChildImmunizationListFragment(
                ChildImmunizationCategory.YEAR_16
            )
        ),
        Icon(
            R.drawable.ic__infant,
            "Catch-Up Vaccines (Missing Vaccines)",
            null,
            ChildImmunizationCategoriesFragmentDirections.actionChildImmunizationFragmentToChildImmunizationListFragment(
                ChildImmunizationCategory.CATCH_UP
            )
        )
    )

}