package org.piramalswasthya.sakhi.model

import android.text.InputType.TYPE_CLASS_TEXT
import kotlinx.coroutines.flow.MutableStateFlow


data class FormInput(
    val inputType: InputType,
    var title: String,
    var list: List<String>? = null,
    val required: Boolean,
    var value: MutableStateFlow<String?> = MutableStateFlow(null),
    val regex: String? = null,
    val useFormEditTextDefaultInputFilter: Boolean = true,
    val etInputType: Int = TYPE_CLASS_TEXT,
    val etLength: Int = 30,
    var errorText: String? = null,
    var max: Long? = null,
    var min: Long? = null,
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
