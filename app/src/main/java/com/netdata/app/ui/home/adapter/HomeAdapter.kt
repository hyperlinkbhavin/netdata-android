package com.netdata.app.ui.home.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netdata.app.R
import com.netdata.app.data.pojo.HomeDataList
import com.netdata.app.databinding.RowItemHomeBinding
import com.netdata.app.utils.invisible
import com.netdata.app.utils.visible
import com.zerobranch.layout.SwipeLayout
import com.zerobranch.layout.SwipeLayout.SwipeActionsListener


class HomeAdapter(val callBack: (View, Int, HomeDataList) -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var list = ArrayList<HomeDataList>()
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
                        if(absoluteAdapterPosition != selectedPos){
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
        fun bind(item: HomeDataList) = with(binding) {
            if (absoluteAdapterPosition != selectedPos && (swipeLayout.isRightOpen || swipeLayout.isLeftOpen)){
                swipeLayout.close(true)
            }

            if(item.isRead){
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

            textViewName.text = item.name
            textViewDateTime.text = item.dateTime
            textViewGKE.text = item.gke
            textViewDiskSpace.text = item.diskSpace
            textViewWarRoomsList.text = item.warRooms
            textViewTypeAndComponent.text = item.typeComponent
        }

    }

}