package org.piramalswasthya.sakhi.model

import androidx.navigation.NavDirections
import androidx.room.ColumnInfo

data class Icon(
    val icon: Int,
    val title: String,
    var count: Int,
    val navAction: NavDirections
)


data class IconCount(
    @ColumnInfo(name = "householdCount")
    val householdCount: Int,
    @ColumnInfo(name = "allBenCount")
    val allBenCount: Int,
    @ColumnInfo(name = "eligibleCoupleCount")
    val eligibleCoupleCount: Int,
    @ColumnInfo(name = "availBenIdsCount")
    val availBenIdsCount: Int,
)