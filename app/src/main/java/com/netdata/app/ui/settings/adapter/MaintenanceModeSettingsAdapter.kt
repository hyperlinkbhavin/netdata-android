package com.netdata.app.ui.settings.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.RowItemMaintenanceModeSettingsBinding
import com.netdata.app.utils.gone
import com.netdata.app.utils.visible

class MaintenanceModeSettingsAdapter(val callBack: (View, Int, WarRoomsList) -> Unit) : RecyclerView.Adapter<MaintenanceModeSettingsAdapter.ViewHolder>() {

    var list = ArrayList<WarRoomsList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemMaintenanceModeSettingsBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemMaintenanceModeSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                switchDisableAllNotifications.setOnClickListener {
                    Log.e("click", list[adapterPosition].isSelected.toString())
                    list[adapterPosition].isSelected = !list[adapterPosition].isSelected
                    callBack.invoke(constraintDisableNotifications, adapterPosition, list[adapterPosition])
                    notifyDataSetChanged()
                }

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: WarRoomsList) = with(binding) {
            Log.e("isSelected", item.isSelected.toString())
            if(item.isSelected){
                constraintDisableNotifications.isSelected = true
                switchDisableAllNotifications.isChecked = true
                radioGroupAllNotifications.visible()
            } else {
                constraintDisableNotifications.isSelected = false
                switchDisableAllNotifications.isChecked = false
                radioGroupAllNotifications.gone()
            }
            textViewDisableAllNotification.text = item.name
        }

    }

}