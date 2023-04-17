package org.piramalswasthya.sakhi.configuration

import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.model.Icon
import org.piramalswasthya.sakhi.repositories.RecordsRepo
import org.piramalswasthya.sakhi.ui.home_activity.child_care.ChildCareFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.home.HomeFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.immunization_due.ImmunizationDueTypeFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.mother_care.MotherCareFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.non_communicable_disease.NcdFragmentDirections
import org.piramalswasthya.sakhi.ui.home_activity.village_level_forms.VillageLevelFormsFragmentDirections
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IconDataset @Inject constructor(private val recordsRepo: RecordsRepo) {

    fun getIconDataset() = listOf(
        Icon(
            R.drawable.ic__hh,
            "All\nHousehold",
            recordsRepo.hhCount,
            HomeFragmentDirections.actionNavHomeToAllHouseholdFragment()
        ),
        Icon(
            R.drawable.ic__ben,
            "All\nBeneficiaries",
            recordsRepo.benListCount,
            HomeFragmentDirections.actionNavHomeToAllBenFragment()
        ),
        Icon(
            R.drawable.ic__eligible_couple,
            "Eligible\nCouple List",
            recordsRepo.eligibleCoupleListCount,
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
            null,
            HomeFragmentDirections.actionNavHomeToImmunizationDueFragment()
        ),
        Icon(
            R.drawable.ic__hrp,
            "HRP Cases",
            recordsRepo.hrpListCount,
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
            recordsRepo.menopauseListCount,
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

    fun getChildCareDataset() = listOf(
        Icon(
            R.drawable.ic_infant,
            "Infant List",
            recordsRepo.infantListCount,
            ChildCareFragmentDirections.actionChildCareFragmentToInfantListFragment()
        ),
        Icon(
            R.drawable.ic__child,
            "Child List",
            recordsRepo.childListCount,
            ChildCareFragmentDirections.actionChildCareFragmentToChildListFragment()
        ),
        Icon(
            R.drawable.ic__adolescent,
            "Adolescent List",
            recordsRepo.adolescentListCount,
            ChildCareFragmentDirections.actionChildCareFragmentToAdolescentListFragment()
        )
    )

    fun getMotherCareDataset() = listOf(
        Icon(
            R.drawable.ic__pregnancy,
            "Pregnancy List",
            recordsRepo.pregnantListCount,
            MotherCareFragmentDirections.actionMotherCareFragmentToPregnancyListFragment()
        ),
        Icon(
            R.drawable.ic__delivery,
            "Delivery Stage List",
            recordsRepo.deliveryListCount,
            MotherCareFragmentDirections.actionMotherCareFragmentToDeliveryStageListFragment()
        ),
        Icon(
            R.drawable.ic__pnc,
            "PNC Mother List",
            recordsRepo.pncMotherListCount,
            MotherCareFragmentDirections.actionMotherCareFragmentToPncMotherListFragment()
        ),
        Icon(
            R.drawable.ic__reproductive_age,
            "Reproductive Age List",
            recordsRepo.reproductiveAgeListCount,
            MotherCareFragmentDirections.actionMotherCareFragmentToReproductiveAgeListFragment()
        )
    )

    fun getNCDDataset() = listOf(
        Icon(
            R.drawable.ic__ncd_list,
            "NCD List",
            recordsRepo.ncdListCount,
            NcdFragmentDirections.actionNcdFragmentToNcdListFragment()
        ),
        Icon(
            R.drawable.ic__ncd_eligibility,
            "NCD Eligible List",
            recordsRepo.ncdEligibleListCount,
            NcdFragmentDirections.actionNcdFragmentToNcdEligibleListFragment()
        ),
        Icon(
            R.drawable.ic__ncd_priority,
            "NCD Priority List",
            recordsRepo.ncdPriorityListCount,
            NcdFragmentDirections.actionNcdFragmentToNcdPriorityListFragment()
        ),
        Icon(
            R.drawable.ic_ncd_noneligible,
            "NCD Non-Eligible List",
            recordsRepo.ncdNonEligibleListCount,
            NcdFragmentDirections.actionNcdFragmentToNcdNonEligibleListFragment()
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


    fun getVillageLevelFormsDataset() = listOf(
        Icon(
            R.drawable.ic_person,
            "Survey Register",
            null,
            VillageLevelFormsFragmentDirections.actionVillageLevelFormsFragmentToSurveyRegisterFragment()
        )
    )

}