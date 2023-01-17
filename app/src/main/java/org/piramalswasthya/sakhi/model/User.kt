package org.piramalswasthya.sakhi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

    @ColumnInfo(name = "service_point_id")
    val servicePointId : Int,

    @ColumnInfo(name = "state_en")
    val stateEnglish: List<String>,

    @ColumnInfo(name = "state_hi")
    val stateHindi: List<String>,

    @ColumnInfo(name = "district_en")
    val districtEnglish: List<String>,

    @ColumnInfo(name = "district_hi")
    val districtHindi: List<String>,

    @ColumnInfo(name = "block_en")
    val blockEnglish: List<String>,

    @ColumnInfo(name = "block_hi")
    val blockHindi: List<String>,

    @ColumnInfo(name = "village_en")
    val villageEnglish: List<String>,

    @ColumnInfo(name = "village_hi")
    val villageHindi: List<String>,

    @ColumnInfo(name = "contact_number")
    val contactNo: String,

    @ColumnInfo(name = "user_type")
    val userType: String,

    @ColumnInfo(name="logged_in")
    val loggedIn : Boolean
){
    fun asDomainModel() : UserDomain{
        return UserDomain(
            userId = userId,
            userName = userName,
            password = password,
            serviceMapId = serviceMapId,
            servicePointId = servicePointId,

            stateEnglish = stateEnglish,
            stateHindi = stateHindi,
            districtEnglish = districtEnglish,
            districtHindi = districtHindi,
            blockEnglish = blockEnglish,
            blockHindi = blockHindi,
            villageEnglish = villageEnglish,
            villageHindi = villageHindi,

            contactNo = contactNo,
            userType = userType,
            loggedIn = loggedIn
        )
    }
}



data class UserDomain(
    val userId: Int,
    val userName: String,
    val password: String,
    val serviceMapId : Int,
    val servicePointId : Int,
    val stateEnglish: List<String>,
    val stateHindi: List<String>,
    val districtEnglish: List<String>,
    val districtHindi: List<String>,
    val blockEnglish: List<String>,
    val blockHindi: List<String>,
    val villageEnglish: List<String>,
    val villageHindi: List<String>,
    val contactNo: String,
    val userType: String,
    val loggedIn : Boolean
)


data class UserNetwork(
    val userId: Int,
    val userName: String,
    val password: String,
    var serviceMapId : Int = -1,
    var servicePointId : Int = -1,

    var stateEnglish: MutableList<String> = mutableListOf(),
    var stateHindi: MutableList<String> = mutableListOf(),
    var districtEnglish:MutableList<String> = mutableListOf(),
    var districtHindi:MutableList<String> = mutableListOf(),
    var blockEnglish:MutableList<String> = mutableListOf(),
    var blockHindi:MutableList<String> = mutableListOf(),
    var villageEnglish:MutableList<String> = mutableListOf(),
    var villageHindi:MutableList<String> = mutableListOf(),

    var contactNo: String? = null,
    var userType: String? = null,
    var loggedIn : Boolean = false
) {
    fun asCacheModel() : UserCache{
        return UserCache(
            userId = userId,
            userName = userName,
            password = password,
            serviceMapId = serviceMapId,
            servicePointId = servicePointId,

            stateEnglish = stateEnglish,
            stateHindi = stateHindi,
            districtEnglish = districtEnglish,
            districtHindi = districtHindi,
            blockEnglish = blockEnglish,
            blockHindi = blockHindi,
            villageEnglish = villageEnglish,
            villageHindi = villageHindi,

            contactNo = contactNo?:"NA",
            userType = userType?:"NA",
            loggedIn = loggedIn

        )
    }
}

