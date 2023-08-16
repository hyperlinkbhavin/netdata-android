package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.data.pojo.response.SpaceList
import cloud.netdata.android.databinding.RowItemChooseSpaceBinding
import cloud.netdata.android.utils.gone
import cloud.netdata.android.utils.visible

class ChooseSpaceAdapter(val callBack: (View, Int, SpaceList) -> Unit) : RecyclerView.Adapter<ChooseSpaceAdapter.ViewHolder>() {

    var list = ArrayList<SpaceList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemChooseSpaceBinding.inflate(
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
    inner class ViewHolder(val binding: RowItemChooseSpaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                constraintTop.setOnClickListener {
                    callBack.invoke(it, adapterPosition, list[adapterPosition])
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: SpaceList) = with(binding) {
            if(item.count != 0){
                textViewSpaceCount.visible()
                textViewSpaceCount.text = item.count.toString()
            } else {
                textViewSpaceCount.gone()
            }
            textViewSpaceName.text = item.name
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(filterList: ArrayList<SpaceList>) {
        list = filterList
        notifyDataSetChanged()
    }

}