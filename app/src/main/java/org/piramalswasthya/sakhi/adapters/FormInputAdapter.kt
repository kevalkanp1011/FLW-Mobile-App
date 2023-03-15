package org.piramalswasthya.sakhi.adapters

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.configuration.FormEditTextDefaultInputFilter
import org.piramalswasthya.sakhi.databinding.*
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.FormInput.InputType.*
import timber.log.Timber
import java.util.*

class FormInputAdapter(private val imageClickListener: ImageClickListener? = null, private val isEnabled: Boolean = true) :
    ListAdapter<FormInput, ViewHolder>(FormInputDiffCallBack) {
    object FormInputDiffCallBack : DiffUtil.ItemCallback<FormInput>() {
        override fun areItemsTheSame(oldItem: FormInput, newItem: FormInput) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: FormInput, newItem: FormInput) =
            (oldItem == newItem)

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

        fun bind(item: FormInput, isEnabled: Boolean) {

            binding.et.isClickable = isEnabled
            binding.et.isFocusable = isEnabled
            binding.form = item
            if(!isEnabled){
                binding.executePendingBindings()
                return
            }
            //binding.et.setText(item.value.value)
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(editable: Editable?) {
                    if (editable == null || editable.toString() == "") {
                        if(!item.required) {
                            item.errorText = null
                            binding.tilEditText.error = null
                        }
                        item.value.value = null
                        return
                    }
                    editable.let { item.value.value = it.toString() }
                    item.value.value = editable.toString()
                    Timber.d("Item ET : $item")
                    if (item.isMobileNumber) {
                        if (item.etMaxLength == 10) {
                            if (editable.first().toString()
                                    .toInt() < 6 || editable.length != item.etMaxLength
                            ) {
                                item.errorText = "Invalid Mobile Number !"
                                binding.tilEditText.error = item.errorText
                            } else {
                                item.errorText = null
                                binding.tilEditText.error = item.errorText
                            }
                        } else if (item.etMaxLength == 12) {
                            if (editable.first().toString()
                                    .toInt() ==0 ||editable.length != item.etMaxLength ) {
                                item.errorText = "Invalid ${item.title} !"
                                binding.tilEditText.error = item.errorText
                            } else {
                                item.errorText = null
                                binding.tilEditText.error = item.errorText
                            }
                        }
                    }
                    else if(item.etInputType == InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL){
                        val entered = editable.toString().toDouble()
                        item.minDecimal?.let {
                            if (entered < it) {
                                binding.tilEditText.error = "Field value has to be at least $it"
                                item.errorText = binding.tilEditText.error.toString()
                            }
                        }
                        item.maxDecimal?.let {
                            if (entered > it) {
                                binding.tilEditText.error =
                                    "Field value has to be less than $it"
                                item.errorText = binding.tilEditText.error.toString()
                            }
                        }
                        if (item.minDecimal != null && item.maxDecimal != null && entered >= item.minDecimal!! && entered <= item.maxDecimal!!) {
                            binding.tilEditText.error = null
                            item.errorText = null
                        }

                    }
                    else if (item.etInputType == (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL)) {
                        val age = editable.toString().toLong()
                        item.min?.let {
                            if (age < it) {
                                binding.tilEditText.error = "Field value has to be at least $it"
                                item.errorText = binding.tilEditText.error.toString()
                            }
                        }
                        item.max?.let {
                            if (age > it) {
                                binding.tilEditText.error =
                                    "Field value has to be less than $it"
                                item.errorText = binding.tilEditText.error.toString()
                            }
                        }
                        if (item.min != null && item.max != null && age >= item.min!! && age <= item.max!!) {
                            binding.tilEditText.error = null
                            item.errorText = null
                        }
                    } else {
                        if (item.errorText != null && editable.isNotBlank()) {
                            item.errorText = null
                            binding.tilEditText.error = null
                        }

                    }

                }
            }
            binding.et.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    binding.et.addTextChangedListener(textWatcher)
                else
                    binding.et.removeTextChangedListener(textWatcher)
            }
            item.errorText?.also { binding.tilEditText.error = it }
                ?: run { binding.tilEditText.error = null }
            val etFilters = mutableListOf<InputFilter>(InputFilter.LengthFilter(item.etMaxLength))
            binding.et.inputType = item.etInputType
            if (item.etInputType == InputType.TYPE_CLASS_TEXT && item.allCaps) {
                etFilters.add(AllCaps())
                etFilters.add(FormEditTextDefaultInputFilter)

            }
