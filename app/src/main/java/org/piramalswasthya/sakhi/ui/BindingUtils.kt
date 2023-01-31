package org.piramalswasthya.sakhi.ui

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.view.View
import android.widget.*
import android.widget.RadioGroup.LayoutParams
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.FormInput
import timber.log.Timber


@BindingAdapter("listItems")
fun AutoCompleteTextView.setSpinnerItems(list : List<String>?){
    list?.let{
        this.setAdapter(ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,it))
    }
}

@BindingAdapter("form")
fun RadioGroup.setItems(form : FormInput?){
    if(this.childCount!=0)
        return
    form?.list?.let{items->
        orientation = LinearLayout.HORIZONTAL
        weightSum = items.size.toFloat()
        items.forEach{
            val rdBtn = RadioButton(this.context)
            rdBtn.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1.0F)
            rdBtn.id = View.generateViewId()
            rdBtn.text = it
            addView(rdBtn)
            if(form.value.value==it)
                rdBtn.isChecked = true
            rdBtn.setOnCheckedChangeListener { _, b ->
                if(b) {
                    form.value.value = it
                }
            }
        }
        form.value.value?.let { value->
            children.forEach {
                if((it as RadioButton).text==value){
                    clearCheck()
                    check(it.id)
            }
        }
        }
    }
}

@BindingAdapter("form")
fun LinearLayout.setItems(form : FormInput?){
    if(this.childCount!=0)
        return
    form?.list?.let{items->
        orientation = LinearLayout.VERTICAL
        weightSum = items.size.toFloat()
        items.forEach{
            val cbx = CheckBox(this.context)
            cbx.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1.0F)
            cbx.id = View.generateViewId()
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                cbx.setTextAppearance(context,android.R.style.TextAppearance_Material_Medium)
            else
                cbx.setTextAppearance(android.R.style.TextAppearance_Material_Subhead)
            cbx.text = it
            addView(cbx)
            if(form.value.value?.contains(it) == true)
                cbx.isChecked = true
            cbx.setOnCheckedChangeListener { _, b ->
                if(b) {
                    if(form.value.value!=null)
                        form.value.value = form.value.value + it
                    else
                        form.value.value = it
                }
                else
                    if(form.value.value?.contains(it) == true){
                        form.value.value = form.value.value?.replace(it,"")
                    }
                if(form.value.value.isNullOrBlank()){
                    form.value.value= null
                }
                Timber.d("Checkbox values : ${form.value.value}")
            }
        }
    }
}

@BindingAdapter("required")
fun TextView.setRequired(required : Boolean? = true){
    required?.let{
        visibility = if(it)
            View.VISIBLE
        else
            View.INVISIBLE
    }
}

@BindingAdapter("syncState")
fun ImageView.setSyncState(syncState: SyncState?){
    syncState?.let{
        val drawable = when(it){
            SyncState.UNSYNCED -> R.drawable.ic_unsynced
            SyncState.SYNCING -> R.drawable.ic_syncing
            SyncState.SYNCED -> R.drawable.ic_synced
        }
        this.setImageResource(drawable)
        if(it == SyncState.SYNCING)
            (getDrawable() as AnimatedVectorDrawable).start()
    }
}

//fun EditText.afterTextChanged(afterTextChanged: (String?) -> Unit) {
//    this.addTextChangedListener(object : TextWatcher {
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//        }
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//        }
//
//        override fun afterTextChanged(editable: Editable?) {
//            if(!editable.isNullOrBlank())
//                afterTextChanged.invoke(editable.toString())
//        }
//    })
//}