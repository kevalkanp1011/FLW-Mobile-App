package org.piramalswasthya.sakhi.model

import java.io.File

data class FormElement(
    val id: Int,
    val inputType: InputType,
    var title: String,
    val subtitle: String? = null,
    var entries: Array<String>? = null,
    var required: Boolean,
    val hasDependants: Boolean = false,
    val hasAlertError: Boolean = false,
    var value: String? = null,
    val regex: String? = null,
    val allCaps: Boolean = false,
    val etInputType: Int = android.text.InputType.TYPE_CLASS_TEXT,
    val isMobileNumber: Boolean = false,
    val etMaxLength: Int = 50,
    val multiLine : Boolean = false,
    var errorText: String? = null,
    var max: Long? = null,
    var min: Long? = null,
    var minDecimal: Double? = null,
    var maxDecimal: Double? = null,
    val orientation: Int? = null,
    var imageFile: File? = null
)