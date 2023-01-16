package org.piramalswasthya.sakhi.configuration

import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.Icon
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeFragmentDirections

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
}