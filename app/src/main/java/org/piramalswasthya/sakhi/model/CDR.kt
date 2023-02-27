package org.piramalswasthya.sakhi.model

data class CDRCache (
    val benId: Long,
    val hhId: Long,
    var childName: String? = null,
    var dateOfBirth: Long = 0,
    var age: Int = 0,
    var visitDate: Long = 0,
    var gender: String? = null,
    var motherName: String? = null,
    var fatherName: String? = null,
    var address: String? = null,
    var houseNumber: String? = null,
    var mohalla: String? = null,
    var landmarks: String? = null,
    var pincode: Int = 0,
    var landline: Long = 0,
    var mobileNumber: Long = 0,
    var dateOfDeath: Long = 0,
    var timeOfDeath: Long = 0,
    var placeOfDeath: String? = null,
    var firstInformant: String? = null,
    var ashaSign: String? = null,
    var dateOfNotification: Long = 0
) {
    fun asPostModel(): CDRPost {
        return CDRPost(
            beneficiaryid = benId,
            houseoldId = hhId.toString()
        )
    }
}

data class CDRPost(
    val id: Int = 0,

    val beneficiaryid: Long = 0,

    val houseoldId: String? = null,

    val villageid: Int = 0,

    val syncstatus: Boolean? = null,

    val cdr_baby_name: String? = null,

    val cdr_dob: String? = null,

    val cdr_age: String? = null,

    val cdr_years: String? = null,

    val cdr_months: String? = null,

    val cdr_days: String? = null,

    val cdr_hours: String? = null,

    val cdr_male: String? = null,

    val cdr_female: String? = null,

    val cdr_mother: String? = null,

    val cdr_father: String? = null,

    val cdr_visit_date: String? = null,

    val cdr_address: String? = null,

    val cdr_house_no: String? = null,

    val cdr_colony: String? = null,

    val cdr_block: String? = null,

    val cdr_district: String? = null,

    val cdr_state: String? = null,

    val cdr_pincode: String? = null,

    val cdr_landmarks: String? = null,

    val cdr_landline: String? = null,

    val cdr_mobile: String? = null,

    val cdr_date_death: String? = null,

    val cdr_home: String? = null,

    val cdr_hospital: String? = null,

    val cdr_transit: String? = null,

    val cdr_hospital_name: String? = null,

    val cdr_name_informant: String? = null,

    val cdr_time: String? = null,

    val cdr_signature: String? = null,

    val cdr_notification: String? = null,

    val cdr_parent_signature: String? = null,

    val cdr_parent_designation: String? = null,

    val createdBy: String? = null,

    val createdDate: String? = null,

    val updatedBy: String? = null,

    val updatedDate: String? = null,

    val edit_flag: Boolean? = null,

    val latitude: Double? = 0.0,

    val longitude: Double? = 0.0,

    val cdr_parent_date: String? = null
)