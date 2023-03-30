package org.piramalswasthya.sakhi.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import org.piramalswasthya.sakhi.database.room.SyncState
import java.text.SimpleDateFormat
import java.util.*

@Entity(
    tableName = "MDSR",
    foreignKeys = [ForeignKey(
        entity = BenRegCache::class,
        parentColumns = arrayOf("beneficiaryId", "householdId"),
        childColumns = arrayOf("benId", "hhId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(name = "mdsrInd", value = ["benId", "hhId"])]
)

data class MDSRCache (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val benId: Long,
    val hhId: Long,
    var dateOfDeath: Long? = System.currentTimeMillis(),
    var address: String? = null,
    var husbandName: String? = null,
    var causeOfDeath: Int = 0,
    var reasonOfDeath: String? = null,
    var investigationDate: Long? = System.currentTimeMillis(),
    var actionTaken: Int = 0,
    var blockMOSign: String? = null,
    var date: Long? = System.currentTimeMillis(),
    var processed: String,
    val syncState : SyncState,
    var createdBy: String? = null,
    var createdDate: Long? = System.currentTimeMillis()
) {
    fun asPostModel(user: UserCache, household: HouseholdCache, ben: BenRegCache, mdsrCount: Int): MdsrPost {
        return MdsrPost(
            beneficiaryid = benId,
            houseoldId = hhId.toString(),
            createdBy = createdBy,
            villageid = household.villageId,
            district = household.district,
            state = household.state,
            month = getMonth(),
            year = getYear(),
            nameOfDeceased = ben.firstName,
            age = ben.age.toString(),
            dateOfDeceased = getDateTimeStringFromLong(dateOfDeath),
            address = address,
            husbandName = husbandName,
            causeOfDeath = causeOfDeath,
            reasonDeath = reasonOfDeath,
            fieldInvestigation = getDateTimeStringFromLong(investigationDate),
            action = actionTaken,
            signature = blockMOSign,
            dateIc = getDateTimeStringFromLong(date),
            createdDate = createdDate,
            updatedBy = user.userName,
            updatedDate = System.currentTimeMillis()/1000L,
            loginId = (mdsrCount+122).toString(),
            id = mdsrCount+122,
            vistDate = ""
        )
    }
}

@JsonClass(generateAdapter = true)
data class MdsrPost(

    val id: Int = 0,
    val beneficiaryid: Long,
    val houseoldId: String,
    val villageid: Int? = 0,
    val syncstatus: Boolean? = null,
    val district: String? = null,
    val state: String? = null,
    val month: String? = null,
    val year: String? = null,
    val nameOfDeceased: String? = null,
    val age: String? = null,
    val dateOfDeceased: String? = null,
    val address: String? = null,
    val husbandName: String? = null,
    val causeOfDeath: Int? = 0,
    val reasonDeath: String? = null,
    val fieldInvestigation: String? = null,
    val action: Int? = 0,
    val signature: String? = null,
    val dateIc: String? = null,
    val createdBy: String? = null,
    val createdDate: Long? = 0L,
    val updatedBy: String? = null,
    val updatedDate: Long? = 0L,
    val loginId: String? = null,
    val vistDate: String? = null
)

fun getMonth(): String {
    val calendar = Calendar.getInstance()
    val date = calendar.time
    val month = SimpleDateFormat("MM").format(date) // always 2 digits
    return month
}
fun getYear(): String {
    val calendar = Calendar.getInstance()
    val date = calendar.time
    val year = SimpleDateFormat("yyyy").format(date) // 4 digit y
    return year
}
fun getDays(): String {
    val calendar = Calendar.getInstance()
    val date = calendar.time
    val days = SimpleDateFormat("dd").format(date) // 2 digit y
    return days
}
