package org.piramalswasthya.sakhi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.databinding.RvItemIconGridBinding
import org.piramalswasthya.sakhi.model.Icon

class HomeIconAdapter(private val iconList : List<Icon>, private val clickListener: HomeIconClickListener) : RecyclerView.Adapter<HomeIconAdapter.HomeIconViewHolder>() {



    class HomeIconViewHolder private constructor(private val binding : RvItemIconGridBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun from(parent: ViewGroup) : HomeIconViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemIconGridBinding.inflate(layoutInflater,parent,false)
                return HomeIconViewHolder(binding)
            }
        }

        fun bind(item: Icon, clickListener: HomeIconClickListener){
            binding.homeIcon = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HomeIconViewHolder.from(parent)

    override fun onBindViewHolder(holder: HomeIconViewHolder, position: Int) {
        holder.bind(iconList[position],clickListener)
    }

    override fun getItemCount() = iconList.size

    class HomeIconClickListener(val selectedListener: (dest : NavDirections) -> Unit) {
        fun onClicked(icon : Icon) = selectedListener(icon.navAction)

    }
}