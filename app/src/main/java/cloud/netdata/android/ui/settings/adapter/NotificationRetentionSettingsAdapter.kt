package cloud.netdata.android.ui.settings.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.response.NotificationRetention
import cloud.netdata.android.databinding.RowItemNotificationRetentionSettingsBinding

class NotificationRetentionSettingsAdapter(val callBack: (View) -> Unit) : RecyclerView.Adapter<NotificationRetentionSettingsAdapter.ViewHolder>() {

    var list = ArrayList<NotificationRetention>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemNotificationRetentionSettingsBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemNotificationRetentionSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                radioButton.setOnClickListener {
                    updateSelection(absoluteAdapterPosition, it)
                }
                constraintMain.setOnClickListener {
                    updateSelection(absoluteAdapterPosition, it)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: NotificationRetention) = with(binding) {
            radioButton.isChecked = item.isSelected
            textViewTitle.text = item.name
        }

        private fun updateSelection(selectedPosition: Int, view: View) {
            for (i in list.indices) {
                list[i].isSelected = (i == selectedPosition)
            }
            notifyDataSetChanged()
            callBack.invoke(view)
        }

    }

}