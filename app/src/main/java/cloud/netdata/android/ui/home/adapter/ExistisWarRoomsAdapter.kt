package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.enumclass.Priority
import cloud.netdata.android.data.pojo.response.HomeNotificationList
import cloud.netdata.android.databinding.RowItemExistisWarRoomsListBinding
import kotlinx.android.synthetic.main.bottom_sheet_notification_priority.*

class ExistisWarRoomsAdapter(val priority: String, val callBack: (View, Int, HomeNotificationList.Data.Netdata.Room) -> Unit) : RecyclerView.Adapter<ExistisWarRoomsAdapter.ViewHolder>() {

    var list = ArrayList<HomeNotificationList.Data.Netdata.Room>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemExistisWarRoomsListBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemExistisWarRoomsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: HomeNotificationList.Data.Netdata.Room) = with(binding) {
            textViewWarRoomsName.text = item.name
            if (priority.equals(Priority.HIGH_PRIORITY.shortName, true)) {
                imageViewWarRoomsPriority.setImageResource(R.drawable.ic_high_priority)
            } else if (priority.equals(Priority.MEDIUM_PRIORITY.shortName, true)) {
                imageViewWarRoomsPriority.setImageResource(R.drawable.ic_medium_priority)
            } else {
                imageViewWarRoomsPriority.setImageResource(R.drawable.ic_low_priority)
            }

        }

    }

}