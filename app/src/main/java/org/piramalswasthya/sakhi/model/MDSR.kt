package org.piramalswasthya.sakhi.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class MDSR (
    var dateOfDeath: Long = 0,
    var address: String? = null,
    var husbandName: String? = null,
    var causeOfDeath: String? = null,
    var investigationDate: String? = null,
    var actionTaken: Boolean? = null,
    var blockMOSign: String? = null,
    var date: Long = 0
)

data class MdsrRegistration(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(index = true)
    var benId: Long,

    @ColumnInfo(index = true)
    var hhId: Long,

    var villageid: Int? = 0,

    var syncstatus: Boolean? = null,

    var latitude: Double? = 0.0,

    var longitude: Double? = 0.0,

    var mdsr_district: String? = null,

    var mdsr_state: String? = null,

    var mdsr_month: String? = null,

    var mdsr_year: String? = null,

    var mdsr_name_of_deceased: String? = null,

    var mdsr_age: String? = null,

    var mdsr_date_of_deceased: String? = null,

    var mdsr_address: String? = null,

    var mdsr_husband_name: String? = null,

    var mdsr_cause_of_death: Int? = 0,

    var mdsr_reason_death: String? = null,

    var mdsr_field_investigation: String? = null,

    var mdsr_action: Int? = 0,

    var mdsr_signature: String? = null,

    var mdsr_date_ic: String? = null,

    var createdBy: String? = null,

    var createdDate: String? = null,

    var updatedBy: String? = null,

    var updatedDate: String? = null,

    var edit_flag: Boolean? = null,
)