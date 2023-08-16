package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.request.SeverityConditionsList
import cloud.netdata.android.databinding.RowItemSeverityConditionsBinding

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