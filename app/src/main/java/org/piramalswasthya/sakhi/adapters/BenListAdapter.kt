package org.piramalswasthya.sakhi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.databinding.RvItemBenBinding
import org.piramalswasthya.sakhi.model.BenBasicDomain


class BenListAdapter(private val clickListener: BenClickListener) :
    ListAdapter<BenBasicDomain, BenListAdapter.BenViewHolder>
        (BenDiffUtilCallBack) {
    private object BenDiffUtilCallBack : DiffUtil.ItemCallback<BenBasicDomain>() {
        override fun areItemsTheSame(
            oldItem: BenBasicDomain,
            newItem: BenBasicDomain
        ) = oldItem.benId == newItem.benId

        override fun areContentsTheSame(
            oldItem: BenBasicDomain,
            newItem: BenBasicDomain
        ) = oldItem == newItem

    }

    class BenViewHolder private constructor(private val binding: RvItemBenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): BenViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemBenBinding.inflate(layoutInflater, parent, false)
                return BenViewHolder(binding)
            }
        }

        fun bind(
            item: BenBasicDomain,
            clickListener: BenClickListener
        ) {
            binding.ben = item
            binding.clickListener = clickListener
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) =
        BenViewHolder.from(parent)

    override fun onBindViewHolder(holder: BenViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }


    class BenClickListener(
        private val selectedBen: (benId: Long) -> Unit,
        private val selectedHousehold: (hhId: Long) -> Unit
    ) {
        fun onClickedBen(item: BenBasicDomain) = selectedBen(item.benId)
        fun onClickedHouseHold(item: BenBasicDomain) = selectedHousehold(item.hhId)
    }

}