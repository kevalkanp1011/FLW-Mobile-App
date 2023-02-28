package org.piramalswasthya.sakhi.configuration

import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.Icon
import org.piramalswasthya.sakhi.model.IconCount
import org.piramalswasthya.sakhi.ui.home_activity.child_care.ChildCareFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.mother_care.MotherCareFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.NcdFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.village_level_forms.VillageLevelFormsFragmentDirections

object IconDataset {

    fun getIconDataset(iconCount: IconCount) = listOf(
        Icon(
            R.drawable.ic__hh,
            "All\nHousehold",
            iconCount.householdCount,
            HomeFragmentDirections.actionNavHomeToAllHouseholdFragment()
        ),
        Icon(
            R.drawable.ic__ben,
            "All\nBeneficiaries",
            iconCount.allBenCount,
            HomeFragmentDirections.actionNavHomeToAllBenFragment()
        ),
        Icon(
            R.drawable.ic__eligible_couple,
            "Eligible\nCouple List",
            iconCount.eligibleCoupleCount,
            HomeFragmentDirections.actionNavHomeToEligibleCoupleFragment()
        ),
        Icon(
            R.drawable.ic__mother_care,
            "Mother Care",
            null,
            HomeFragmentDirections.actionNavHomeToMotherCareFragment()
        ),
        Icon(
            R.drawable.ic__child_care,
            "Child Care",
            null,
            HomeFragmentDirections.actionNavHomeToChildCareFragment()
        ),
        Icon(
            R.drawable.ic__ncd,
            "NCD",
            null,
            HomeFragmentDirections.actionNavHomeToNcdFragment()
        ),
        Icon(
            R.drawable.ic__immunization,
            "Immunization Due List",
            iconCount.immunizationDueCount,
            HomeFragmentDirections.actionNavHomeToImmunizationDueFragment()
        ),
        Icon(
            R.drawable.ic__hrp,
            "HRP Cases",
            iconCount.hrpCount,
            HomeFragmentDirections.actionNavHomeToHrpCasesFragment()
        ),
        Icon(
            R.drawable.ic__general_op,
            "General OP Care List",
            null,
            HomeFragmentDirections.actionNavHomeToGeneralOpCareFragment()
        ),
        Icon(
            R.drawable.ic__menopause,
            "Menopause Stage List",
            iconCount.menopauseCount,
            HomeFragmentDirections.actionNavHomeToMenopauseStageFragment()
        ),
        Icon(
            R.drawable.ic__death, "Death Reports",
            null,
            HomeFragmentDirections.actionNavHomeToDeathReportsFragment()
        ),
        Icon(
            R.drawable.ic__village_level_form, "Village Level Forms",
            null,
            HomeFragmentDirections.actionNavHomeToVillageLevelFormsFragment()
        ),
    )

    fun getChildCareDataset(iconCount: IconCount) = listOf(
        Icon(
            R.drawable.ic_infant,
            "Infant List",
            iconCount.infantCount,
            ChildCareFragmentDirections.actionChildCareFragmentToInfantListFragment()
        ),
        Icon(
            R.drawable.ic__child,
            "Child List",
            iconCount.childCount,
            ChildCareFragmentDirections.actionChildCareFragmentToChildListFragment()
        ),
        Icon(
            R.drawable.ic__adolescent,
            "Adolescent List",
            iconCount.adolescentCount,
            ChildCareFragmentDirections.actionChildCareFragmentToAdolescentListFragment()
        )
    )

    fun getMotherCareDataset(iconCount: IconCount) = listOf(
        Icon(
            R.drawable.ic__pregnancy,
            "Pregnancy List",
            iconCount.pregnantCount,
            MotherCareFragmentDirections.actionMotherCareFragmentToPregnancyListFragment()
        ),
        Icon(
            R.drawable.ic__delivery,
            "Delivery Stage List",
            iconCount.deliveryStageCount,
            MotherCareFragmentDirections.actionMotherCareFragmentToDeliveryStageListFragment()
        ),
        Icon(
            R.drawable.ic__pnc,
            "PNC Mother List",
            iconCount.pncMotherCount,
            MotherCareFragmentDirections.actionMotherCareFragmentToPncMotherListFragment()
        ),
        Icon(
            R.drawable.ic__reproductive_age,
            "Reproductive Age List",
            iconCount.reproductiveAgeCount,
            MotherCareFragmentDirections.actionMotherCareFragmentToReproductiveAgeListFragment()
        )
    )

    fun getNCDDataset(iconCount: IconCount) = listOf(
        Icon(
            R.drawable.ic__ncd_list,
            "NCD List",
            iconCount.ncdCount,
            NcdFragmentDirections.actionNcdFragmentToNcdListFragment()
        ),
        Icon(
            R.drawable.ic__ncd_eligibility,
            "NCD Eligible List",
            iconCount.ncdEligibleCount,
            NcdFragmentDirections.actionNcdFragmentToNcdEligibleListFragment()
        ),
        Icon(
            R.drawable.ic__ncd_priority,
            "NCD Priority List",
            iconCount.ncdPriorityCount,
            NcdFragmentDirections.actionNcdFragmentToNcdPriorityListFragment()
        ),
        Icon(
            R.drawable.ic_ncd_noneligible,
            "NCD Non-Eligible List",
            iconCount.ncdNonEligibleCount,
            NcdFragmentDirections.actionNcdFragmentToNcdNonEligibleListFragment()
        )
    )

    fun getVillageLevelFormsDataset() = listOf(
        Icon(
            R.drawable.ic_person,
            "Survey Register",
            0,
            VillageLevelFormsFragmentDirections.actionVillageLevelFormsFragmentToSurveyRegisterFragment()
        )
    )
}