package org.piramalswasthya.sakhi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.databinding.RvItemNcdCbacBinding
import org.piramalswasthya.sakhi.model.BenWithCbacDomain

class NcdCbacBenListAdapter(
    private val clickListener: CbacFormClickListener
) : ListAdapter<BenWithCbacDomain, NcdCbacBenListAdapter.BenCbacViewHolder>(
    BenDiffUtilCallBack
) {
    private object BenDiffUtilCallBack : DiffUtil.ItemCallback<BenWithCbacDomain>() {
        override fun areItemsTheSame(
            oldItem: BenWithCbacDomain, newItem: BenWithCbacDomain
        ) = oldItem.ben.benId == newItem.ben.benId

        override fun areContentsTheSame(
            oldItem: BenWithCbacDomain, newItem: BenWithCbacDomain
        ) = oldItem == newItem

    }

    class BenCbacViewHolder private constructor(private val binding: RvItemNcdCbacBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): BenCbacViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemNcdCbacBinding.inflate(layoutInflater, parent, false)
                return BenCbacViewHolder(binding)
            }
        }

        fun bind(
            item: BenWithCbacDomain, clickListener: CbacFormClickListener?
        ) {
            binding.benWithCbac = item
            binding.clickListener = clickListener
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BenCbacViewHolder = BenCbacViewHolder.from(parent)

    override fun onBindViewHolder(holder: BenCbacViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }


    class CbacFormClickListener(
        private val clickedView: (benId: Long) -> Unit,
        private val clickedNew: (benId: Long) -> Unit,

        ) {
        fun onClickedView(item: BenWithCbacDomain) = clickedView(
            item.ben.benId
        )

        fun onClickedNew(item: BenWithCbacDomain) = clickedNew(
            item.ben.benId
        )


    }

}