package org.piramalswasthya.sakhi.adapters

import android.text.InputFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.piramalswasthya.sakhi.configuration.FormEditTextDefaultInputFilter
import org.piramalswasthya.sakhi.databinding.RvItemDropdownBinding
import org.piramalswasthya.sakhi.databinding.RvItemEditTextBinding
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.FormInput.InputType.*
import org.piramalswasthya.sakhi.ui.afterTextChanged
import timber.log.Timber

class FormInputAdapter : ListAdapter<FormInput, ViewHolder>(FormInputDiffCallBack) {
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
            binding.field = item.title
            binding.required = item.required
            binding.et.afterTextChanged {
                item.value = it
                Timber.d("Item ET : $item")
                if (item.errorText != null && !it.isNullOrBlank()) {
                    item.errorText = null
                    binding.tilEditText.error = null
                }

            }
            item.errorText?.let { binding.tilEditText.error = it }
            val etFilters = mutableListOf<InputFilter>(InputFilter.LengthFilter(item.etLength))
            item.etInputType?.let { binding.et.inputType = it }
            if (item.etInputType == null && item.useFormEditTextDefaultInputFilter) {
                etFilters.add(FormEditTextDefaultInputFilter)
            }

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

        fun bind(
            item: FormInput,
            hidden: (hiddenForm: FormInput?, show: Boolean) -> Unit
        ) {
            binding.field = item.title
            binding.required = item.required
            binding.listItems = item.list
            binding.actvRvDropdown.setOnItemClickListener { _, _, index, _ ->
                if(item.list!![index]==item.value)
                    return@setOnItemClickListener
                item.hiddenFieldTrigger?.let {
                    if (it == item.value && it != item.list[index]) {
                        hidden(item.hiddenField, false)
                    }
                }
                item.value = item.list!![index]
                if (item.value == item.hiddenFieldTrigger) {
                    hidden(item.hiddenField, true)
                }
                Timber.d("Item DD : $item")
                item.errorText = null
                binding.tilRvDropdown.error = null
            }

            item.errorText?.let { binding.tilRvDropdown.error = it }
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inputTypes = values()
        return when (inputTypes[viewType]) {
            EDIT_TEXT -> EditTextInputViewHolder.from(parent)
            DROPDOWN -> DropDownInputViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val field = getItem(position)
        when (field.inputType) {
            EDIT_TEXT -> (holder as EditTextInputViewHolder).bind(field)
            DROPDOWN -> {
                (holder as DropDownInputViewHolder).bind(field)
                { form, show ->
                    val currentPosition = currentList.indexOf(field)
                    val list = currentList.toMutableList()
                    if (show) {
                        list.add(currentPosition + 1, form)
                        submitList(list)
                    } else {
                        list.remove(form)
                        submitList(list)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int) =
        getItem(position).inputType.ordinal


    fun validateInput(): Boolean {
        var retVal = true
        currentList.forEach {
            if (it.required) {
                if (it.value.isNullOrBlank()) {
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