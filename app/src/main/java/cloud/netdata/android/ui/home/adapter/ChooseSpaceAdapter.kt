package cloud.netdata.android.ui.home.adapter

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.response.SpaceList
import cloud.netdata.android.databinding.RowItemChooseSpaceBinding
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.Constant.isDarkMode
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
            if (item.plan.equals("EarlyBird", true) || item.plan.equals("Community", true)) {
                if (isDarkMode) {
                    constraintTop.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            constraintTop.context,
                            R.color.colorBlack3F
                        )
                    )
                } else {
                    constraintTop.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            constraintTop.context,
                            R.color.colorWhiteD6
                        )
                    )
                }
            } else {
                if (isDarkMode) {
                    constraintTop.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            constraintTop.context,
                            R.color.colorBlack1C
                        )
                    )
                } else {
                    constraintTop.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            constraintTop.context,
                            R.color.colorWhite
                        )
                    )
                }
            }
            if (item.count != 0) {
                textViewSpaceCount.visible()
                textViewSpaceCount.text = item.count.toString()
            } else {
                textViewSpaceCount.gone()
            }
            if(item.silenceRuleIdList.isNotEmpty()){
                imageViewSilencingRules.visible()
            } else {
                imageViewSilencingRules.gone()
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