package cloud.netdata.android.ui.settings.adapter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.response.SpaceList
import cloud.netdata.android.databinding.RowItemMaintenanceModeSettingsBinding
import cloud.netdata.android.utils.gone
import cloud.netdata.android.utils.visible
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import java.util.*
import kotlin.collections.ArrayList

class MaintenanceModeSettingsAdapter(var list: ArrayList<SpaceList>, val callBack: (View, Int, SpaceList, Boolean) -> Unit) : RecyclerView.Adapter<MaintenanceModeSettingsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemMaintenanceModeSettingsBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemMaintenanceModeSettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                switchDisableAllNotifications.setOnClickListener {
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition], switchDisableAllNotifications.isChecked)
                }
                textViewUntilDate.setOnClickListener {
                    datePicker()
                }
                radioButtonUntil.setOnClickListener {
                    datePicker()
                }

                radioButtonForever.setOnCheckedChangeListener { buttonView, isChecked ->
                    if(isChecked){
                        textViewUntilDate.text = "DD/MM/YY, HH:MM"
                        callBack.invoke(radioButtonForever, absoluteAdapterPosition, list[absoluteAdapterPosition], false)
                    }
                }

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: SpaceList) = with(binding) {
            if (item.plan.equals("EarlyBird", true) || item.plan.equals("Community", true)) {
                constraintDisableNotifications.setBackgroundResource(R.drawable.dw_corner_four_with_border_bg_inactive)
                constraintDisableNotifications.isSelected = false
                switchDisableAllNotifications.isClickable = false
            } else {
                constraintDisableNotifications.setBackgroundResource(R.drawable.dw_corner_four_with_border_bg)
                constraintDisableNotifications.isSelected = false
                switchDisableAllNotifications.isClickable = true
            }

                if(item.isSelected){
                constraintDisableNotifications.isSelected = true
                switchDisableAllNotifications.isChecked = true
                radioGroupAllNotifications.visible()
                if(item.isUntil){
                    radioButtonUntil.isChecked  =true
                    textViewUntilDate.text = item.untilDate
                } else {
                    radioButtonForever.isChecked  =true
                }
            } else {
                constraintDisableNotifications.isSelected = false
                switchDisableAllNotifications.isChecked = false
                radioGroupAllNotifications.gone()
            }
            textViewDisableAllNotification.text = item.name
        }
        fun datePicker()=with(binding){
            val mCalendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                textViewDisableAllNotification.context,
                { _, year, month, date ->
                    val dates = if(date < 10){
                        "0${date}"
                    } else {
                        (date).toString()
                    }

                    val months = if(month < 9){
                        "0${month+1}"
                    } else {
                        (month+1).toString()
                    }

                    timePicker(dates +"/"+months +"/"+year.toString().drop(2))
                },
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DATE)
            )
            datePickerDialog.datePicker.minDate = mCalendar.time.time
            datePickerDialog.show()
        }

        fun timePicker(date: String) =with(binding){
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog( textViewDisableAllNotification.context,
                { timePicker, selectedHour, selectedMinute ->
                    val fullDate = "$date, $selectedHour:$selectedMinute"
                    textViewUntilDate.setText(fullDate)
                    radioButtonUntil.isChecked = true
                    list[absoluteAdapterPosition].untilDate = fullDate
                    callBack.invoke(textViewUntilDate, absoluteAdapterPosition, list[absoluteAdapterPosition], false)
                },
                hour,
                minute,
                false) //Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }
    }

    fun updateList(filterList: ArrayList<SpaceList>) {
        list = filterList
        notifyDataSetChanged()
    }

}