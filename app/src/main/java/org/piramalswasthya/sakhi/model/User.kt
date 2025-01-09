package org.piramalswasthya.sakhi.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "USER")
data class UserCache(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "username")
    val userName: String,

    @ColumnInfo(name = "Password")
    val password: String,

    @ColumnInfo(name = "service_map_id")
    val serviceMapId : Int,

//    @ColumnInfo(name = "service_id")
//    val serviceId : Int,

//    @ColumnInfo(name = "service_point_id")
//    val servicePointId : Int,

//    @ColumnInfo(name = "service_point_name")
//    val servicePointName : String,

//    @ColumnInfo(name = "parking_place_id")
//    val parkingPlaceId : Int,

//    @ColumnInfo(name = "parking_place_name")
//    val parkingPlaceName : String,

//    @ColumnInfo(name = "zone_id")
//    val zoneId : Int,
//
//    @ColumnInfo(name = "zone_name")
//    val zoneName : String,

//    @ColumnInfo(name = "van_id")
//    val vanId : Int,

    @Embedded(prefix = "country_")
    val country: LocationEntity,

//    val stateIds: List<Int>,
//
//    val districtIds:List<Int>,
//
//    val blockIds:List<Int> ,
//
//    val villageIds:List<Int>,

    val states: List<LocationEntity>,

    val districts: List<LocationEntity>,

    val blocks: List<LocationEntity>,

    val villages: List<LocationEntity>,

    @ColumnInfo(name = "emergency_contact_number")
    val emergencyContactNo: String,

    @ColumnInfo(name = "user_type")
    val userType: String,

    @ColumnInfo(name = "logged_in")
    val loggedIn: Boolean
) {
    fun asDomainModel(): UserDomain {
        return UserDomain(
            userId = userId,
            userName = userName,
            password = password,
//            serviceMapId = serviceMapId,
//            servicePointId = servicePointId,
//            serviceId = serviceId,
//            servicePointName = servicePointName,
//            vanId = vanId,
//            zoneId = zoneId,
//            zoneName = zoneName,
//            parkingPlaceId = parkingPlaceId,
//            parkingPlaceName = parkingPlaceName,
            country = country,
            states = states,
            districts = districts,
            blocks = blocks,
            villages = villages,

//            countryId = countryId,
//            stateIds = stateIds,
//            districtIds = districtIds,
//            blockIds = blockIds,
//            villageIds = villageIds,
//            stateEnglish = stateEnglish,
//            stateHindi = stateHindi,
//            stateAssamese = emptyList(),
//            districtEnglish = districtEnglish,
//            districtHindi = districtHindi,
//            districtAssamese = emptyList(),
//            blockEnglish = blockEnglish,
//            blockHindi = blockHindi,
//            blockAssamese = emptyList(),
//            villageEnglish = villageEnglish,
//            villageHindi = villageHindi,
//            villageAssamese = emptyList(),

            contactNo = emergencyContactNo,
            userType = userType,
            loggedIn = loggedIn
        )
    }
}


data class UserDomain(
    val userId: Int,
    val userName: String,
    val password: String,
//    val serviceMapId: Int,
//    val serviceId: Int,
//    val servicePointId: Int,
//    val servicePointName: String,
//    val parkingPlaceId: Int,
//    val parkingPlaceName: String,
//    val zoneId: Int,
//    val zoneName: String,
//    val vanId: Int,
    val country: LocationEntity,
    val states: List<LocationEntity>,
    val districts: List<LocationEntity>,
    val blocks: List<LocationEntity>,
    val villages: List<LocationEntity>,
//    val stateIds: List<Int>,
//    val districtIds: List<Int>,
//    val blockIds: List<Int>,
//    val villageIds: List<Int>,
//
//    val stateEnglish: List<String>,
//    val stateHindi: List<String>,
//    val stateAssamese: List<String>,
//    val districtEnglish: List<String>,
//    val districtHindi: List<String>,
//    val districtAssamese: List<String>,
//    val blockEnglish: List<String>,
//    val blockHindi: List<String>,
//    val blockAssamese: List<String>,
//    val villageEnglish: List<String>,
//    val villageHindi: List<String>,
//    val villageAssamese: List<String>,
    val contactNo: String,
    val userType: String,
    val loggedIn: Boolean,

    )


