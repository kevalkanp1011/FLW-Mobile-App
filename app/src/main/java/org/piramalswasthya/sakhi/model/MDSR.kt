package org.piramalswasthya.sakhi.model

data class MDSRCache (
    val benId: Long,
    val hhId: Long,
    var dateOfDeath: Long? = System.currentTimeMillis(),
    var address: String? = null,
    var husbandName: String? = null,
    var causeOfDeath: Int = 0,
    var reasonOfDeath: String? = null,
    var investigationDate: Long? = System.currentTimeMillis(),
    var actionTaken: Int = 0,
    var blockMOSign: String? = null,
    var date: Long = 0
) {
    fun asPostModel(): MdsrPost {
        return MdsrPost(
            beneficiaryid = benId,
            houseoldId = hhId.toString()
        )
    }
}

data class MdsrPost(

    val id: Int = 0,

    val beneficiaryid: Long,

    val houseoldId: String,

    val villageid: Int? = 0,

    val syncstatus: Boolean? = null,

    val latitude: Double? = 0.0,

    val longitude: Double? = 0.0,

    val mdsr_district: String? = null,

    val mdsr_state: String? = null,

    val mdsr_month: String? = null,

    val mdsr_year: String? = null,

    val mdsr_name_of_deceased: String? = null,

    val mdsr_age: String? = null,

    val mdsr_date_of_deceased: String? = null,

    val mdsr_address: String? = null,

    val mdsr_husband_name: String? = null,

    val mdsr_cause_of_death: Int? = 0,

    val mdsr_reason_death: String? = null,

    val mdsr_field_investigation: String? = null,

    val mdsr_action: Int? = 0,

    val mdsr_signature: String? = null,

    val mdsr_date_ic: String? = null,

    val createdBy: String? = null,

    val createdDate: String? = null,

    val updatedBy: String? = null,

    val updatedDate: String? = null,

    val edit_flag: Boolean? = null,
)