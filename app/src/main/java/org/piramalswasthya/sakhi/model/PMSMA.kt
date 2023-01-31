package org.piramalswasthya.sakhi.model

data class PMSMACache (

    var mctsNumberOrRchNumber: String? = null,

    var haveMCPCard: Boolean = false,

    var husbandName: String? = null,

    var address: String? = null,

    var mobileNumber: Long = 0,

    var numANC: Int = 0,

    var weight: Int = 0,

    var systolicBloodPressure: String? = null,

    var bloodPressure: String? = null,

    var abdominalCheckUp: String? = null,

    var fetalHRPM: Int = 0,

    var twinPregnancy: Boolean = false,

    var urineAlbumin: String? = null,

    var haemoglobinAndBloodGroup: String? = null,

    var hiv: String? = null,

    var vdrl: String? = null,

    var hbsc: String? = null,

    var malaria: String? = null,

    var hivTestDuringANC: Boolean? = null,

    var swollenCondtion: Boolean? = null,

    var bloodSugarTest: Boolean? = null,

    var ultraSound: Boolean? = null,

    var ironFolicAcid: Boolean? = null,

    var calciumSupplementation: Boolean? = null,

    var tetanusToxoid: Boolean? = null,

    var lastMenstrualPeriod: Long = 0,

    var expectedDateOfDelivery: Long = 0,

    var highriskSymbols: Boolean? = null,

    var highRiskReason: String? = null,

    var highRiskPregnant: Boolean? = null,

    var highRiskPregnancyReferred: Boolean? = null,

    var birthPrepAndNutritionAndFamilyPlanning: Boolean? = null,

    var medicalOfficerSign: String? = null,
        )