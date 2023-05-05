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
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.HomeFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.auth.IsolatedFullActivity
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.adapter.AllWarRoomsAdapter
import com.netdata.app.ui.home.adapter.HomeAdapter
import com.netdata.app.ui.home.adapter.SortByAdapter
import com.netdata.app.ui.notification.fragment.NotificationFragment
import com.netdata.app.ui.settings.fragment.SettingsFragment
import com.netdata.app.utils.Constant
import com.netdata.app.utils.visible

class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    private var warRoomsItemPosition = -1
    private var sortByTimeItemPosition = -1
    private var sortByNotificationPriorityItemPosition = -1
    private var sortByCriticalityItemPosition = -1

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
            imageViewSetting.visible()
            imageViewFilter.visible()
            imageViewNotification.visible()

            if(appPreferences.getString(Constant.APP_PREF_SPACE_NAME).isNotEmpty()){
                textViewSpace.text = appPreferences.getString(Constant.APP_PREF_SPACE_NAME)
            }
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
            navigator.loadActivity(IsolatedFullActivity::class.java, SettingsFragment::class.java).start()
        }

        includeToolbar.imageViewFilter.setOnClickListener {

        }

        includeToolbar.imageViewNotification.setOnClickListener {
            navigator.loadActivity(IsolatedFullActivity::class.java, NotificationFragment::class.java).start()
        }

        includeToolbar.textViewSpace.setOnClickListener {
            navigator.loadActivity(IsolatedFullActivity::class.java, ChooseSpaceFragment::class.java).start()
        }

        constraintAllWarRooms.setOnClickListener {
            bottomSheetAllWarRooms()
        }

        constraintSortBy.setOnClickListener {
            bottomSheetSortBy()
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
            AllWarRoomsAdapter() { view, position, item ->
                when(view.id){
                    R.id.constraintMain -> {
                        warRoomsItemPosition = position
                        binding.textViewLabelAllWarRooms.text = item.name
                        dialog.dismiss()
                    }
                }
            }
        }

        allWarRoomsAdapter.list.add(WarRoomsList("All War Rooms"))
        allWarRoomsAdapter.list.add(WarRoomsList("War Rooms 1"))
        allWarRoomsAdapter.list.add(WarRoomsList("War Rooms 2"))
        allWarRoomsAdapter.list.add(WarRoomsList("War Rooms 3"))
        allWarRoomsAdapter.list.add(WarRoomsList("War Rooms 4"))
        allWarRoomsAdapter.list.add(WarRoomsList("War Rooms 5"))

        val recyclerViewSelectWarRooms = view.findViewById<RecyclerView>(R.id.recyclerViewSelectWarRooms)
        val textViewLabelClose = view.findViewById<AppCompatTextView>(R.id.textViewLabelClose)
        recyclerViewSelectWarRooms.adapter = allWarRoomsAdapter

        if(warRoomsItemPosition != -1){
            allWarRoomsAdapter.selectionPosition = warRoomsItemPosition
        }

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

    @SuppressLint("NotifyDataSetChanged")
    private fun bottomSheetSortBy() {

        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.bottom_sheet_sort_by, null)

        val sortByTimeAdapter by lazy {
            SortByAdapter() { view, position, item ->
                when(view.id){
                    R.id.constraintMain -> {
                        sortByTimeItemPosition = position
                    }
                }
            }
        }

        val sortByNotificationPriorityAdapter by lazy {
            SortByAdapter() { view, position, item ->
                when(view.id){
                    R.id.constraintMain -> {
                        sortByNotificationPriorityItemPosition = position
                    }
                }
            }
        }

        val sortByCriticalityAdapter by lazy {
            SortByAdapter() { view, position, item ->
                when(view.id){
                    R.id.constraintMain -> {
                        sortByCriticalityItemPosition = position
                    }
                }
            }
        }

        val recyclerViewTime = view.findViewById<RecyclerView>(R.id.recyclerViewTime)
        val recyclerViewNotificationPriority = view.findViewById<RecyclerView>(R.id.recyclerViewNotificationPriority)
        val recyclerViewCriticality = view.findViewById<RecyclerView>(R.id.recyclerViewCriticality)
        val textViewLabelClose = view.findViewById<AppCompatTextView>(R.id.textViewLabelClose)
        recyclerViewTime.adapter = sortByTimeAdapter
        recyclerViewNotificationPriority.adapter = sortByNotificationPriorityAdapter
        recyclerViewCriticality.adapter = sortByCriticalityAdapter

        sortByTimeAdapter.list.add(WarRoomsList("New on top"))
        sortByTimeAdapter.list.add(WarRoomsList("Old on top"))

        sortByNotificationPriorityAdapter.list.add(WarRoomsList("High to Low"))
        sortByNotificationPriorityAdapter.list.add(WarRoomsList("Low to High"))

        sortByCriticalityAdapter.list.add(WarRoomsList("Critical to Clear"))
        sortByCriticalityAdapter.list.add(WarRoomsList("Clear to Critical"))

        if(sortByTimeItemPosition != -1){
            sortByTimeAdapter.selectionPosition = sortByTimeItemPosition
        }

        if(sortByNotificationPriorityItemPosition != -1){
            sortByNotificationPriorityAdapter.selectionPosition = sortByNotificationPriorityItemPosition
        }

        if(sortByCriticalityItemPosition != -1){
            sortByCriticalityAdapter.selectionPosition = sortByCriticalityItemPosition
        }

        sortByTimeAdapter.notifyDataSetChanged()
        sortByNotificationPriorityAdapter.notifyDataSetChanged()
        sortByCriticalityAdapter.notifyDataSetChanged()

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