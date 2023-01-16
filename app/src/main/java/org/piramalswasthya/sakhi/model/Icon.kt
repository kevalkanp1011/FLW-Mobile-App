package org.piramalswasthya.sakhi.model

import androidx.navigation.NavDirections

data class Icon(
    val icon: Int,
    val title: String,
    val navAction: NavDirections
)