data class UserNetwork(
    val userId: Int,
    val userName: String,
    val password: String,
    var serviceMapId: Int = -1,
    var serviceId: Int = -1,
    var servicePointId: Int = -1,
    var parkingPlaceId: Int = -1,
    var zoneId: Int = -1,
    var vanId: Int = -1,

    var parkingPlaceName: String? = null,
    var servicePointName: String? = null,
    var zoneName: String? = null,

    var country: LocationEntity? = null,

    var states: MutableList<LocationEntity> = mutableListOf(),
    var districts: MutableList<LocationEntity> = mutableListOf(),
    var blocks: MutableList<LocationEntity> = mutableListOf(),
    var villages: MutableList<LocationEntity> = mutableListOf(),


//    var stateIds: MutableList<Int> = mutableListOf(),
//    var stateEnglish: MutableList<String> = mutableListOf(),
//    var stateHindi: MutableList<String> = mutableListOf(),
//
//    var districtIds:MutableList<Int> = mutableListOf(),
//    var districtEnglish:MutableList<String> = mutableListOf(),
//    var districtHindi:MutableList<String> = mutableListOf(),
//
//    var blockIds:MutableList<Int> = mutableListOf(),
//    var blockEnglish:MutableList<String> = mutableListOf(),
//    var blockHindi:MutableList<String> = mutableListOf(),
//
//    var villageIds:MutableList<Int> = mutableListOf(),
//    var villageEnglish:MutableList<String> = mutableListOf(),
//    var villageHindi:MutableList<String> = mutableListOf(),

//    var countryId : Int = -1,

    var emergencyContactNo: String? = null,
    var userType: String? = null,
    var loggedIn: Boolean = false
) {
    fun asCacheModel(): UserCache {
        return UserCache(
            userId = userId,
            userName = userName,
            password = password,
            serviceMapId = serviceMapId,
//            servicePointId = servicePointId,
//            serviceId = serviceId,
//            servicePointName = servicePointName?:"",
//            vanId = vanId,
//            zoneId = zoneId,
//            zoneName = zoneName?:"",
//            parkingPlaceId = parkingPlaceId,
//            parkingPlaceName = parkingPlaceName?:"",
            country = country ?: LocationEntity(1, "India"),
            states = states,
            districts = districts,
            blocks = blocks,
            villages = villages,


//            countryId = countryId,
//
//            stateIds = stateIds,
//            districtIds = districtIds,
//            blockIds = blockIds,
//            villageIds = villageIds,
//
//            stateEnglish = stateEnglish,
//            stateHindi = stateHindi,
//            districtEnglish = districtEnglish,
//            districtHindi = districtHindi,
//            blockEnglish = blockEnglish,
//            blockHindi = blockHindi,
//            villageEnglish = villageEnglish,
//            villageHindi = villageHindi,

            emergencyContactNo = emergencyContactNo ?: "",
            userType = userType ?: "",
            loggedIn = loggedIn

        )
    }
}

data class User(
    val userId: Int,
    val name: String,
    val userName: String,
    val password: String,
    val serviceMapId: Int,
    var vanId: Int = 4,
    val state: LocationEntity,
    val district: LocationEntity,
    val block: LocationEntity,
    val villages: List<LocationEntity>
)


@JsonClass(generateAdapter = true)
data class UserNetworkResponse(
    val success: Boolean,
    val message: String?,
    val data: UserDetailsInResponse
)

@JsonClass(generateAdapter = true)
data class UserDetailsInResponse(
    val userId: Int,
    val name: String,
    val userName: String,
    val stateId: Int,
    val stateName: String,
    val workingDistrictId: Int,
    val workingDistrictName: String,
    val serviceProviderId: Int,
    val roleId: Int,
    val roleName: String,
    val providerServiceMapId: Int,
    val blockId: Int,
    val blockName: String,
    val villageId: String,
    val villageName: String
) {
    fun toUser(password: String): User {
        return User(
            userId = userId,
            name = name,
            userName = userName,
            password = password,
            serviceMapId = providerServiceMapId,
            state = LocationEntity(id = stateId, name = stateName),
            district = LocationEntity(id = workingDistrictId, name = workingDistrictName),
            block = LocationEntity(id = blockId, name = blockName),
            villages = getLocationEntityListForVillage(villageId, villageName),
        )
    }

    private fun getLocationEntityListForVillage(
        villageId: String,
        villageName: String
    ): List<LocationEntity> {
        val idList = villageId.split(",").map { it.toInt() }
        val nameList = villageName.split(",")
        val locationEntityList = mutableListOf<LocationEntity>()
        if (idList.size == nameList.size) {
            for (index: Int in idList.indices) {
                locationEntityList.add(LocationEntity(id = idList[index], name = nameList[index]))
            }
        }
        return locationEntityList
    }
}

