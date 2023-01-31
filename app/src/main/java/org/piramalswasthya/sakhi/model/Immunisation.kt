package org.piramalswasthya.sakhi.model

data class ImmunisationCache (

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