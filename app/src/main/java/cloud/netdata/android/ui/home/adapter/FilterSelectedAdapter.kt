package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.request.FilterSelectedList
import cloud.netdata.android.databinding.RowItemFilterSelectedBinding

class FilterSelectedAdapter(val callBack: (View, Int, FilterSelectedList) -> Unit) : RecyclerView.Adapter<FilterSelectedAdapter.ViewHolder>() {

    var list = ArrayList<FilterSelectedList>()
    var selectionPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemFilterSelectedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(val binding: RowItemFilterSelectedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                imageViewClose.setOnClickListener {
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: FilterSelectedList) = with(binding) {
            when(item.id){
                1 -> textViewFilterName.text = "Notification Type:${item.name}"
                2 -> textViewFilterName.text = "Node:${item.name}"
                3 -> textViewFilterName.text = "Alert Status:${item.name}"
                4 -> textViewFilterName.text = "Priority:${item.name}"
                5 -> textViewFilterName.text = "Class:${item.name}"
                6 -> textViewFilterName.text = "Type & Component:${item.name}"
            }
        }

    }

}