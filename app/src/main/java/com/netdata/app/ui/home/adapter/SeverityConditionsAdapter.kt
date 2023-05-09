package com.netdata.app.ui.home.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.netdata.app.R
import com.netdata.app.data.pojo.request.ChooseSpaceList
import com.netdata.app.data.pojo.request.SeverityConditionsList
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.RowItemChooseSpaceBinding
import com.netdata.app.databinding.RowItemSelectWarRoomsBinding
import com.netdata.app.databinding.RowItemSeverityConditionsBinding
import com.netdata.app.databinding.RowItemTimeValuesBinding
import com.netdata.app.utils.gone
import com.netdata.app.utils.invisible
import com.netdata.app.utils.visible

class SeverityConditionsAdapter(val callBack: (View, Int, SeverityConditionsList) -> Unit) : RecyclerView.Adapter<SeverityConditionsAdapter.ViewHolder>() {

    var list = ArrayList<SeverityConditionsList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemSeverityConditionsBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemSeverityConditionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintTop.setOnClickListener {
                    callBack.invoke(it, adapterPosition, list[adapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: SeverityConditionsList) = with(binding) {
            imageViewDot.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(imageViewDot.context, item.dotColor))
            textViewTitle.text = item.title
            textViewDescription.text = item.description
            textViewLabelMoreWarningDetails.text = item.details
        }

    }

}