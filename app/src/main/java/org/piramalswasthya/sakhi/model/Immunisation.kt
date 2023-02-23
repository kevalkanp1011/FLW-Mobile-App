package org.piramalswasthya.sakhi.model

import androidx.room.PrimaryKey

data class ImmunisationCache (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val benId: Long,

    val hhId: Long,

    var motherName: String? = null,

    var dateOfBirth: Long = 0,

    var dateOfPrevVaccination: Long = 0,

    var numDoses: String? = null,

    var vaccineName: String? = null,

    var doseNumber: String? = null,

    var expectedDate: String? = null,

    var dateOfCurrentVaccination: String? = null,

    var vaccinatedAt: String? = null,

    var vaccinatedBy: String? = null,


)