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
            R.drawable.ic_person,
            "All\nBeneficiaries",
            iconCount.allBenCount,
            HomeFragmentDirections.actionNavHomeToAllBenFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Eligible\nCouple List",
            iconCount.eligibleCoupleCount,
            HomeFragmentDirections.actionNavHomeToEligibleCoupleFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Mother Care",
            0,
            HomeFragmentDirections.actionNavHomeToMotherCareFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Child Care",
            0,
            HomeFragmentDirections.actionNavHomeToChildCareFragment()
        ),
        Icon(R.drawable.ic_person, "NCD", 0, HomeFragmentDirections.actionNavHomeToNcdFragment()),
        Icon(
            R.drawable.ic_person,
            "Immunization Due List",
            0,
            HomeFragmentDirections.actionNavHomeToImmunizationDueFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "HRP Cases",
            0,
            HomeFragmentDirections.actionNavHomeToHrpCasesFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "General OP Care List",
            0,
            HomeFragmentDirections.actionNavHomeToGeneralOpCareFragment()
        ),
        Icon(
            R.drawable.ic_person, "Menopause Stage List",
            0,
            HomeFragmentDirections.actionNavHomeToMenopauseStageFragment()
        ),
        Icon(
            R.drawable.ic_person, "Death Reports",
            0,
            HomeFragmentDirections.actionNavHomeToDeathReportsFragment()
        ),
        Icon(
            R.drawable.ic_person, "Village Level Forms",
            0,
            HomeFragmentDirections.actionNavHomeToVillageLevelFormsFragment()
        ),
    )

    fun getChildCareDataset() = listOf(
        Icon(
            R.drawable.ic_person,
            "Infant List",
            0,
            ChildCareFragmentDirections.actionChildCareFragmentToInfantListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Child List",
            0,
            ChildCareFragmentDirections.actionChildCareFragmentToChildListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Adolescent List",
            0,
            ChildCareFragmentDirections.actionChildCareFragmentToAdolescentListFragment()
        )
    )

    fun getMotherCareDataset() = listOf(
        Icon(
            R.drawable.ic_person,
            "Pregnancy List",
            0,
            MotherCareFragmentDirections.actionMotherCareFragmentToPregnancyListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Delivery Stage List",
            0,
            MotherCareFragmentDirections.actionMotherCareFragmentToDeliveryStageListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "PNC Mother List",
            0,
            MotherCareFragmentDirections.actionMotherCareFragmentToPncMotherListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Reproductive Age List",
            0,
            MotherCareFragmentDirections.actionMotherCareFragmentToReproductiveAgeListFragment()
        )
    )

    fun getNCDDataset() = listOf(
        Icon(
            R.drawable.ic_person,
            "NCD List",
            0,
            NcdFragmentDirections.actionNcdFragmentToNcdListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "NCD Eligible List",
            0,
            NcdFragmentDirections.actionNcdFragmentToNcdEligibleListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "NCD Priority List",
            0,
            NcdFragmentDirections.actionNcdFragmentToNcdPriorityListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "NCD Non-Eligible List",
            0,
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