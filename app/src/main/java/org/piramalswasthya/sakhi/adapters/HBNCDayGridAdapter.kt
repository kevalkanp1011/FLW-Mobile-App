package org.piramalswasthya.sakhi.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.RvItemIconHbncBinding
import org.piramalswasthya.sakhi.model.HbncIcon
import org.piramalswasthya.sakhi.model.ImmunizationIcon

class HBNCDayGridAdapter(private val clickListener: HbncIconClickListener) :
    ListAdapter<HbncIcon, HBNCDayGridAdapter.IconViewHolder>(HbncIconDiffCallback) {
    object HbncIconDiffCallback : DiffUtil.ItemCallback<HbncIcon>() {
        override fun areItemsTheSame(oldItem: HbncIcon, newItem: HbncIcon) =
            oldItem.count == newItem.count

        override fun areContentsTheSame(oldItem: HbncIcon, newItem: HbncIcon) =
            (oldItem == newItem)

    }


    class IconViewHolder private constructor(private val binding: RvItemIconHbncBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): IconViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemIconHbncBinding.inflate(layoutInflater, parent, false)
                return IconViewHolder(binding)
            }
        }

        fun bind(item: HbncIcon, clickListener: HbncIconClickListener) {
            binding.homeIcon = item
            binding.clickListener = clickListener
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (item.isFilled)
                    binding.cvIcon.setBackgroundColor(
                        binding.root.resources.getColor(
                            R.color.green,
                            binding.root.context.theme
                        )
                    )
                else
                    binding.cvIcon.setBackgroundColor(
                        binding.root.resources.getColor(
                            R.color.red,
                            binding.root.context.theme
                        )
                    )
            } else
                if (item.isFilled)
                    binding.cvIcon.setBackgroundColor(binding.root.resources.getColor(R.color.green))
                else
                    binding.cvIcon.setBackgroundColor(binding.root.resources.getColor(R.color.red))
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        IconViewHolder.from(parent)

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class HbncIconClickListener(val selectedListener: (hhId: Long, benId: Long, count: Int) -> Unit) {
        fun onClicked(icon: HbncIcon) = selectedListener(icon.hhId, icon.benId, icon.count)

    }
}