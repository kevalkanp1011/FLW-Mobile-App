package org.piramalswasthya.sakhi.configuration

import android.content.Context
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.BenRegCache
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.FormInput.InputType
import org.piramalswasthya.sakhi.model.HouseholdCache
import java.text.SimpleDateFormat
import java.util.*

class BenKidRegFormDataset(context: Context, private val household: HouseholdCache? = null) {

    companion object {
        private fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val mdFormat =
                SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            return mdFormat.format(calendar.time)
        }
        private fun getLongFromDate(dateString : String): Long {
            val f = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val date = f.parse(dateString)
            return date?.time ?: throw IllegalStateException("Invalid date for dateReg")
            }
    }

    val firstPage : List<FormInput> by lazy {
        listOf(
//            FormInput(
//                inputType = InputType.TEXT_VIEW,
//                title = "Date of Registration",
//                value = getCurrentDate(),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "First Name",
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "Last Name / Surname",
//                required = false,
//            ),
//            FormInput(
//                inputType = InputType.DATE_PICKER,
//                title = "Date of Birth",
//                required = true,
//                hiddenField = FormInput(
//                    inputType = InputType.TEXT_VIEW,
//                    title = "Age",
//                    required = true,
//                    hiddenField = FormInput(
//                        inputType = InputType.TEXT_VIEW,
//                        title = "Age Unit",
//                        required = true,
//                    )
//                )
//            ),
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Gender",
//                list = listOf(
//                    "Male",
//                    "Female",
//                    "Transgender"
//                ),
//                required = true,
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "Father's Name",
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "Mother's Name",
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Relation with family head",
//                list = listOf(
//                    "Mother",
//                    "Father",
//                    "Brother",
//                    "Sister",
//                    "Wife",
//                    "Husband",
//                    "Nephew",
//                    "Niece",
//                    "Son",
//                    "Daughter",
//                    "Grand Father",
//                    "Grand Mother",
//                    "Father in Law",
//                    "Mother in Law",
//                    "Grand Son",
//                    "Grand Daughter",
//                    "Son in Law",
//                    "Daughter in Law",
//                    "Self",
//                    "Other"
//                ),
//                required = true,
//                hiddenFieldTrigger = "Any",
//                hiddenField = FormInput(
//                    inputType = InputType.EDIT_TEXT,
//                    title = "Mobile Number",
//                    required = true,
//                    etLength = 10,
//                    etInputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL
//                )
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "Community",
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "Religion",
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "RCH ID",
//                required = false
//            ),
//
            )
    }

    val secondPage : List<FormInput> by lazy {
        listOf(
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Place of birth",
//                list = listOf(
//                    "Home",
//                    "Health Facility",
//                    "Any other Place"
//                ),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Facility Selection",
//                list = listOf(
//                    "Sub Centre",
//                    "PHC",
//                    "CHC",
//                    "Sub District Hospital",
//                    "District Hospital",
//                    "Medical College Hospital",
//                    "In Transit",
//                    "Private Hospital",
//                    "Accredited Private Hospital",
//                    "Other",
//                ),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "Other Place of Birth",
//                required = true
//            ),
//
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Who Conducted Delivery",
//                list = listOf(
//                    "ANM",
//                    "LHV",
//                    "Doctor",
//                    "Staff Nurse",
//                    "Relative",
//                    "TBA(Non-Skilled Birth Attendant)",
//                    "Other",
//                ),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "Other - Enter who Conducted Delivery",
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Type of Delivery",
//                listOf(
//                    "Normal Delivery",
//                    "C - Section",
//                    "Assisted"
//                ),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Complications during delivery",
//                listOf(
//                    "PPH",
//                    "Retained Placenta",
//                    "Obstructed Delivery",
//                    "Prolapsed cord",
//                    "Death",
//                    "None",
//                ),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Breast feeding started within 1 hr of birth",
//                listOf(
//                    "Yes",
//                    "No",
//                    "Don't Know"
//                ),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Birth Dose",
//                listOf(
//                    "Given",
//                    "Not Given",
//                    "Don't Know"
//                ),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Term",
//                listOf(
//                    "Full-Term",
//                    "Pre-Term",
//                    "Don't Know"
//                ),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Baby cried immediately at birth",
//                listOf(
//                    "Yes",
//                    "No",
//                    "Don't Know"
//                ),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.DROPDOWN,
//                title = "Any Defect Seen at Birth",
//                listOf(
//                    "Yes",
//                    "Cleft Lip-Cleft Palate",
//                    "Neural Tube defect(Spina Bifida)",
//                    "Club Foot",
//                    "Hydrocephalus",
//                    "Imperforate Anus",
//                    "Downs Syndrome",
//                    "None"
//                ),
//                required = true
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "Height at birth ( cm )",
//                required = false
//            ),
//            FormInput(
//                inputType = InputType.EDIT_TEXT,
//                title = "Weight at birth (gram )",
//                required = false
//            ),
//

        )
    }

    fun getBenForFirstPage(userId: Int, hhId: Long): BenRegCache {

        val ben = BenRegCache(
            householdId = hhId,
            ashaId = userId,
            syncState = SyncState.UNSYNCED,
            isDraft = true
        )
        ben.apply {
//            regDate = getLongFromDate(firstPage[0].value!!)
//            firstName = firstPage[1].value
//            lastName = firstPage[2].value
//            dob = getLongFromDate(firstPage[3].value!!)
//            age = firstPage[3].hiddenField?.value?.toInt()?:0
//            age_unit = firstPage[3].hiddenField?.hiddenField?.value
//            gender = firstPage[4].value
//            fatherName = firstPage[5].value
//            motherName = firstPage[6].value
//            familyHeadRelation = firstPage[7].value
//            mobileNoOfRelation = firstPage[7].hiddenField?.value
//            community = firstPage[8].value
//            religion = firstPage[9].value
//            rchId = firstPage[10].value

        }
        return ben
    }

    fun getBenForSecondPage(userId: Int, hhId: Long): BenRegCache {

        val ben = getBenForFirstPage(userId, hhId)
        ben.apply {

//            facilitySelection = secondPage[1].value
//            whoConductedDelivery = secondPage[3].value
            isDraft = false

        }

        return ben
    }
}