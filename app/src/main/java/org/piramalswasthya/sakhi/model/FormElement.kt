package org.piramalswasthya.sakhi.model

import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes

data class FormElement(
    val id: Int,
    var inputType: InputType,
    var required: Boolean,
    var title: String,
    val subtitle: String? = null,
      @ArrayRes var arrayId: Int = -1,
    var entries: Array<String>? = null,
    var doubleStar: Boolean = false,
    val hasDependants: Boolean = false,
    val hasAlertError: Boolean = false,
    var value: String? = null,
    val regex: String? = null,
    val allCaps: Boolean = false,
    val etInputType: Int = android.text.InputType.TYPE_CLASS_TEXT,
    val isMobileNumber: Boolean = false,
    val etMaxLength: Int = 50,
    val multiLine: Boolean = false,
    var errorText: String? = null,
    var max: Long? = null,
    var min: Long? = null,
    var minDecimal: Double? = null,
    var maxDecimal: Double? = null,
    val orientation: Int? = null,
    var hasSpeechToText: Boolean = false,
    var showDateNumberPicker: Boolean = false,
    var showHighRisk: Boolean = false,
    var isEnabled: Boolean = true,
    var headingLine: Boolean = true,
    val showYearFirstInDatePicker: Boolean = false,
    @DrawableRes val backgroundDrawable: Int? = null,
    @DrawableRes val iconDrawableRes: Int? = null,
    val showDrawable: Boolean? = false
)