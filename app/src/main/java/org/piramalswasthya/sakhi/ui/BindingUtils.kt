package org.piramalswasthya.sakhi.ui

import android.graphics.drawable.AnimatedVectorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.database.room.SyncState


@BindingAdapter("listItems")
fun AutoCompleteTextView.setSpinnerItems(list : List<String>?){
    list?.let{
        this.setAdapter(ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,it))
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

fun EditText.afterTextChanged(afterTextChanged: (String?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            if(!editable.isNullOrBlank())
                afterTextChanged.invoke(editable.toString())
            else
                afterTextChanged.invoke(null)
        }
    })
}