package org.piramalswasthya.sakhi.model

data class FormInput(
    val inputType: InputType,
    val title : String,
    val list : List<String>? = null,
    val required : Boolean,
    var value : String? = null,
    val regex : String? = null,
    val useFormEditTextDefaultInputFilter : Boolean = true,
    val etInputType : Int? = null,
    val etLength : Int = 30,
    var errorText : String? = null
){
    enum class InputType{
        EDIT_TEXT,
        DROPDOWN,
//        RADIO,
//        DATE_PICKER
    }
}
