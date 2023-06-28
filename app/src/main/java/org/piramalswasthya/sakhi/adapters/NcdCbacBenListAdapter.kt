package org.piramalswasthya.sakhi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.databinding.RvItemNcdCbacBinding
import org.piramalswasthya.sakhi.model.BenBasicDomain

class NcdCbacBenListAdapter(
    private val clickListener: CbacFormClickListener? = null
) : ListAdapter<BenBasicDomain, NcdCbacBenListAdapter.BenCbacViewHolder>(
    BenDiffUtilCallBack
) {
    private object BenDiffUtilCallBack : DiffUtil.ItemCallback<BenBasicDomain>() {
        override fun areItemsTheSame(
            oldItem: BenBasicDomain, newItem: BenBasicDomain
        ) = oldItem.benId == newItem.benId

        override fun areContentsTheSame(
            oldItem: BenBasicDomain, newItem: BenBasicDomain
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
            item: BenBasicDomain, clickListener: CbacFormClickListener?
        ) {
            binding.ben = item
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
        private val clickedBen: (benId: Long) -> Unit,

        ) {
        fun onClickedBen(item: BenBasicDomain) = clickedBen(
            item.benId
        )

    }

}