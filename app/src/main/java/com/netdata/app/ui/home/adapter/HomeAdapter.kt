package com.netdata.app.ui.home.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.netdata.app.R
import com.netdata.app.data.pojo.enumclass.Priority
import com.netdata.app.data.pojo.response.HomeNotificationList
import com.netdata.app.databinding.RowItemHomeBinding
import com.netdata.app.utils.*
import com.zerobranch.layout.SwipeLayout.SwipeActionsListener
import java.util.*
import kotlin.collections.ArrayList


class HomeAdapter(val callBack: (View, Int, HomeNotificationList) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var list = ArrayList<HomeNotificationList>()
    var selectedPos = -1
    var previousPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemHomeBinding.inflate(
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


    inner class ViewHolder(val binding: RowItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintTop.setOnClickListener {
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                }
                imageViewPriority.setOnClickListener {
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                }

                swipeLayout.setOnActionsListener(object : SwipeActionsListener {
                    override fun onOpen(direction: Int, isContinuous: Boolean) {
//                        notifyItemChanged(selectedPos)
                        if (absoluteAdapterPosition != selectedPos) {
                            previousPos = selectedPos
                            selectedPos = absoluteAdapterPosition
                            notifyItemChanged(previousPos)
                        }
                        /*Log.e("selected", selectedPos.toString())
                        Log.e("Preselected", previousPos.toString())*/
                    }

                    override fun onClose() {
                        //called when closing
                    }
                })

                leftViewSwipe.setOnClickListener {
                    list[absoluteAdapterPosition].isRead = !list[absoluteAdapterPosition].isRead
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                    /*list[absoluteAdapterPosition].isRead = !list[absoluteAdapterPosition].isRead
                    notifyItemChanged(absoluteAdapterPosition)*/
                }

                rightViewSwipe.setOnClickListener {
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                }

                textViewWarRoomsListCount.setOnClickListener {
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: HomeNotificationList) = with(binding) {
            if (absoluteAdapterPosition != selectedPos && (swipeLayout.isRightOpen || swipeLayout.isLeftOpen)) {
                swipeLayout.close(true)
            }

            if (item.isRead) {
                constraintTop.isSelected = true
//                Glide.with(imageViewMessage.context).load(R.drawable.ic_message_read).into(imageViewMessage)
                imageViewMessageRead.visible()
                imageViewMessageUnread.invisible()
                leftViewSwipe.setImageResource(R.drawable.ic_swipe_mark_as_unread)
            } else {
                constraintTop.isSelected = false
                imageViewMessageRead.invisible()
                imageViewMessageUnread.visible()
                leftViewSwipe.setImageResource(R.drawable.ic_swipe_mark_as_read)
//                imageViewMessage.setImageResource(R.drawable.ic_message_unread)
//                Glide.with(imageViewMessage.context).load(R.drawable.ic_message_unread).into(imageViewMessage)
//                imageViewMessage.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(imageViewMessage.context, R.color.colorPrimary))
            }

            textViewName.text = item.data!!.netdata!!.alert!!.name[0]
            textViewDateTime.text = "${
                ConvertDateTimeFormat.getPrettyTime(
                    ConvertDateTimeFormat.convertUTCToLocalDate(
                        item.createdAt!!,
                        DateTimeFormats.SERVER_DATE_TIME_FORMAT_NEW,
                        "yyyy-MM-dd HH:mm:ss"
                    ) ,
                    textViewDateTime.context
                )
//                AppUtils.getTimeAgo(item.createdAt!!)
            } · ${
                ConvertDateTimeFormat.convertUTCToLocalDate(
                    item.createdAt!!,
                    DateTimeFormats.SERVER_DATE_TIME_FORMAT_NEW,
                    "dd/MM/yyyy-HH:mm:ss"
                )
            }"
            textViewGKE.text = item.data!!.host[0].name
            textViewDiskSpace.text = item.data!!.netdata!!.chart!!.id

            var roomList = ""
            for(i in item.data!!.netdata!!.room){
                roomList += "${i.name} • "
            }
            textViewWarRoomsList.text = roomList.dropLast(3)
            textViewTypeAndComponent.text = "Type & Component : ${item.data!!.netdata!!.alert!!.type} • ${item.data!!.netdata!!.alert!!.component}"
            textViewLabelWarning.text = item.data!!.netdata!!.alert!!.current!!.status[0].replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            textViewWarningPercent.text = AppUtils.convertTwoDecimal(item.data!!.netdata!!.alert!!.current!!.value!!, true)
            textViewCriticalPercent.text = AppUtils.convertTwoDecimal(item.data!!.netdata!!.alert!!.current!!.value!!, true)

            if (item.data!!.netdata!!.alert!!.current!!.status[0].equals("critical", true)) {
                textViewLabelWarning.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        textViewLabelWarning.context,
                        R.color.colorRedFF
                    )
                )
                textViewWarningPercent.gone()
                textViewCriticalPercent.visible()
            } else if (item.data!!.netdata!!.alert!!.current!!.status[0].equals("warning", true)) {
                textViewWarningPercent.visible()
                textViewCriticalPercent.gone()
                textViewLabelWarning.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        textViewLabelWarning.context,
                        R.color.colorYellowF9
                    )
                )
                textViewWarningPercent.setTextColor(
                    ContextCompat.getColor(
                        textViewWarningPercent.context,
                        R.color.colorYellowF9
                    )
                )
                textViewWarningPercent.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        textViewWarningPercent.context,
                        R.color.colorLightYellowFF
                    )
                )
            } else {
                textViewWarningPercent.visible()
                textViewCriticalPercent.gone()
                textViewLabelWarning.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        textViewLabelWarning.context,
                        R.color.colorPrimary
                    )
                )
                textViewWarningPercent.setTextColor(
                    ContextCompat.getColor(
                        textViewWarningPercent.context,
                        R.color.colorPrimary
                    )
                )
                textViewWarningPercent.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        textViewWarningPercent.context,
                        R.color.colorGreen80
                    )
                )
            }

            if (item.priority.equals(Priority.HIGH_PRIORITY.shortName, true)) {
                Glide.with(imageViewPriority.context).load(R.drawable.ic_high_priority)
                    .into(imageViewPriority)
            } else if (item.priority.equals(Priority.MEDIUM_PRIORITY.shortName, true)) {
                Glide.with(imageViewPriority.context).load(R.drawable.ic_medium_priority)
                    .into(imageViewPriority)
            } else {
                Glide.with(imageViewPriority.context).load(R.drawable.ic_low_priority)
                    .into(imageViewPriority)
            }

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(filterList: ArrayList<HomeNotificationList>) {
        list = filterList
        notifyDataSetChanged()
    }

}