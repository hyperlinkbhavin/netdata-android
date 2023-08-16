package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.request.WarRoomsList
import cloud.netdata.android.databinding.RowItemSelectWarRoomsBinding
import cloud.netdata.android.utils.gone
import cloud.netdata.android.utils.invisible
import cloud.netdata.android.utils.visible

class SortByAdapter(val callBack: (View, Int, ArrayList<WarRoomsList>) -> Unit) : RecyclerView.Adapter<SortByAdapter.ViewHolder>() {

    var list = ArrayList<WarRoomsList>()
    var selectedItemPosition = RecyclerView.NO_POSITION

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

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: WarRoomsList) = with(binding) {
            viewTop.invisible()

            if(selectedItemPosition == absoluteAdapterPosition){
                imageViewCheck.visible()
//                imageViewCheck.visibility = if (item.isSelected) View.VISIBLE else View.GONE
            } else {
                imageViewCheck.gone()
            }
            textViewName.text = item.name

            constraintMain.setOnClickListener{
                if (selectedItemPosition != absoluteAdapterPosition) {
                    // Toggle tick visibility for the previous and current selected items
                    val previousSelectedItemPosition = selectedItemPosition
                    selectedItemPosition = absoluteAdapterPosition

                    if (previousSelectedItemPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(previousSelectedItemPosition)
                    }
                    notifyItemChanged(selectedItemPosition)
                } else {
                    // If clicked on the same item, hide the tick
                    selectedItemPosition = RecyclerView.NO_POSITION
                    notifyItemChanged(absoluteAdapterPosition)
                }

                for(i in list.indices){
                    list[i].isSelected = i == selectedItemPosition
                }
                callBack.invoke(it, selectedItemPosition, list)
                /*if (selectedItemPosition != RecyclerView.NO_POSITION) {

                } else {
                    item.isSelected = false
                    callBack.invoke(it, absoluteAdapterPosition, list)
                }*/

//                notifyDataSetChanged()
            }
        }

    }
}