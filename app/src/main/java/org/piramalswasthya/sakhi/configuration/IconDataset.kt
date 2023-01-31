package org.piramalswasthya.sakhi.configuration

import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.Icon
import org.piramalswasthya.sakhi.ui.home_activity.child_care.ChildCareFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.mother_care.MotherCareFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.NcdFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.village_level_forms.VillageLevelFormsFragmentDirections

object IconDataset {

    fun getIconDataset() = listOf(
        Icon(
            R.drawable.ic__hh,
            "All Household",
            HomeFragmentDirections.actionNavHomeToAllHouseholdFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "All Beneficiaries",
            HomeFragmentDirections.actionNavHomeToAllBenFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Eligible Couple List",
            HomeFragmentDirections.actionNavHomeToEligibleCoupleFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Mother Care",
            HomeFragmentDirections.actionNavHomeToMotherCareFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Child Care",
            HomeFragmentDirections.actionNavHomeToChildCareFragment()
        ),
        Icon(R.drawable.ic_person, "NCD", HomeFragmentDirections.actionNavHomeToNcdFragment()),
        Icon(
            R.drawable.ic_person,
            "Immunization Due List",
            HomeFragmentDirections.actionNavHomeToImmunizationDueFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "HRP Cases",
            HomeFragmentDirections.actionNavHomeToHrpCasesFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "General OP Care List",
            HomeFragmentDirections.actionNavHomeToGeneralOpCareFragment()
        ),
        Icon(
            R.drawable.ic_person, "Menopause Stage List",
            HomeFragmentDirections.actionNavHomeToMenopauseStageFragment()
        ),
        Icon(
            R.drawable.ic_person, "Death Reports",
            HomeFragmentDirections.actionNavHomeToDeathReportsFragment()
        ),
        Icon(
            R.drawable.ic_person, "Village Level Forms",
            HomeFragmentDirections.actionNavHomeToVillageLevelFormsFragment()
        ),
    )

    fun getChildCareDataset() = listOf(
        Icon(
            R.drawable.ic_person,
            "Infant List",
            ChildCareFragmentDirections.actionChildCareFragmentToInfantListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Child List",
            ChildCareFragmentDirections.actionChildCareFragmentToChildListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Adolescent List",
            ChildCareFragmentDirections.actionChildCareFragmentToAdolescentListFragment()
        )
    )

    fun getMotherCareDataset() = listOf(
        Icon(
            R.drawable.ic_person,
            "Pregnancy List",
            MotherCareFragmentDirections.actionMotherCareFragmentToPregnancyListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Delivery Stage List",
            MotherCareFragmentDirections.actionMotherCareFragmentToDeliveryStageListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "PNC Mother List",
            MotherCareFragmentDirections.actionMotherCareFragmentToPncMotherListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Reproductive Age List",
            MotherCareFragmentDirections.actionMotherCareFragmentToReproductiveAgeListFragment()
        )
    )

    fun getNCDDataset() = listOf(
        Icon(
            R.drawable.ic_person,
            "NCD List",
            NcdFragmentDirections.actionNcdFragmentToNcdListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "NCD Eligible List",
            NcdFragmentDirections.actionNcdFragmentToNcdEligibleListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "NCD Priority List",
            NcdFragmentDirections.actionNcdFragmentToNcdPriorityListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "NCD Non-Eligible List",
            NcdFragmentDirections.actionNcdFragmentToNcdNonEligibleListFragment()
        )
    )

    fun getVillageLevelFormsDataset() = listOf(
        Icon(
            R.drawable.ic_person,
            "Survey Register",
            VillageLevelFormsFragmentDirections.actionVillageLevelFormsFragmentToSurveyRegisterFragment()
        )
    )
}