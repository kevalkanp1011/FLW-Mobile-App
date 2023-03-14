package org.piramalswasthya.sakhi.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.*
import android.widget.RadioGroup.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.database.room.SyncState
import org.piramalswasthya.sakhi.model.FormInput
import timber.log.Timber


//@BindingAdapter("listItems")
//fun AutoCompleteTextView.setSpinnerItems(list: List<String>?) {
//    list?.let {
//        this.setAdapter(ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, it))
//    }
//}

@BindingAdapter("listItems")
fun AutoCompleteTextView.setSpinnerItems(list: Array<String>?) {
    list?.let {
        this.setAdapter(ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, it))
    }
}

@BindingAdapter("allCaps")
fun TextInputEditText.setAllAlphabetCaps(allCaps : Boolean){
    if(allCaps) {
        isAllCaps = true
        inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
    }
}

@BindingAdapter("radioForm")
fun ConstraintLayout.setItems(form: FormInput?) {
//    if(this.childCount!=0)
//        return

//    val rg = this.findViewById<RadioGroup>(R.id.rg)
//    rg.removeAllViews()
//    rg.apply {
//        form?.list?.let { items ->
//            orientation = form.orientation ?: LinearLayout.HORIZONTAL
//            weightSum = items.size.toFloat()
//            items.forEach {
//                val rdBtn = RadioButton(this.context)
//                rdBtn.layoutParams =
//                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0F).apply {
//                        gravity = Gravity.CENTER_HORIZONTAL
//                    }
//                rdBtn.id = View.generateViewId()
//
//                rdBtn.text = it
//                addView(rdBtn)
//                if (form.value.value == it)
//                    rdBtn.isChecked = true
//                rdBtn.setOnCheckedChangeListener { _, b ->
//                    if (b) {
//                        form.value.value = it
//                    }
//                    form.errorText = null
//                    this@setItems.setBackgroundResource(0)
//                }
//            }
//            form.value.value?.let { value ->
//                children.forEach {
//                    if ((it as RadioButton).text == value) {
//                        clearCheck()
//                        check(it.id)
//                    }
//                }
//            }
//        }
//    }
}

@BindingAdapter("checkBoxesForm")
fun ConstraintLayout.setItemsCheckBox(form: FormInput?) {
//    if (this.childCount != 0)
//        return
    val ll = this.findViewById<LinearLayout>(R.id.ll_checks)
    ll.removeAllViews()
    ll.apply{
        form?.list?.let { items ->
            orientation = form.orientation ?: LinearLayout.VERTICAL
            weightSum = items.size.toFloat()
            items.forEach {
                val cbx = CheckBox(this.context)
                cbx.layoutParams =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0F)
                cbx.id = View.generateViewId()
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                    cbx.setTextAppearance(context, android.R.style.TextAppearance_Material_Medium)
                else
                    cbx.setTextAppearance(android.R.style.TextAppearance_Material_Subhead)
                cbx.text = it
                addView(cbx)
                if (form.value.value?.contains(it) == true)
                    cbx.isChecked = true
                cbx.setOnCheckedChangeListener { _, b ->
                    if (b) {
                        if (form.value.value != null)
                            form.value.value = form.value.value + it
                        else
                            form.value.value = it
                    } else {
                        if (form.value.value?.contains(it) == true) {
                            form.value.value = form.value.value?.replace(it, "")
                        }
                    }
                    if (form.value.value.isNullOrBlank()) {
                        form.value.value = null
                    } else {
                        Timber.d("Called here!")
                        form.errorText = null
                        this@setItemsCheckBox.setBackgroundResource(0)
                    }
                    Timber.d("Checkbox values : ${form.value.value}")
                }
            }
        }
    }
}

@BindingAdapter("required")
fun TextView.setRequired(required: Boolean? = true) {
    required?.let {
        visibility = if (it)
            View.VISIBLE
        else
            View.INVISIBLE
    }
}


private val rotate = RotateAnimation(
    360F,
    0F, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
).apply {
    duration = 1000
    interpolator = LinearInterpolator()
    repeatCount = Animation.INFINITE
}


@BindingAdapter("syncState")
fun ImageView.setSyncState(syncState: SyncState?) {
    syncState?.let {
        val drawable = when (it) {
            SyncState.UNSYNCED -> R.drawable.ic_unsynced
            SyncState.SYNCING -> R.drawable.ic_syncing
            SyncState.SYNCED -> R.drawable.ic_synced
        }
        this.setImageResource(drawable)
        isClickable = it == SyncState.UNSYNCED
        if (it == SyncState.SYNCING)
            this.startAnimation(rotate)
    }
}


@BindingAdapter("benImage")
fun ImageView.setBenImage(uriString: String?) {
    if (uriString == null)
        setImageResource(R.drawable.ic_person)
    else
    {
        Glide
            .with(this)
            .load(Uri.parse(uriString))
            .placeholder(R.drawable.ic_person)
            .circleCrop()
            .into(this)
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
