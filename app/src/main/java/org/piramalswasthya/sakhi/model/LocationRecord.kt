package org.piramalswasthya.sakhi.model

data class LocationRecord (
    val stateId : Int,
    val state :String,
    val districtId : Int,
    val district :String,
    val blockId : Int,
    val block :String,
    val villageId : Int,
    val village :String,

    val countryId : Int
)