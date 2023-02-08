package org.piramalswasthya.sakhi.model

import androidx.navigation.NavDirections
import androidx.room.ColumnInfo

data class Icon(
    val icon: Int,
    val title: String,
    var count: Int?,
    val navAction: NavDirections
)


data class IconCount(
    @ColumnInfo(name = "householdCount")
    val householdCount: Int,
    val allBenCount: Int,
    val eligibleCoupleCount: Int,
    val availBenIdsCount: Int,
    val infantCount: Int,
    val childCount: Int,
    val adolescentCount: Int,
    val pregnantCount: Int,
    val deliveryStageCount: Int,
    val pncMotherCount: Int,
    val reproductiveAgeCount: Int,
    val menopauseCount: Int,
)