//            else if(item.etInputType == InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
//                etFilters.add(DecimalDigitsInputFilter)
            binding.et.filters = etFilters.toTypedArray()
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

        fun bind(item: FormInput, isEnabled : Boolean) {
            if(!isEnabled){
                binding.form = item
                binding.tilRvDropdown.visibility = View.GONE
                binding.tilEditText.visibility = View.VISIBLE
                binding.et.isFocusable = false
                binding.et.isClickable = false
//                binding.clContent.isClickable = false

                binding.executePendingBindings()
                return
            }
            val savedValue = item.value.value
            item.value.value = null
            item.value.value = savedValue
            binding.form = item

            binding.actvRvDropdown.setOnItemClickListener { _, _, index, _ ->
                item.value.value = item.entries?.get(index)
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
            item: FormInput, isEnabled: Boolean) {
            if(!isEnabled){
                binding.rg.isClickable = false
                binding.rg.isFocusable = false
            }
//            binding.rg.isEnabled = isEnabled
            binding.invalidateAll()
            binding.form = item

            binding.rg.removeAllViews()
            binding.rg.apply {
                item.entries?.let { items ->
                    orientation = item.orientation ?: LinearLayout.HORIZONTAL
                    weightSum = items.size.toFloat()
                    items.forEach {
                        val rdBtn = RadioButton(this.context)
                        rdBtn.layoutParams =
                            RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.WRAP_CONTENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT,
                                1.0F
                            ).apply {
                                gravity = Gravity.CENTER_HORIZONTAL
                            }
                        rdBtn.id = View.generateViewId()

                        rdBtn.text = it
                        addView(rdBtn)
                        if (item.value.value == it)
                            rdBtn.isChecked = true
                        rdBtn.setOnCheckedChangeListener { _, b ->
                            if (b) {
                                item.value.value = it
                            }
                            item.errorText = null
                            binding.clRi.setBackgroundResource(0)
                        }
                    }
                    item.value.value?.let { value ->
                        children.forEach {
                            if ((it as RadioButton).text == value) {
                                clearCheck()
                                check(it.id)
                            }
                        }
                    }
                }
            }



            if(!isEnabled){
                binding.rg.children.forEach {
                    it.isClickable = false
                }
            }
            if (item.errorText != null)
                binding.clRi.setBackgroundResource(R.drawable.state_errored)
            else
                binding.clRi.setBackgroundResource(0)

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
            item: FormInput
        ) {
            binding.form = item
            if (item.errorText != null)
                binding.clRi.setBackgroundResource(R.drawable.state_errored)
            else
                binding.clRi.setBackgroundResource(0)
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
            item: FormInput, isEnabled: Boolean) {
            binding.form = item
            if(!isEnabled){
                binding.et.isFocusable = false
                binding.et.isClickable = false
                binding.executePendingBindings()
                return
            }
            val today = Calendar.getInstance()
            var thisYear = today.get(Calendar.YEAR)
            var thisMonth = today.get(Calendar.MONTH)
            var thisDay = today.get(Calendar.DAY_OF_MONTH)

            item.errorText?.also { binding.tilEditText.error = it }
                ?: run { binding.tilEditText.error = null }
            binding.et.setOnClickListener {
                item.value.value?.let { value ->
                    thisYear = value.substring(6).toInt()
                    thisMonth = value.substring(3, 5).trim().toInt() - 1
                    thisDay = value.substring(0, 2).trim().toInt()
                }
                val datePickerDialog = DatePickerDialog(
                    it.context,
                    { _, year, month, day ->
                        item.value.value =
                            "${if (day > 9) day else "0$day"}-${if (month > 8) month + 1 else "0${month + 1}"}-$year"
                        binding.invalidateAll()
                    }, thisYear, thisMonth, thisDay
                )
                item.errorText = null
                binding.tilEditText.error = null
                datePickerDialog.datePicker.maxDate = item.max ?: 0
                datePickerDialog.datePicker.minDate = item.min ?: 0
                datePickerDialog.datePicker.touchables[0].performClick()
                datePickerDialog.show()
            }
            binding.executePendingBindings()

        }
    }

    class TimePickerInputViewHolder private constructor(private val binding: RvItemTimepickerBinding) :
        ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemTimepickerBinding.inflate(layoutInflater, parent, false)
                return TimePickerInputViewHolder(binding)
            }
        }

        fun bind(
            item: FormInput, isEnabled: Boolean
        ) {
            binding.form = item
            binding.et.isEnabled = isEnabled
            binding.et.setOnClickListener {
                val hour: Int
                val minute: Int
                if(item.value.value==null) {
                    val currentTime = Calendar.getInstance();
                    hour = currentTime.get(Calendar.HOUR_OF_DAY);
                    minute = currentTime.get(Calendar.MINUTE);
                }else{
                    hour = item.value.value!!.substringBefore(":").toInt()
                    minute = item.value.value!!.substringAfter(":").toInt()
                    Timber.d("Time picker hour min : $hour $minute")
                }
                    val mTimePicker = TimePickerDialog(it.context, {
                            _, hourOfDay, minuteOfHour ->
                        item.value.value =
                            "$hourOfDay:$minuteOfHour"
                        binding.invalidateAll()

                }, hour, minute, false );
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
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

    class ImageViewInputViewHolder private constructor(private val binding: RvItemImageViewBinding) :
        ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemImageViewBinding.inflate(layoutInflater, parent, false)
                return ImageViewInputViewHolder(binding)
            }
        }

        fun bind(
            item: FormInput,
            clickListener: ImageClickListener?,
            isEnabled: Boolean
        ) {
            binding.form = item
            if(isEnabled) {
                binding.clickListener = clickListener
                if (item.errorText != null)
                    binding.clRi.setBackgroundResource(R.drawable.state_errored)
                else
                    binding.clRi.setBackgroundResource(0)
            }
            binding.executePendingBindings()

        }
    }


    class HeadlineViewHolder private constructor(private val binding: RvItemHeadlineBinding) :
        ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemHeadlineBinding.inflate(layoutInflater, parent, false)
                return HeadlineViewHolder(binding)
            }
        }

        fun bind(
            item: FormInput,
        ) {
            binding.form = item
            binding.executePendingBindings()

        }
    }

    class ImageClickListener(private val imageClick: (form: FormInput) -> Unit) {

        fun onImageClick(form: FormInput) = imageClick(form)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inputTypes = values()
        return when (inputTypes[viewType]) {
            EDIT_TEXT -> EditTextInputViewHolder.from(parent)
            DROPDOWN -> DropDownInputViewHolder.from(parent)
            RADIO -> RadioInputViewHolder.from(parent)
            DATE_PICKER -> DatePickerInputViewHolder.from(parent)
            TEXT_VIEW -> TextViewInputViewHolder.from(parent)
            IMAGE_VIEW -> ImageViewInputViewHolder.from(parent)
            CHECKBOXES -> CheckBoxesInputViewHolder.from(parent)
            TIME_PICKER -> TimePickerInputViewHolder.from(parent)
            HEADLINE -> HeadlineViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.inputType) {
            EDIT_TEXT -> (holder as EditTextInputViewHolder).bind(item, isEnabled)
            DROPDOWN -> (holder as DropDownInputViewHolder).bind(item, isEnabled)
            RADIO -> (holder as RadioInputViewHolder).bind(item, isEnabled)
            DATE_PICKER -> (holder as DatePickerInputViewHolder).bind(item, isEnabled)
            TEXT_VIEW -> (holder as TextViewInputViewHolder).bind(item)
            IMAGE_VIEW -> (holder as ImageViewInputViewHolder).bind(item, imageClickListener, isEnabled)
            CHECKBOXES -> (holder as CheckBoxesInputViewHolder).bind(item)
            TIME_PICKER -> (holder as TimePickerInputViewHolder).bind(item, isEnabled)
            HEADLINE -> (holder as HeadlineViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) =
        getItem(position).inputType.ordinal

    /**
     * Validation Result : -1 -> all good
     * else index of element creating trouble
     */
    fun validateInput(): Int {
        var retVal = -1
        if(!isEnabled)
            return retVal
        currentList.forEach {
            Timber.d("Error text for ${it.title} ${it.errorText}")
            if (it.errorText != null) {
                retVal = currentList.indexOf(it)
                return@forEach
            }
        }
        Timber.d("Validation : $retVal")
        if (retVal != -1)
            return retVal
        currentList.forEach {
            if (it.required) {
                if (it.value.value.isNullOrBlank()) {
                    Timber.d("validateInput called for item $it, with index ${currentList.indexOf(it)}")
                    it.errorText = "Required field cannot be empty !"
                    notifyItemChanged(currentList.indexOf(it))
                    if (retVal == -1)
                        retVal = currentList.indexOf(it)
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