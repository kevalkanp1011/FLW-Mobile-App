package org.piramalswasthya.sakhi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.databinding.RvItemNcdCbacElementBinding
import org.piramalswasthya.sakhi.model.CbacCache

class NcdCbacAdapter(private val clickListener: NcdCbacElementClickListener) :
    ListAdapter<CbacCache, NcdCbacAdapter.NcdCbacElementViewHolder>(
        ImmunizationIconDiffCallback
    ) {
    object ImmunizationIconDiffCallback : DiffUtil.ItemCallback<CbacCache>() {
        override fun areItemsTheSame(
            oldItem: CbacCache,
            newItem: CbacCache
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CbacCache,
            newItem: CbacCache
        ) =
            (oldItem == newItem)

    }


    class NcdCbacElementViewHolder private constructor(private val binding: RvItemNcdCbacElementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): NcdCbacElementViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemNcdCbacElementBinding.inflate(layoutInflater, parent, false)
                return NcdCbacElementViewHolder(binding)
            }
        }

        fun bind(item: CbacCache, clickListener: NcdCbacElementClickListener) {
            binding.cbac = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NcdCbacElementViewHolder.from(parent)

    override fun onBindViewHolder(holder: NcdCbacElementViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class NcdCbacElementClickListener(val selectedListener: (cbacId: Int) -> Unit) {
        fun onClicked(cbac: CbacCache) = selectedListener(cbac.id)

    }
}