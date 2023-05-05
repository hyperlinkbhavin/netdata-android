package com.netdata.app.ui.settings.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.netdata.app.R
import com.netdata.app.data.pojo.enumclass.Priority
import com.netdata.app.data.pojo.request.NotificationsList
import com.netdata.app.data.pojo.request.SettingsList
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.RowItemNotificationBinding
import com.netdata.app.databinding.RowItemSelectWarRoomsBinding
import com.netdata.app.databinding.RowItemSettingsBinding
import com.netdata.app.utils.gone
import com.netdata.app.utils.invisible
import com.netdata.app.utils.visible

class SettingsAdapter(val callBack: (View, Int, SettingsList) -> Unit) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    var list = ArrayList<SettingsList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemSettingsBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintMain.setOnClickListener {
                    callBack.invoke(it, adapterPosition, list[adapterPosition])
                }

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: SettingsList) = with(binding) {
            imageViewSettings.setImageResource(item.icon)
            textViewSettingsName.text = item.title
            textViewSettingsDescription.text = item.description
        }

    }

}