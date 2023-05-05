package com.netdata.app.ui.notification.adapter

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
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.RowItemNotificationBinding
import com.netdata.app.databinding.RowItemSelectWarRoomsBinding
import com.netdata.app.utils.gone
import com.netdata.app.utils.invisible
import com.netdata.app.utils.visible

class NotificationAdapter(val callBack: (View, Int, NotificationsList) -> Unit) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    var list = ArrayList<NotificationsList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemNotificationBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: NotificationsList) = with(binding) {
            textViewSpaceName.text = item.spaceName
            textViewSpaceWarningPercent.text = item.priorityPercent
            textViewNotificationDateTime.text = item.dateTime

            when (item.priority) {
                Priority.HIGH_PRIORITY -> {
                    imageViewNotificationPriority.setImageResource(R.drawable.ic_high_priority)
//                    imageViewSpaceWarning.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(imageViewSpaceWarning.context, R.color.colorRedF9))
                }
                Priority.MEDIUM_PRIORITY -> {
                    imageViewNotificationPriority.setImageResource(R.drawable.ic_medium_priority)
//                    imageViewSpaceWarning.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(imageViewSpaceWarning.context, R.color.colorYellowFF))
                }
                else -> {
                    imageViewNotificationPriority.setImageResource(R.drawable.ic_low_priority)
//                    imageViewSpaceWarning.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(imageViewSpaceWarning.context, R.color.colorGreen68))
                }
            }
        }

    }

}