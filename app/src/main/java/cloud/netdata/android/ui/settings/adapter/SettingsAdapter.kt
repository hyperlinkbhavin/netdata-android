package cloud.netdata.android.ui.settings.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.request.SettingsList
import cloud.netdata.android.databinding.RowItemSettingsBinding

class SettingsAdapter(val callBack: (View, Int, SettingsList) -> Unit) : RecyclerView.Adapter<SettingsAdapter.ViewHolder>() {

    var list = ArrayList<SettingsList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemSettingsBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintMain.setOnClickListener {
                    callBack.invoke(it, adapterPosition, list[adapterPosition])
                }

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: SettingsList) = with(binding) {
            imageViewSettings.setImageResource(item.icon)
            textViewSettingsName.text = item.title
            textViewSettingsDescription.text = item.description
        }

    }

}