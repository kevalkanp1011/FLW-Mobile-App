package org.piramalswasthya.sakhi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.databinding.RvItemImmunizationBinding
import org.piramalswasthya.sakhi.model.ImmunizationDetailsDomain
import org.piramalswasthya.sakhi.model.VaccineClickListener

class VaccineListAdapter(private val clickListener: VaccineClickListener) :
    ListAdapter<ImmunizationDetailsDomain, VaccineListAdapter.ImmViewHolder>(BenDiffUtilCallBack) {
    private object BenDiffUtilCallBack : DiffUtil.ItemCallback<ImmunizationDetailsDomain>() {
        override fun areItemsTheSame(
            oldItem: ImmunizationDetailsDomain, newItem: ImmunizationDetailsDomain
        ) = oldItem.benId == newItem.benId

        override fun areContentsTheSame(
            oldItem: ImmunizationDetailsDomain, newItem: ImmunizationDetailsDomain
        ) = oldItem == newItem

    }

    class ImmViewHolder private constructor(private val binding: RvItemImmunizationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ImmViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemImmunizationBinding.inflate(layoutInflater, parent, false)
                return ImmViewHolder(binding)
            }
        }

        fun bind(
            item: ImmunizationDetailsDomain,
            clickListener: VaccineClickListener,
//            clickListener: BenClickListener?
        ) {
            binding.temp = item
            binding.clickListener = clickListener
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ) = ImmViewHolder.from(parent)

    override fun onBindViewHolder(holder: ImmViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }


//    class BenClickListener(
//        private val clickedBen: (hhId: Long, benId : Long, isKid : Boolean) -> Unit,
//        private val clickedHousehold: (hhId: Long) -> Unit
//    ) {
//        fun onClickedBen(item: BenBasicDomain) = clickedBen(item.hhId, item.benId, item.typeOfList== TypeOfList.CHILD.name || item.typeOfList == TypeOfList.INFANT.name || item.typeOfList== TypeOfList.ADOLESCENT.name)
//        fun onClickedHouseHold(item: BenBasicDomain) = clickedHousehold(item.hhId)
//    }

}