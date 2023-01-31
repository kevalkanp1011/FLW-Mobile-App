package org.piramalswasthya.sakhi.adapters

import android.app.DatePickerDialog
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onSubscription
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.configuration.FormEditTextDefaultInputFilter
import org.piramalswasthya.sakhi.databinding.RvItemCheckBinding
import org.piramalswasthya.sakhi.databinding.RvItemDatepickerBinding
import org.piramalswasthya.sakhi.databinding.RvItemDropdownBinding
import org.piramalswasthya.sakhi.databinding.RvItemEditTextBinding
import org.piramalswasthya.sakhi.databinding.RvItemRadioBinding
import org.piramalswasthya.sakhi.databinding.RvItemTextViewBinding
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.FormInput.InputType.*
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class FormInputAdapter (private val imageClickListener : FormInputAdapter.ImageClickListener?=null): ListAdapter<FormInput, ViewHolder>(FormInputDiffCallBack) {
    object FormInputDiffCallBack : DiffUtil.ItemCallback<FormInput>() {
        override fun areItemsTheSame(oldItem: FormInput, newItem: FormInput) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: FormInput, newItem: FormInput) = oldItem == newItem

    }

    class EditTextInputViewHolder private constructor(private val binding: RvItemEditTextBinding) :
        ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemEditTextBinding.inflate(layoutInflater, parent, false)
                return EditTextInputViewHolder(binding)
            }
        }

        fun bind(item: FormInput) {
            binding.form = item
            //binding.et.setText(item.value.value)
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    if(!editable.isNullOrBlank()) {
                        item.value.value = editable.toString()
                        Timber.d("Item ET : $item")
                        if (item.errorText != null && editable.isNotBlank()) {
                            item.errorText = null
                            binding.tilEditText.error = null
                        }

                    }
                }
            }
            binding.et.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus)
                    binding.et.addTextChangedListener(textWatcher)
                else
                    binding.et.removeTextChangedListener(textWatcher)
            }
            item.errorText?.also { binding.tilEditText.error = it }?: run{binding.tilEditText.error = null}
            val etFilters = mutableListOf<InputFilter>(InputFilter.LengthFilter(item.etLength))
            binding.et.inputType = item.etInputType
            if (item.etInputType == InputType.TYPE_CLASS_TEXT && item.useFormEditTextDefaultInputFilter) {
                etFilters.add(FormEditTextDefaultInputFilter)
                binding.et.filters = etFilters.toTypedArray()
            }
            else{
                binding.et.filters = etFilters.toTypedArray()
            }
            binding.executePendingBindings()
        }
    }

    class DropDownInputViewHolder private constructor(private val binding: RvItemDropdownBinding) :
        ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemDropdownBinding.inflate(layoutInflater, parent, false)
                return DropDownInputViewHolder(binding)
            }
        }

        fun bind(item: FormInput, ) {
            val savedValue = item.value.value
            item.value.value = null
            item.value.value = savedValue
            binding.form = item
            binding.actvRvDropdown.setOnItemClickListener { _, _, index, _ ->
                item.value.value = item.list?.get(index)
                Timber.d("Item DD : $item")
                item.errorText = null
                binding.tilRvDropdown.error = null
            }

            item.errorText?.let { binding.tilRvDropdown.error = it }
            binding.executePendingBindings()

        }
    }

    class RadioInputViewHolder private constructor(private val binding: RvItemRadioBinding) :
        ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemRadioBinding.inflate(layoutInflater, parent, false)
                return RadioInputViewHolder(binding)
            }
        }

        fun bind(
            item: FormInput ){
            binding.form = item

            //item.errorText?.let { binding.rg.error = it }
            binding.executePendingBindings()

        }
    }

    class CheckBoxesInputViewHolder private constructor(private val binding: RvItemCheckBinding) :
        ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemCheckBinding.inflate(layoutInflater, parent, false)
                return CheckBoxesInputViewHolder(binding)
            }
        }

        fun bind(
            item: FormInput ){
            binding.form = item

            //item.errorText?.let { binding.rg.error = it }
            binding.executePendingBindings()

        }
    }

    class DatePickerInputViewHolder private constructor(private val binding: RvItemDatepickerBinding) :
        ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemDatepickerBinding.inflate(layoutInflater, parent, false)
                return DatePickerInputViewHolder(binding)
            }
        }

        fun bind(
            item: FormInput) {
            val today = Calendar.getInstance()
            var thisYear = today.get(Calendar.YEAR)
            var thisMonth = today.get(Calendar.MONTH)
            var thisDay = today.get(Calendar.DAY_OF_MONTH)
            binding.form = item
            item.errorText?.also { binding.tilEditText.error = it }?: run{binding.tilEditText.error = null}
            binding.et.setOnClickListener{
                item.value.value?.let {value ->
                    thisYear = value.substring(6).toInt()
                    thisMonth = value.substring(3,5).trim().toInt()-1
                    thisDay = value.substring(0,2).trim().toInt()
                }
                val datePickerDialog = DatePickerDialog(it.context,
                    { _, year, month, day ->
                        item.value.value = "${if(day > 9)day else "0$day"}-${if(month > 8)month+1 else "0${month + 1}"}-$year"
                        binding.invalidateAll()
                    }, thisYear, thisMonth, thisDay
                )
                item.errorText = null
                binding.tilEditText.error = null
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.datePicker.touchables[0].performClick();
                datePickerDialog.show()
            }
            binding.executePendingBindings()

        }
    }

    class TextViewInputViewHolder private constructor(private val binding: RvItemTextViewBinding) :
        ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemTextViewBinding.inflate(layoutInflater, parent, false)
                return TextViewInputViewHolder(binding)
            }
        }

        fun bind(item: FormInput) {
            binding.form = item
            binding.executePendingBindings()
        }
    }

    class ImageViewInputViewHolder private constructor(private val binding: RvItemDropdownBinding) :
        ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemDropdownBinding.inflate(layoutInflater, parent, false)
                return ImageViewInputViewHolder(binding)
            }
        }

        fun bind(
            item: FormInput,
            clickListener: ImageClickListener?,
        ) {
            binding.form = item
            item.errorText?.let { binding.tilRvDropdown.error = it }
            binding.executePendingBindings()

        }
    }
    class ImageClickListener(private val imageClick : (form : FormInput) -> Unit) {

        fun onImageClick(form : FormInput) = imageClick(form)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inputTypes = values()
        return when (inputTypes[viewType]) {
            EDIT_TEXT -> EditTextInputViewHolder.from(parent)
            DROPDOWN -> DropDownInputViewHolder.from(parent)
            RADIO -> RadioInputViewHolder.from(parent)
            DATE_PICKER -> DatePickerInputViewHolder.from(parent)
            TEXT_VIEW -> TextViewInputViewHolder.from(parent)
            IMAGE_VIEW -> TODO()
            CHECKBOXES -> CheckBoxesInputViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.inputType) {
            EDIT_TEXT -> (holder as EditTextInputViewHolder).bind(item)
            DROPDOWN ->  (holder as DropDownInputViewHolder).bind(item)
            RADIO -> (holder as RadioInputViewHolder).bind(item)
            DATE_PICKER ->(holder as DatePickerInputViewHolder).bind(item)
            TEXT_VIEW -> (holder as TextViewInputViewHolder).bind(item)
            IMAGE_VIEW -> (holder as ImageViewInputViewHolder).bind(item,imageClickListener)
            CHECKBOXES -> (holder as CheckBoxesInputViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) =
        getItem(position).inputType.ordinal


    fun validateInput(): Boolean {
        var retVal = true
        currentList.forEach {
            if (it.required) {
                if (it.value.value.isNullOrBlank()) {
                    Timber.d("validateInput called for item $it, with index ${currentList.indexOf(it)}")
                    it.errorText = "Required field cannot be empty !"
                    notifyItemChanged(currentList.indexOf(it))
                    retVal = false
                }
            }
/*            if(it.regex!=null){
                Timber.d("Regex not null")
                retVal= false
            }*/
        }
        return retVal
    }
}