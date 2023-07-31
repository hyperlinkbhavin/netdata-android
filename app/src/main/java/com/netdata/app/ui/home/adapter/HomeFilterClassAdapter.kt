package com.netdata.app.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netdata.app.data.pojo.request.AlertInfoList
import com.netdata.app.data.pojo.request.FilterList
import com.netdata.app.data.pojo.response.HomeNotificationList
import com.netdata.app.databinding.RowItemAlertInfoBinding
import com.netdata.app.databinding.RowItemFilterCheckboxBinding
import com.netdata.app.utils.gone
import com.netdata.app.utils.visible

class HomeFilterClassAdapter(val list: ArrayList<HomeNotificationList.Data.Alarm>, val callBack: (View, Int, HomeNotificationList.Data.Alarm) -> Unit) : RecyclerView.Adapter<HomeFilterClassAdapter.ViewHolder>() {

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
                    list[absoluteAdapterPosition].isSelected = !list[absoluteAdapterPosition].isSelected
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: HomeNotificationList.Data.Alarm) = with(binding) {
            textViewFilterName.text = item.classification
            checkBoxFilter.isChecked = item.isSelected
            textViewFilterCount.gone()

        }

    }

}