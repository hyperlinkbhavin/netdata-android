package com.netdata.app.ui.home.fragment

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.netdata.app.R
import com.netdata.app.data.pojo.HomeDataList
import com.netdata.app.databinding.HomeFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.adapter.AllWarRoomsAdapter
import com.netdata.app.ui.home.adapter.HomeAdapter
import com.netdata.app.ui.settings.fragment.SettingsFragment
import com.netdata.app.utils.visible

class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    private val homeAdapter by lazy {
        HomeAdapter(){ view, position ->

        }
    }

    private var isAllButtonSelected = true

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater, container, attachToRoot)
    }

    override fun bindData() {
        toolbar()
        manageClick()
        setAdapter()
        addData()

        binding.buttonAll.isSelected = isAllButtonSelected
        binding.buttonUnread.isSelected = !isAllButtonSelected
    }

    private fun toolbar() = with(binding) {
        includeToolbar.apply {
            textViewSpace.visible()
            textViewSpace.text = "Space 1"
            imageViewSetting.visible()
            imageViewFilter.visible()
            imageViewNotification.visible()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun manageClick() = with(binding) {
        buttonAll.setOnClickListener {
            isAllButtonSelected = true
            binding.buttonAll.isSelected = isAllButtonSelected
            binding.buttonUnread.isSelected = !isAllButtonSelected
        }

        buttonUnread.setOnClickListener {
            isAllButtonSelected = false
            binding.buttonAll.isSelected = isAllButtonSelected
            binding.buttonUnread.isSelected = !isAllButtonSelected
        }

        textViewLabelMarkAllAsRead.setOnClickListener {
            homeAdapter.list[0].isRead = true
            homeAdapter.list[1].isRead = true

            homeAdapter.notifyDataSetChanged()
        }

        includeToolbar.imageViewSetting.setOnClickListener {
            navigator.load(SettingsFragment::class.java).replace(true)
        }

        constraintAllWarRooms.setOnClickListener {
            bottomSheetAllWarRooms()
        }
    }

    private fun setAdapter() = with(binding){
        recyclerViewHome.adapter = homeAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addData() {
        homeAdapter.list.add(HomeDataList(
                "inbound",
                "24 second ago",
                "gke-gke",
                "disk-space",
                "warRooms1 warRooms2",
                "Type and Component",
                true
            ))

        homeAdapter.list.add(HomeDataList(
            "inbound",
            "24 second ago",
            "gke-gke",
            "disk-space",
            "warRooms1 warRooms2",
            "Type and Component",
        ))


        homeAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bottomSheetAllWarRooms() {

        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.bottom_sheet_select_war_rooms, null)

        val allWarRoomsAdapter by lazy {
            AllWarRoomsAdapter() { view, position ->
                when(view.id){

                }
            }
        }

        allWarRoomsAdapter.list.add(Pair("All War Rooms", false))
        allWarRoomsAdapter.list.add(Pair("War Rooms 1", false))
        allWarRoomsAdapter.list.add(Pair("War Rooms 2", false))
        allWarRoomsAdapter.list.add(Pair("War Rooms 3", false))
        allWarRoomsAdapter.list.add(Pair("War Rooms 4", false))
        allWarRoomsAdapter.list.add(Pair("War Rooms 5", false))

        val recyclerViewSelectWarRooms = view.findViewById<RecyclerView>(R.id.recyclerViewSelectWarRooms)
        val textViewLabelClose = view.findViewById<AppCompatTextView>(R.id.textViewLabelClose)
        recyclerViewSelectWarRooms.adapter = allWarRoomsAdapter

        allWarRoomsAdapter.notifyDataSetChanged()

        textViewLabelClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(true)

        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                dialog.dismiss()
            }
            true
        }

        dialog.show()
    }
}