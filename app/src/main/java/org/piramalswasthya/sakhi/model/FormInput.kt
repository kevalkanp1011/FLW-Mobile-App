package org.piramalswasthya.sakhi.model

import android.text.InputType.TYPE_CLASS_TEXT
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File


data class FormInput(
    val inputType: InputType,
    var title: String,
    var list: Array<String>? = null,
    var required: Boolean,
    var value: MutableStateFlow<String?> = MutableStateFlow(null),
    val regex: String? = null,
    val allCaps: Boolean = false,
    val etInputType: Int = TYPE_CLASS_TEXT,
    val isMobileNumber: Boolean = false,
    val etMaxLength: Int = 30,
    var errorText: String? = null,
    var max: Long? = null,
    var min: Long? = null,
    val orientation: Int? = null,
    var imageFile: File? = null
){


    enum class InputType{
        EDIT_TEXT,
        DROPDOWN,
        RADIO,
        DATE_PICKER,
        TEXT_VIEW,
        IMAGE_VIEW,
        CHECKBOXES
    }
}
