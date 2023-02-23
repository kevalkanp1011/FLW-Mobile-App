package org.piramalswasthya.sakhi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.databinding.RvItemBenWithFormBinding
import org.piramalswasthya.sakhi.model.BenBasicDomain

class BenListAdapterForForm(private val clickListener: ClickListener? = null,
                            private val formButtonText: String) :
    ListAdapter<BenBasicDomain, BenListAdapterForForm.BenViewHolder>
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

    class BenViewHolder private constructor(private val binding: RvItemBenWithFormBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): BenViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemBenWithFormBinding.inflate(layoutInflater, parent, false)
                return BenViewHolder(binding)
            }
        }

        fun bind(
            item: BenBasicDomain,
            clickListener: ClickListener?,
            btnText: String,
        ) {
            binding.ben = item
            binding.clickListener = clickListener
            binding.button2.text = btnText
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) =
        BenViewHolder.from(parent)

    override fun onBindViewHolder(holder: BenViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, btnText = formButtonText)
    }


    class ClickListener(
        private val clickedBen: (benId: Long) -> Unit,
        private val clickedHousehold: (hhId: Long) -> Unit,
        private val clickedSync: (hhId: Long, benId: Long) -> Unit,
        private val clickedButton: (hhId: Long, benId: Long) -> Unit
    ) {
        fun onClickedBen(item: BenBasicDomain) = clickedBen(item.benId)
        fun onClickedHouseHold(item: BenBasicDomain) = clickedHousehold(item.hhId)
        fun onClickSync(item: BenBasicDomain) = clickedSync(item.hhId, item.benId)
        fun onClickButton(item: BenBasicDomain) = clickedButton(item.hhId, item.benId)
    }

}