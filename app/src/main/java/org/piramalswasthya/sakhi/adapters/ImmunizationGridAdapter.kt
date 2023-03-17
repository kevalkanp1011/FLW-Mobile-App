package org.piramalswasthya.sakhi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.databinding.RvItemIconImmunizationBinding
import org.piramalswasthya.sakhi.model.ImmunizationIcon

class ImmunizationGridAdapter (private val clickListener: ImmunizationIconClickListener) :
    ListAdapter<ImmunizationIcon, ImmunizationGridAdapter.IconViewHolder>(ImmunizationIconDiffCallback) {
    object ImmunizationIconDiffCallback : DiffUtil.ItemCallback<ImmunizationIcon>() {
        override fun areItemsTheSame(oldItem: ImmunizationIcon, newItem: ImmunizationIcon) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: ImmunizationIcon, newItem: ImmunizationIcon) =
            (oldItem == newItem)

    }


    class IconViewHolder private constructor(private val binding: RvItemIconImmunizationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup) : IconViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemIconImmunizationBinding.inflate(layoutInflater,parent,false)
                return IconViewHolder(binding)
            }
        }

        fun bind(item: ImmunizationIcon, clickListener: ImmunizationIconClickListener){
            binding.homeIcon = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        IconViewHolder.from(parent)

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ImmunizationIconClickListener(val selectedListener: (vaccine: String, count: Int, maxCount: Int, benId: Long, hhId: Long) -> Unit) {
        fun onClicked(icon : ImmunizationIcon) = selectedListener(icon.title, icon.count, icon.maxCount, icon.benId, icon.hhId)

    }
}