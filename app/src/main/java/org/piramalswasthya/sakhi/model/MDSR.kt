package org.piramalswasthya.sakhi.model

data class MDSRCache (
    var dateOfDeath: Long = 0,
    var address: String? = null,
    var husbandName: String? = null,
    var causeOfDeath: String? = null,
    var investigationDate: String? = null,
    var actionTaken: Boolean? = null,
    var blockMOSign: String? = null,
    var date: Long = 0
)