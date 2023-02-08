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
            R.drawable.ic_person,
            "Eligible\nCouple List",
            iconCount.eligibleCoupleCount,
            HomeFragmentDirections.actionNavHomeToEligibleCoupleFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Mother Care",
            null,
            HomeFragmentDirections.actionNavHomeToMotherCareFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Child Care",
            null,
            HomeFragmentDirections.actionNavHomeToChildCareFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "NCD",
            null,
            HomeFragmentDirections.actionNavHomeToNcdFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Immunization Due List",
            iconCount.immunizationDueCount,
            HomeFragmentDirections.actionNavHomeToImmunizationDueFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "HRP Cases",
            null,
            HomeFragmentDirections.actionNavHomeToHrpCasesFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "General OP Care List",
            iconCount.generalOpCareCount,
            HomeFragmentDirections.actionNavHomeToGeneralOpCareFragment()
        ),
        Icon(
            R.drawable.ic_person, "Menopause Stage List",
            iconCount.menopauseCount,
            HomeFragmentDirections.actionNavHomeToMenopauseStageFragment()
        ),
        Icon(
            R.drawable.ic_person, "Death Reports",
            iconCount.deathReportCount,
            HomeFragmentDirections.actionNavHomeToDeathReportsFragment()
        ),
        Icon(
            R.drawable.ic_person, "Village Level Forms",
            null,
            HomeFragmentDirections.actionNavHomeToVillageLevelFormsFragment()
        ),
    )

    fun getChildCareDataset(iconCount: IconCount) = listOf(
        Icon(
            R.drawable.ic_person,
            "Infant List",
            iconCount.infantCount,
            ChildCareFragmentDirections.actionChildCareFragmentToInfantListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "Child List",
            iconCount.childCount,
            ChildCareFragmentDirections.actionChildCareFragmentToChildListFragment()
        ),
        Icon(
            R.drawable.ic_person,
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
            R.drawable.ic_person,
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
            R.drawable.ic_person,
            "NCD List",
            iconCount.ncdCount,
            NcdFragmentDirections.actionNcdFragmentToNcdListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "NCD Eligible List",
            iconCount.ncdEligibleCount,
            NcdFragmentDirections.actionNcdFragmentToNcdEligibleListFragment()
        ),
        Icon(
            R.drawable.ic_person,
            "NCD Priority List",
            iconCount.ncdPriorityCount,
            NcdFragmentDirections.actionNcdFragmentToNcdPriorityListFragment()
        ),
        Icon(
            R.drawable.ic_person,
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