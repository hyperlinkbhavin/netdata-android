package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.response.RoomList
import cloud.netdata.android.databinding.RowItemSelectWarRoomsBinding
import cloud.netdata.android.utils.gone
import cloud.netdata.android.utils.visible

class AllWarRoomsAdapter(val callBack: (View, Int, RoomList) -> Unit) : RecyclerView.Adapter<AllWarRoomsAdapter.ViewHolder>() {

    var list = ArrayList<RoomList>()
    var selectionPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemSelectWarRoomsBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemSelectWarRoomsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintMain.setOnClickListener {
                    selectionPosition = absoluteAdapterPosition
                    callBack.invoke(it, absoluteAdapterPosition, list[absoluteAdapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: RoomList) = with(binding) {
            if(selectionPosition == absoluteAdapterPosition){
                item.isSelected = true
                imageViewCheck.visible()
            } else {
                item.isSelected = false
                imageViewCheck.gone()
            }
            textViewName.text = item.name
        }

    }

}