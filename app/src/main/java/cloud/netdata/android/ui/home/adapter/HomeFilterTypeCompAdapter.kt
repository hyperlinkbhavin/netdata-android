package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.request.FilterList
import cloud.netdata.android.databinding.RowItemFilterCheckboxBinding
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.visible

class HomeFilterTypeCompAdapter(var list: ArrayList<FilterList>, val callBack: (View, Int, FilterList) -> Unit) : RecyclerView.Adapter<HomeFilterTypeCompAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemFilterCheckboxBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemFilterCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                checkBoxFilter.setOnClickListener {
                    /*list[absoluteAdapterPosition].isSelected = !list[absoluteAdapterPosition].isSelected
                    notifyItemChanged(absoluteAdapterPosition)*/
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                }
                textViewFilterName.setOnClickListener {
                    checkBoxFilter.performClick()
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: FilterList) = with(binding) {
            if(Constant.isReachable){
                textViewFilterName.alpha = 0.2F
                checkBoxFilter.alpha = 0.2F
                textViewFilterCount.alpha = 0.2F
                checkBoxFilter.isClickable = false
                checkBoxFilter.isFocusable = false
            } else {
                textViewFilterName.alpha = 1F
                checkBoxFilter.alpha = 1F
                textViewFilterCount.alpha = 1F
                checkBoxFilter.isClickable = true
                checkBoxFilter.isFocusable = true
            }
            textViewFilterName.text = item.name
            checkBoxFilter.isChecked = item.isSelected
            textViewFilterCount.visible()
            textViewFilterCount.text = item.count.toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(filterList: ArrayList<FilterList>) {
        list = filterList
        notifyDataSetChanged()
    }
}