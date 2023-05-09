package com.netdata.app.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netdata.app.data.pojo.request.AlertInfoList
import com.netdata.app.data.pojo.request.ChooseSpaceList
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.RowItemAlertInfoBinding
import com.netdata.app.databinding.RowItemChooseSpaceBinding
import com.netdata.app.databinding.RowItemSelectWarRoomsBinding
import com.netdata.app.databinding.RowItemTimeValuesBinding
import com.netdata.app.utils.gone
import com.netdata.app.utils.invisible
import com.netdata.app.utils.visible

class AlertInfoAdapter(val callBack: (View, Int, AlertInfoList) -> Unit) : RecyclerView.Adapter<AlertInfoAdapter.ViewHolder>() {

    var list = ArrayList<AlertInfoList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemAlertInfoBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemAlertInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintTop.setOnClickListener {
                    callBack.invoke(it, adapterPosition, list[adapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: AlertInfoList) = with(binding) {
            imageViewAlertInfo.setImageResource(item.icon)
            textViewAlertInfoName.text = item.name
            textViewAlertInfoValue.text=  item.value
        }

    }

}