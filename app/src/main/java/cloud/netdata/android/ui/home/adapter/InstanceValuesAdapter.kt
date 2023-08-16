package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.request.InstanceValuesList
import cloud.netdata.android.databinding.RowItemInstanceValuesBinding

class InstanceValuesAdapter(val callBack: (View, Int, InstanceValuesList) -> Unit) : RecyclerView.Adapter<InstanceValuesAdapter.ViewHolder>() {

    var list = ArrayList<InstanceValuesList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemInstanceValuesBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemInstanceValuesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintTop.setOnClickListener {
                    callBack.invoke(it, adapterPosition, list[adapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: InstanceValuesList) = with(binding) {
            textViewInstanceName.text = item.name
            textViewLabelWarning.text = item.priority
            textViewWarningPercent.text = item.priorityPercentage
            textViewInstanceDateTime.text = item.dateTime
        }

    }

}