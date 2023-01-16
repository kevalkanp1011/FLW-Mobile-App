package org.piramalswasthya.sakhi.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("app:listItems")
fun AutoCompleteTextView.setSpinnerItems(list : List<String>?){
    list?.let{
        this.setAdapter(ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,it))
    }
}

@BindingAdapter("app:required")
fun TextView.setRequired(required : Boolean? = true){
    required?.let{
        visibility = if(it)
            View.VISIBLE
        else
            View.INVISIBLE
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            if(!editable.isNullOrBlank())
                afterTextChanged.invoke(editable.toString())
        }
    })
}