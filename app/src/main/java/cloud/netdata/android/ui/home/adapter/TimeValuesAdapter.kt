package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.request.TimeValuesList
import cloud.netdata.android.databinding.RowItemTimeValuesBinding


class TimeValuesAdapter(val callBack: (View, Int, TimeValuesList) -> Unit) : RecyclerView.Adapter<TimeValuesAdapter.ViewHolder>() {

    var list = ArrayList<TimeValuesList>()

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

        @SuppressLint("SetTextI18n", "UseCompatTextViewDrawableApis")
        fun bind(item: TimeValuesList) = with(binding) {
            if(absoluteAdapterPosition == 1){
                textViewLabelWarning.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(textViewLabelWarning.context, R.color.colorLightYellowFF))
                textViewLabelWarning.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(textViewLabelWarning.context, R.color.colorBlack35)))
                textViewLabelWarning.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(textViewLabelWarning.context, R.color.colorBlack35))

                textViewWarningPercent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(textViewLabelWarning.context, R.color.colorLightYellowFF))
                textViewWarningPercent.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(textViewLabelWarning.context, R.color.colorBlack35)))
            }
        }

    }

}