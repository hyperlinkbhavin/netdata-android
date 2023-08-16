package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.request.ConfigurationList
import cloud.netdata.android.databinding.RowItemConfigurationBinding

class ConfigurationAdapter(val callBack: (View, Int, ConfigurationList) -> Unit) : RecyclerView.Adapter<ConfigurationAdapter.ViewHolder>() {

    var list = ArrayList<ConfigurationList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemConfigurationBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemConfigurationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintTop.setOnClickListener {
                    callBack.invoke(it, adapterPosition, list[adapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: ConfigurationList) = with(binding) {
            textViewTitle.text = item.title
            textViewDescription.text = item.description
        }

    }

}