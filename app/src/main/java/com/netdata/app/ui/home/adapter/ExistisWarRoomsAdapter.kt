package com.netdata.app.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netdata.app.data.pojo.request.ExistisWarRoomsList
import com.netdata.app.databinding.RowItemExistisWarRoomsListBinding

class ExistisWarRoomsAdapter(val callBack: (View, Int, ExistisWarRoomsList) -> Unit) : RecyclerView.Adapter<ExistisWarRoomsAdapter.ViewHolder>() {

    var list = ArrayList<ExistisWarRoomsList>()
    var selectionPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemExistisWarRoomsListBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemExistisWarRoomsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: ExistisWarRoomsList) = with(binding) {
            textViewWarRoomsName.text = item.name
            imageViewWarRoomsPriority.setImageResource(item.status)
        }

    }

}