package org.piramalswasthya.sakhi.adapters

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GetBenPageNumberAdapter(private val maxPageNumber: Int, private val clickListener: PageClickListener) :
    RecyclerView.Adapter<GetBenPageNumberAdapter.PageNumberViewHolder>() {

    class PageNumberViewHolder private constructor(private val textView: TextView) :
        RecyclerView.ViewHolder(textView) {

        companion object {
            fun from(parent: ViewGroup): PageNumberViewHolder {
                val textView = TextView(parent.context)
                return PageNumberViewHolder(textView)
            }
        }

        fun bind(
            item: Int,
            clickListener: PageClickListener
        ) {
            textView.text = item.toString()
            textView.setOnClickListener{clickListener}
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) =
        PageNumberViewHolder.from(parent)

    override fun onBindViewHolder(holder: PageNumberViewHolder, position: Int) {
        holder.bind(position+1, clickListener)
    }


    class PageClickListener(
        private val selectedPage: (page: Int) -> Unit,
    ) {
        fun onClickedBen(item: Int) = selectedPage(item)
    }

    override fun getItemCount(): Int {
        return maxPageNumber
    }
}