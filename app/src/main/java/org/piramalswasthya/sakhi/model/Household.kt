package org.piramalswasthya.sakhi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "HOUSEHOLD",
    foreignKeys = [ForeignKey(
        entity = UserCache::class,
        parentColumns = arrayOf("user_id"),
        childColumns = arrayOf("ashaId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class HouseholdCache(

    @PrimaryKey
    var householdId: Long,

    @ColumnInfo(index = true)
    var ashaId: Int,

    var benId: Long? = null,

    //FamilyDetails

    var familyHeadName: String? = null,

    var familyName: String? = null,

    var familyHeadPhoneNo: Long? = null,

    var houseNo: String? = null,

    var houseNum: String? = null,

    var wardNo: String? = null,

    var wardName: String? = null,

    var mohallaName: String? = null,

    var rationCardDetails: String? = null,

    var povertyLine: String? = null,

    //household Details
    var residentialArea: String? = null,

    var otherResidentialArea: String? = null,

    var houseType: String? = null,

    var otherHouseType: String? = null,

    var isHouseOwned: String? = null,

    var isLandOwned: Boolean? = null,

    var isLandIrrigated: Boolean? = null,

    var isLivestockOwned: Boolean? = null,

    var street: String? = null,

    var colony: String? = null,

    var pincode: Int? = null,

    //HH Amenities
    var separateKitchen: String? = null,

    var fuelUsed: String? = null,

    var otherFuelUsed: String? = null,

    var sourceOfDrinkingWater: String? = null,

    var otherSourceOfDrinkingWater: String? = null,

    var availabilityOfElectricity: String? = null,

    var otherAvailabilityOfElectricity: String? = null,

    var availabilityOfToilet: String? = null,

    var otherAvailabilityOfToilet: String? = null,

    var motorizedVehicle: String? = null,

    var otherMotorizedVehicle: String? = null,

    //Spam
    var registrationType: String? = null,

    var state: String? = null,

    var stateId: Int? = null,

    var district: String? = null,

    var districtId: Int? = null,

    var block: String? = null,

    var blockId: Int? = null,

    var village: String? = null,

    var villageId: Int? = null,

    var countyId: Int? = null,

    var serverUpdatedStatus: Int = 0,

    var createdBy: String? = null,

    var createdTimeStamp: Long? = null,

    var updatedBy: String? = null,

    var updatedTimeStamp: Long? = null,
//
//    var providerServiceMapId: Int? = null,
//
//    var vanId :Int? = null,

    var processed: String? = null,

    var isDraft: Boolean

){
    fun asBasicDomainModel() : HouseHoldBasicDomain{
        return HouseHoldBasicDomain(
            hhId = householdId,
            headName = familyHeadName?:"Not Available",
            headSurname = familyName?:"Not Available"

        )
    }
}

data class HouseHoldBasicDomain(
    val hhId : Long,
    val headName : String,
    val headSurname : String
)