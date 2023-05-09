package com.netdata.app.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netdata.app.data.pojo.request.ChooseSpaceList
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.RowItemChooseSpaceBinding
import com.netdata.app.databinding.RowItemSelectWarRoomsBinding
import com.netdata.app.databinding.RowItemTimeValuesBinding
import com.netdata.app.utils.gone
import com.netdata.app.utils.invisible
import com.netdata.app.utils.visible

class TimeValuesAdapter(val callBack: (View, Int, ChooseSpaceList) -> Unit) : RecyclerView.Adapter<TimeValuesAdapter.ViewHolder>() {

    var list = ArrayList<ChooseSpaceList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemTimeValuesBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemTimeValuesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintTop.setOnClickListener {
                    callBack.invoke(it, adapterPosition, list[adapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: ChooseSpaceList) = with(binding) {

        }

    }

}