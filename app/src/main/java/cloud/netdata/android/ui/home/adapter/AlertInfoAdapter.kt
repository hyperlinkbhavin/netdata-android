package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.request.AlertInfoList
import cloud.netdata.android.databinding.RowItemAlertInfoBinding

class AlertInfoAdapter(val callBack: (View, Int, AlertInfoList) -> Unit) : RecyclerView.Adapter<AlertInfoAdapter.ViewHolder>() {

    var list = ArrayList<AlertInfoList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemAlertInfoBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemAlertInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintTop.setOnClickListener {
                    callBack.invoke(it, adapterPosition, list[adapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: AlertInfoList) = with(binding) {
            imageViewAlertInfo.setImageResource(item.icon)
            textViewAlertInfoName.text = item.name
            textViewAlertInfoValue.text=  item.value
        }

    }

}