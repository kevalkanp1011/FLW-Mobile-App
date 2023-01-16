package org.piramalswasthya.sakhi.adapters

import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.piramalswasthya.sakhi.databinding.RvItemDropdownBinding
import org.piramalswasthya.sakhi.databinding.RvItemEditTextBinding
import org.piramalswasthya.sakhi.model.FormInput
import org.piramalswasthya.sakhi.model.FormInput.InputType.*
import org.piramalswasthya.sakhi.ui.afterTextChanged
import timber.log.Timber

class FormInputAdapter : ListAdapter<FormInput, ViewHolder>(FormInputDiffCallBack) {
    object FormInputDiffCallBack : DiffUtil.ItemCallback<FormInput>(){
        override fun areItemsTheSame(oldItem: FormInput, newItem: FormInput)
            = oldItem.title == newItem.title
        override fun areContentsTheSame(oldItem: FormInput, newItem: FormInput)
        = oldItem == newItem

    }
    class EditTextInputViewHolder private constructor(private val binding : RvItemEditTextBinding) : ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemEditTextBinding.inflate(layoutInflater,parent,false)
                return EditTextInputViewHolder(binding)
            }
        }
        fun bind(item: FormInput) {
            binding.field = item.title
            binding.required = item.required
            binding.et.afterTextChanged {
                item.value = it
            }
            binding.executePendingBindings()
        }
    }
    class DropDownInputViewHolder private constructor(private val binding : RvItemDropdownBinding) : ViewHolder(binding.root){
        companion object{
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemDropdownBinding.inflate(layoutInflater,parent,false)
                return DropDownInputViewHolder(binding)
            }
        }
        fun bind(item: FormInput) {
            binding.field = item.title
            binding.required = item.required
            binding.listItems = item.list
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inputTypes = values()
        return when(inputTypes[viewType]){
            EDIT_TEXT -> EditTextInputViewHolder.from(parent)
            DROPDOWN -> DropDownInputViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val field = getItem(position)
        when(field.inputType){
            EDIT_TEXT -> (holder as EditTextInputViewHolder).bind(field)
            DROPDOWN -> (holder as DropDownInputViewHolder).bind(field)
        }
    }

    override fun getItemViewType(position: Int) =
        getItem(position).inputType.ordinal
}