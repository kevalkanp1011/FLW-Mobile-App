package org.piramalswasthya.sakhi.model

import androidx.room.PrimaryKey

data class NewBornRegNetwork(
    var id: Int = 0,
    @PrimaryKey
    var benficieryid: Long = 0,
    var childName: String? = null,
    var birthPlace: String? = null,
    var birthPlaceid: Int = 0,
    var facilityName: String? = null,
    var facilityid: Int = 0,
    var ashaid: Int = 0,
    var facilityOther: String? = null,
    var placeName: String? = null,
    var conductedDelivery: String? = null,
    var conductedDeliveryid: Int = 0,
    var conductedDeliveryOther: String? = null,
    var deliveryType: String? = null,
    var deliveryTypeid: Int = 0,
    var complecations: String? = null,
    var complecationsid: Int = 0,
    var complicationsOther: String? = null,
    var term: String? = null,
    var termid: Int = 0,
    var gestationalAge: String? = null,
    var gestationalAgeid: Int = 0,
    var corticosteroidGivenMother: String? = null,
    var corticosteroidGivenMotherid: Int = 0,
    var criedImmediately: String? = null,
    var criedImmediatelyid: Int = 0,
    var birthDefects: String? = null,
    var birthDefectsid: Int = 0,
    var birthDefectsOthers: String? = null,
    var heightAtBirth: Int = 0,
    var weightAtBirth: Float = 0F,
    var feedingStarted: String? = null,
    var feedingStartedid: Int = 0,
    var birthDosage: String? = null,
    var birthDosageid: Int = 0,
    var opvBatchNo: String? = null,
    var opvGivenDueDate: String? = null,
    var opvDate: String? = null,
    var bcdBatchNo: String? = null,
    var bcgGivenDueDate: String? = null,
    var bcgDate: String? = null,
    var hptdBatchNo: String? = null,
    var hptGivenDueDate: String? = null,
    var hptDate: String? = null,
    var vitaminkBatchNo: String? = null,
    var vitaminkGivenDueDate: String? = null,
    var vitaminkDate: String? = null,
    var createdBy: String? = null,
    var createdDate: String? = null,
    var serverUpdatedStatus: Int = 0,
    var updatedBy: String? = null,
    var updatedDate: String? = null,
    var deliveryTypeOther: String? = null,
    //private int BenRegId;//Commenting by AHex,
    var providerServiceMapID: Int = 0,
    var vanID: Int = 0,
    var processed: String? = null,
    var countyid: Int = 0,
    var stateid: Int = 0,
    var districtid: Int = 0,
    var districtname: String? = null,
    var villageid: Int = 0,
    var motherBenId: Long = 0,
    var motherName: String? = null,
    var motherposition: Int = 0,
    var birthBCG: Boolean,
    var birthHepB: Boolean,
    var birthOPV: Boolean
)