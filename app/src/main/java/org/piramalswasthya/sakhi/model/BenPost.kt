package org.piramalswasthya.sakhi.model

import com.squareup.moshi.Json


data class BenPost(
    @Json(name = "houseoldId")
    var houseoldId: String,

    var benficieryid: Long,

    @Json(name = "ashaid")
    var ashaid: Int = 0,

    @Json(name = "registrationDate")
    var registrationDate: String? = null,

    @Json(name = "age")
    var age: Int = 0,

    @Json(name = "age_unit")
    var age_unit: String? = null,

    @Json(name = "age_unitId")
    var age_unitId: Int = 0,

    @Json(name = "marriageDate")
    var marriageDate: String? = null,

    @Json(name = "mobilenoofRelation")
    var mobilenoofRelation: String? = null,

    @Json(name = "mobilenoofRelationId")
    var mobilenoofRelationId: Int = 0,

    @Json(name = "mobileOthers")
    var mobileOthers: String? = null,

    @Json(name = "literacy")
    var literacy: String? = null,

    @Json(name = "literacyId")
    var literacyId: Int = 0,

    @Json(name = "religionOthers")
    var religionOthers: String? = null,

    @Json(name = "rchid")
    var rchid: String? = null,

    @Json(name = "registrationType")
    var registrationType: String? = null,

    @Json(name = "latitude")
    var latitude: Double = 0.0,

    @Json(name = "longitude")
    var longitude: Double = 0.0,

    //Bank details
    @Json(name = "aadha_no")
    var aadha_no: String? = null,

    @Json(name = "aadha_noId")
    var aadha_noId: Int = 0,

    @Json(name = "aadhaNo")
    var aadhaNo: String? = null,

    @Json(name = "need_opcare")
    var need_opcare: String? = null,

    @Json(name = "need_opcareId")
    var need_opcareId: Int = 0,

    //Menstrual details
    @Json(name = "menstrualStatusId")
    var menstrualStatusId: Int = 0,

    @Json(name = "regularityofMenstrualCycleId")
    var regularityofMenstrualCycleId: Int = 0,

    @Json(name = "lengthofMenstrualCycleId")
    var lengthofMenstrualCycleId: Int = 0,

    @Json(name = "menstrualBFDId")
    var menstrualBFDId: Int = 0,

    @Json(name = "menstrualProblemId")
    var menstrualProblemId: Int = 0,

    @Json(name = "lastMenstrualPeriod")
    var lastMenstrualPeriod: String? = null,

    @Json(name = "reproductiveStatus")
    var reproductiveStatus: String? = null,

    @Json(name = "reproductiveStatusId")
    var reproductiveStatusId: Int = 0,

    @Json(name = "noOfDaysForDelivery")
    var noOfDaysForDelivery: Int? = null,

    @Json(name = "formStatus")
    var formStatus: String? = null,

    @Json(name = "formType")
    var formType: String? = null,

    //these are new fields of new born registration
    @Json(name = "childRegisteredAWCID")
    var childRegisteredAWCID: Int = 0,

    @Json(name = "childRegisteredSchoolID")
    var childRegisteredSchoolID: Int = 0,

    @Json(name = "TypeofSchoolID")
    var typeofSchoolID: Int = 0,

    //strings
    @Json(name = "childRegisteredAWC")
    var childRegisteredAWC: String? = null,

    @Json(name = "childRegisteredSchool")
    var childRegisteredSchool: String? = null,

    @Json(name = "TypeofSchool")
    var typeofSchool: String? = null,

    //these are new fields of registration for asha login
    @Json(name = "PreviousLiveBirth")
    var previousLiveBirth: String? = null,

    @Json(name = "LastDeliveryConductedID")
    var lastDeliveryConductedID: Int = 0,

    @Json(name = "WhoConductedDeliveryID")
    var whoConductedDeliveryID: Int = 0,

    @Json(name = "FamilyHeadRelation")
    var familyHeadRelation: String? = null,

    @Json(name = "FamilyHeadRelationPosition")
    var familyHeadRelationPosition: Int = 0,

    //strings
    @Json(name = "WhoConductedDelivery")
    var whoConductedDelivery: String? = null,

    @Json(name = "LastDeliveryConducted")
    var lastDeliveryConducted: String? = null,

    @Json(name = "facilitySelection")
    var facilitySelection: String? = null,

    @Json(name = "ServerUpdatedStatus")
    var serverUpdatedStatus: Int = 0,

    @Json(name = "createdBy")
    var createdBy: String? = null,

    @Json(name = "createdDate")
    var createdDate: String? = null,

    @Json(name = "ncd_priority")
    var ncd_priority: Int = 0,

    @Json(name = "guidelineId")
    var guidelineId: String? = null,

    @Json(name = "villagename")
    var villagename: String? = null,

    @Json(name = "VanID")
    var vanID: Int = 0,

    @Json(name = "Countyid")
    var countyid: Int = 0,

    @Json(name = "ProviderServiceMapID")
    var providerServiceMapID: Int = 0,

    @Json(name = "Processed")
    var processed: String? = null,

    @Json(name = "currSubDistrictId")
    var currSubDistrictId: Int = 0,

    @Json(name = "expectedDateOfDelivery")
    var expectedDateOfDelivery: String? = null,

    @Json(name = "hrpStatus")
    var isHrpStatus: Boolean = false,

    @Json(name = "menstrualStatus")
    var menstrualStatus: String? = null,

    @Json(name = "ageAtMarriage")
    var ageAtMarriage: Int = 0,

    @Json(name = "dateMarriage")
    var dateMarriage: String? = null,

    @Json(name = "deliveryDate")
    var deliveryDate: String? = null,

    @Json(name = "suspected_hrp")
    var suspected_hrp: String? = null,

    @Json(name = "suspected_ncd")
    var suspected_ncd: String? = null,

    @Json(name = "suspected_tb")
    var suspected_tb: String? = null,

    @Json(name = "suspected_ncd_diseases")
    var suspected_ncd_diseases: String? = null,

    @Json(name = "confirmed_ncd")
    var confirmed_ncd: String? = null,

    @Json(name = "confirmed_hrp")
    var confirmed_hrp: String? = null,

    @Json(name = "confirmed_tb")
    var confirmed_tb: String? = null,

    @Json(name = "confirmed_ncd_diseases")
    var confirmed_ncd_diseases: String? = null,

    @Json(name = "diagnosis_status")
    var diagnosis_status: String? = null,

    @Json(name = "nishchayPregnancyStatus")
    var nishchayPregnancyStatus: String? = null,

    @Json(name = "nishchayPregnancyStatusPosition")
    var nishchayPregnancyStatusPosition: Int = 0,

    @Json(name = "nishchayDeliveryStatus")
    var nishchayDeliveryStatus: String? = null,

    @Json(name = "nishchayDeliveryStatusPosition")
    var nishchayDeliveryStatusPosition: Int = 0,

    @Json(name = "nayiPahalDeliveryStatus")
    var nayiPahalDeliveryStatus: String? = null,

    @Json(name = "nayiPahalDeliveryStatusPosition")
    var nayiPahalDeliveryStatusPosition: Int = 0,

    @Json(name = "immunizationStatus")
    var isImmunizationStatus: Boolean = false
)
