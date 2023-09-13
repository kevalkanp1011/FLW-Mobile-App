package org.piramalswasthya.sakhi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.databinding.RvItemBenBinding
import org.piramalswasthya.sakhi.model.BenBasicDomain


class BenListAdapter(
    private val clickListener: BenClickListener? = null,
    private val showAddBeneficiaries: Boolean = false,
    private val showSyncIcon: Boolean = false,
    private val showAbha: Boolean = false
) :
    ListAdapter<BenBasicDomain, BenListAdapter.BenViewHolder>(BenDiffUtilCallBack) {
    private object BenDiffUtilCallBack : DiffUtil.ItemCallback<BenBasicDomain>() {
        override fun areItemsTheSame(
            oldItem: BenBasicDomain, newItem: BenBasicDomain
        ) = oldItem.benId == newItem.benId

        override fun areContentsTheSame(
            oldItem: BenBasicDomain, newItem: BenBasicDomain
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
            clickListener: BenClickListener?,
            showAbha: Boolean,
            showSyncIcon: Boolean
        ) {
            binding.ben = item
            binding.clickListener = clickListener
            binding.showAbha = showAbha
            binding.ivSyncState.visibility = if(showSyncIcon) View.VISIBLE else View.INVISIBLE
            binding.hasAbha = !item.abhaId.isNullOrEmpty()
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ) = BenViewHolder.from(parent)

    override fun onBindViewHolder(holder: BenViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, showAbha, showSyncIcon)
    }


    class BenClickListener(
        private val clickedBen: (hhId: Long, benId: Long, relToHeadId: Int) -> Unit,
        private val clickedHousehold: (hhId: Long) -> Unit,
        private val clickedABHA: (benId: Long, hhId: Long) -> Unit,
    ) {
        fun onClickedBen(item: BenBasicDomain) = clickedBen(
            item.hhId,
            item.benId,
            item.relToHeadId -1
        )

        fun onClickedHouseHold(item: BenBasicDomain) = clickedHousehold(item.hhId)

        fun onClickABHA(item: BenBasicDomain) = clickedABHA(item.benId, item.hhId)
    }

}