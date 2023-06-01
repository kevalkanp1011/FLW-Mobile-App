package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.helpers.Languages
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.FormElement
import org.piramalswasthya.sakhi.model.ImmunizationCache
import org.piramalswasthya.sakhi.model.InputType
import org.piramalswasthya.sakhi.model.Vaccine

class ImmunizationDataset(context: Context, language: Languages) : Dataset(context, language) {

    private val motherName = FormElement(
        id = 101,
        inputType = InputType.TEXT_VIEW,
        title = "Mother's Name",
        required = false
    )
    private val dateOfBirth = FormElement(
        id = 102,
        inputType = InputType.TEXT_VIEW,
        title = "Date Of Birth",
        required = false
    )

    //    private val dateOfPrevVaccination = FormElement(
//        id = 103,
//        inputType = InputType.EDIT_TEXT,
//        title = "Date of vaccination",
//        required = false
//    )
//    private val numDoses = FormElement(
//        id = 104,
//        inputType = InputType.EDIT_TEXT,
//        title = "No. of Doses Taken",
//        required = false
//    )
    private val vaccineName = FormElement(
        id = 105,
        inputType = InputType.TEXT_VIEW,
        title = "Vaccine Name",
        required = false
    )
    private val doseNumber = FormElement(
        id = 106,
        inputType = InputType.TEXT_VIEW,
        title = "Dose Number",
        required = false
    )
    private val expectedDate = FormElement(
        id = 107,
        inputType = InputType.TEXT_VIEW,
        title = "Expected Date",
        required = false
    )
    private val dateOfVaccination = FormElement(
        id = 108,
        inputType = InputType.EDIT_TEXT,
        title = "Date of Vaccination",
        required = false
    )
    private val vaccinatedPlace = FormElement(
        id = 109,
        inputType = InputType.EDIT_TEXT,
        title = "Vaccinated Place",
        required = false
    )
    private val vaccinatedBy = FormElement(
        id = 110,
        inputType = InputType.EDIT_TEXT,
        title = "Vaccinated By",
        required = false
    )

    suspend fun setFirstPage(ben: BenRegCache, vaccine: Vaccine, imm: ImmunizationCache?) {
        val list = listOf(
            motherName,
            dateOfBirth,
            vaccineName,
            doseNumber,
            expectedDate,
            dateOfVaccination,
            vaccinatedPlace,
            vaccinatedBy
        )
        motherName.value = ben.motherName
        dateOfBirth.value = getDateFromLong(ben.dob)
        vaccineName.value = vaccine.name
        doseNumber.value = vaccine.dosage.toString()
        expectedDate.value = getDateFromLong(ben.dob + vaccine.dueDuration)

        imm?.let { saved ->
            dateOfVaccination.value = getDateFromLong(saved.date)
            vaccinatedPlace.value = vaccinatedPlace.getStringFromPosition(saved.placeId)
            vaccinatedBy.value = vaccinatedBy.getStringFromPosition(saved.byWhoId)
        }
        setUpPage(list)
    }

    override suspend fun handleListOnValueChanged(formId: Int, index: Int) = -1

    override fun mapValues(cacheModel: FormDataModel, pageNumber: Int) {

    }

}