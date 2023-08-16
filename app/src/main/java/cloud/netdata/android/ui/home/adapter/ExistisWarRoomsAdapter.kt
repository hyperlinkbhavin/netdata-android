package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.response.HomeNotificationList
import cloud.netdata.android.databinding.RowItemExistisWarRoomsListBinding

class ExistisWarRoomsAdapter(val callBack: (View, Int, HomeNotificationList.Data.Netdata.Room) -> Unit) : RecyclerView.Adapter<ExistisWarRoomsAdapter.ViewHolder>() {

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
            imageViewWarRoomsPriority.setImageResource(R.drawable.ic_high_priority)
        }

    }

}