package com.netdata.app.ui.notification.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.netdata.app.R
import com.netdata.app.data.pojo.enumclass.Priority
import com.netdata.app.data.pojo.request.NotificationsList
import com.netdata.app.data.pojo.response.HomeNotificationList
import com.netdata.app.databinding.RowItemNotificationBinding
import com.netdata.app.utils.AppUtils
import com.netdata.app.utils.ConvertDateTimeFormat
import com.netdata.app.utils.DateTimeFormats
import com.zerobranch.layout.SwipeLayout

class NotificationAdapter(val callBack: (View, Int, HomeNotificationList) -> Unit) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    var list = ArrayList<HomeNotificationList>()
    private val binderHelper: ViewBinderHelper = ViewBinderHelper()

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
        with(holder){
            binderHelper.setOpenOnlyOne(true)
            binderHelper.bind(binding.swipeRevealLayout, position.toString())
            binding.swipeRevealLayout.close(true)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(val binding: RowItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintTop.setOnClickListener {
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                }

                constraintNotificationRight.setOnClickListener {
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: HomeNotificationList) = with(binding) {

            textViewSpaceName.text = item.data!!.netdata!!.space!!.name
            textViewSpaceWarningPercent.text = AppUtils.convertTwoDecimal(item.data!!.netdata!!.alert!!.current!!.value!!, isPercent = true)
            textViewSpaceWarningText.text = item.data!!.netdata!!.alert!!.name[0]
            textViewNotificationDateTime.text = "${
                ConvertDateTimeFormat.getPrettyTime(
                    ConvertDateTimeFormat.convertUTCToLocalDate(
                        item.createdAt!!,
                        DateTimeFormats.SERVER_DATE_TIME_FORMAT_NEW,
                        "yyyy-MM-dd HH:mm:ss"
                    ) ,
                    textViewNotificationDateTime.context
                )
//                AppUtils.getTimeAgo(item.createdAt!!)
            } · ${
                ConvertDateTimeFormat.convertUTCToLocalDate(
                    item.createdAt!!,
                    DateTimeFormats.SERVER_DATE_TIME_FORMAT_NEW,
                    "dd/MM/yyyy-HH:mm:ss"
                )
            }"

            when (item.priority) {
                Priority.HIGH_PRIORITY.shortName -> {
                    imageViewNotificationPriority.setImageResource(R.drawable.ic_high_priority)
//                    imageViewSpaceWarning.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(imageViewSpaceWarning.context, R.color.colorRedF9))
                }
                Priority.MEDIUM_PRIORITY.shortName -> {
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