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
import com.netdata.app.data.pojo.HomeDataList
import com.netdata.app.databinding.RowItemHomeBinding
import com.netdata.app.utils.invisible
import com.netdata.app.utils.visible

class HomeAdapter(val callBack: (View, Int) -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var list = ArrayList<HomeDataList>()

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

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: HomeDataList) = with(binding) {
            if(item.isRead){
                constraintTop.isSelected = true
//                Glide.with(imageViewMessage.context).load(R.drawable.ic_message_read).into(imageViewMessage)
                imageViewMessageRead.visible()
                imageViewMessageUnread.invisible()
            } else {
                constraintTop.isSelected = false
                imageViewMessageRead.invisible()
                imageViewMessageUnread.visible()
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