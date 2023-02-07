package org.piramalswasthya.sakhi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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

) {
    fun asBasicDomainModel(): HouseHoldBasicDomain {
        return HouseHoldBasicDomain(
            hhId = householdId,
            headName = familyHeadName ?: "Not Available",
            headSurname = familyName ?: "Not Available"

        )
    }

    fun asNetworkModel(userCache: UserCache): HouseholdNetwork {
        return HouseholdNetwork(
            householdId = householdId.toString(),
            ashaId = ashaId,
            benId = benId ?: 0,
            familyHeadName = familyHeadName,
            familyName = familyName,
            familyHeadPhoneNo = familyHeadPhoneNo,
            houseNo = houseNo,
            houseNum = houseNum,
            wardNo = wardNo,
            wardName = wardName,
            mohallaName = mohallaName,
            rationCardDetails = rationCardDetails,
            povertyLine = povertyLine,
            residentialArea = residentialArea,
            otherResidentialArea = otherResidentialArea,
            houseType = houseType,
            otherHouseType = otherHouseType,
            separateKitchen = separateKitchen,
            fuelUsed = fuelUsed,
            otherFuelUsed = otherFuelUsed,
            sourceOfDrinkingWater = sourceOfDrinkingWater,
            availabilityOfElectricity = availabilityOfElectricity,
            otherAvailabilityOfElectricity = otherAvailabilityOfElectricity,
            availabilityOfToilet = availabilityOfToilet,
            otherAvailabilityOfToilet = otherAvailabilityOfToilet,
            state = state,
            district = district,
            block = block,
            village = village,
            serverUpdatedStatus = serverUpdatedStatus,
            createdBy = createdBy,
            createdDate = getDateTimeStringFromLong(createdTimeStamp),
            updatedBy = updatedBy,
            updatedDate = getDateTimeStringFromLong(updatedTimeStamp),
            ProviderServiceMapID = userCache.serviceMapId,
            VanID = userCache.vanId,
            Processed = processed,
            Countyid = countyId ?: 0,
            stateid = stateId ?: 0,
            districtid = districtId ?: 0,
            districtname = district,
            blockid = blockId ?: 0,
            villageid = villageId ?: 0
        )

    }
}


@JsonClass(generateAdapter = true)
data class HouseholdNetwork(
    @Json(name = "houseoldId")
    val householdId: String,
    @Json(name = "ashaid")
    val ashaId: Int,
    @Json(name = "benficieryid")
    val benId: Long = 0,

    //FamilyDetails

    val familyHeadName: String? = null,

    val familyName: String? = null,

    val familyHeadPhoneNo: Long? = null,
    @Json(name = "houseno")
    val houseNo: String? = null,
    @Json(name = "houseNum")
    val houseNum: String? = null,

    val wardNo: String? = null,

    val wardName: String? = null,

    val mohallaName: String? = null,

    val rationCardDetails: String? = null,
    @Json(name = "type_bpl_apl")
    val povertyLine: String? = null,
    @Json(name = "bpl_aplId")
    val povertyLineId: Int = 0,
    //household Details
    val residentialArea: String? = null,

    @Json(name = "residentialAreaId")
    val residentialAreaId: Int = 0,
    @Json(name = "other_residentialArea")
    val otherResidentialArea: String? = null,

    val houseType: String? = null,
    @Json(name = "houseTypeId")
    val houseTypeId: Int = 0,

    @Json(name = "other_houseType")
    val otherHouseType: String? = null,

    @Json(name = "houseOwnerShip")
    val isHouseOwned: String? = null,
    @Json(name = "houseOwnerShipId")
    val houseOwnerShipId: Int = 0,

    @Json(name = "landOwned")
    val isLandOwned: Boolean? = null,
    @Json(name = "landOwnedId")
    val landOwnedId: Int = 0,

    @Json(name = "landIrregated")
    val landIrregated: String? = null,
    @Json(name = "landIrregatedId")
    val landIrregatedId: Int = 0,

    @Json(name = "liveStockOwnerShip")
    val isLivestockOwned: Boolean? = null,
    @Json(name = "liveStockOwnerShipId")
    val liveStockOwnerShipId: Int = 0,

    @Json(name = "Street")
    val street: String? = null,

    @Json(name = "Colony")
    val colony: String? = null,

    @Json(name = "Pincode")
    val pincode: Int? = null,

    //HH Amenities
    @Json(name = "seperateKitchen")
    val separateKitchen: String? = null,
    @Json(name = "seperateKitchenId")
    val seperateKitchenId: Int = 0,

    val fuelUsed: String? = null,
    @Json(name = "fuelUsedId")
    val fuelUsedId: Int = 0,
    @Json(name = "other_fuelUsed")
    val otherFuelUsed: String? = null,

    @Json(name = "sourceofDrinkingWater")
    val sourceOfDrinkingWater: String? = null,
    @Json(name = "sourceofDrinkingWaterId")
    val sourceofDrinkingWaterId: Int = 0,
    @Json(name = "other_sourceofDrinkingWater")
    val otherSourceOfDrinkingWater: String? = null,

    @Json(name = "avalabilityofElectricity")
    val availabilityOfElectricity: String? = null,
    @Json(name = "avalabilityofElectricityId")
    val avalabilityofElectricityId: Int = 0,
    @Json(name = "other_avalabilityofElectricity")
    val otherAvailabilityOfElectricity: String? = null,

    @Json(name = "availabilityofToilet")
    val availabilityOfToilet: String? = null,
    @Json(name = "availabilityofToiletId")
    val availabilityofToiletId: Int = 0,
    @Json(name = "other_availabilityofToilet")
    val otherAvailabilityOfToilet: String? = null,
    @Json(name = "motarizedVehicle")
    val motorizedVehicle: String? = null,
    @Json(name = "motarizedVehicleId")
    val motarizedVehicleId: Int = 0,
    @Json(name = "other_motarizedVehicle")
    val otherMotorizedVehicle: String? = null,

    val registrationType: String? = null,
    @Json(name = "state")
    val state: String? = null,

    @Json(name = "district")
    val district: String? = null,

    @Json(name = "block")
    val block: String? = null,

    @Json(name = "village")
    val village: String? = null,

    @Json(name = "serverUpdatedStatus")
    val serverUpdatedStatus: Int = 0,

    @Json(name = "createdBy")
    val createdBy: String? = null,

    @Json(name = "createdDate")
    val createdDate: String? = null,

    @Json(name = "updatedBy")
    val updatedBy: String? = null,

    @Json(name = "updatedDate")
    val updatedDate: String? = null,

    @Json(name = "ProviderServiceMapID")
    val ProviderServiceMapID: Int = 0,

    @Json(name = "VanID")
    val VanID: Int = 0,

    @Json(name = "Processed")
    val Processed: String? = null,

    @Json(name = "Countyid")
    val Countyid: Int = 0,

    @Json(name = "stateid")
    val stateid: Int = 0,

    @Json(name = "districtid")
    val districtid: Int = 0,

    @Json(name = "districtname")
    val districtname: String? = null,

    @Json(name = "blockid")
    val blockid: Int = 0,

    @Json(name = "villageid")
    val villageid: Int = 0,

    ) {
    fun asCacheModel() {

    }
}

data class HouseHoldBasicDomain(
    val hhId: Long,
    val headName: String,
    val headSurname: String
)