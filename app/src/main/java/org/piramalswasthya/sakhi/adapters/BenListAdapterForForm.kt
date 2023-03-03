package org.piramalswasthya.sakhi.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.R
import org.piramalswasthya.sakhi.databinding.RvItemBenWithFormBinding
import org.piramalswasthya.sakhi.model.BenBasicDomainForForm

class BenListAdapterForForm(private val clickListener: ClickListener? = null,
                            private val formButtonText: String) :
    ListAdapter<BenBasicDomainForForm, BenListAdapterForForm.BenViewHolder>
        (BenDiffUtilCallBack) {
    private object BenDiffUtilCallBack : DiffUtil.ItemCallback<BenBasicDomainForForm>() {
        override fun areItemsTheSame(
            oldItem: BenBasicDomainForForm,
            newItem: BenBasicDomainForForm
        ) = oldItem.benId == newItem.benId

        override fun areContentsTheSame(
            oldItem: BenBasicDomainForForm,
            newItem: BenBasicDomainForForm
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
            item: BenBasicDomainForForm,
            clickListener: ClickListener?,
            btnText: String,
        ) {
            binding.ben = item
            binding.clickListener = clickListener
            binding.button2.text = btnText
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(item.hasForm)
                    binding.button2.setBackgroundColor(binding.root.resources.getColor(R.color.green, binding.root.context.theme))
                else
                    binding.button2.setBackgroundColor(binding.root.resources.getColor(R.color.red, binding.root.context.theme))
            }
            else
                if(item.hasForm)
                    binding.button2.setBackgroundColor(binding.root.resources.getColor(R.color.green))
                else
                    binding.button2.setBackgroundColor(binding.root.resources.getColor(R.color.red))
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
        private val clickedSync: () -> Unit,
        private val clickedButton: (hhId: Long, benId: Long) -> Unit
    ) {
        fun onClickedBen(item: BenBasicDomainForForm) = clickedBen(item.benId)
        fun onClickedHouseHold(item: BenBasicDomainForForm) = clickedHousehold(item.hhId)
        fun onClickSync(item: BenBasicDomainForForm) = clickedSync()
        fun onClickButton(item: BenBasicDomainForForm) = clickedButton(item.hhId, item.benId)
    }

}