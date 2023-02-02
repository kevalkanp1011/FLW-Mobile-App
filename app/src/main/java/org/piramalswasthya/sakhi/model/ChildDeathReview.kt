package org.piramalswasthya.sakhi.model

data class childDeathReviewCache (
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
)