package com.netdata.app.ui.home.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.netdata.app.R
import com.netdata.app.data.pojo.HomeDataList
import com.netdata.app.data.pojo.enumclass.AlertStatus
import com.netdata.app.data.pojo.request.FilterList
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.HomeFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.auth.IsolatedFullActivity
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.adapter.AllWarRoomsAdapter
import com.netdata.app.ui.home.adapter.HomeAdapter
import com.netdata.app.ui.home.adapter.HomeFilterAdapter
import com.netdata.app.ui.home.adapter.SortByAdapter
import com.netdata.app.ui.notification.fragment.NotificationFragment
import com.netdata.app.ui.settings.fragment.SettingsFragment
import com.netdata.app.utils.Constant
import com.netdata.app.utils.gone
import com.netdata.app.utils.invisible
import com.netdata.app.utils.visible


class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    private var warRoomsItemPosition = 0
    private var sortByTimeItemPosition = -1
    private var sortByNotificationPriorityItemPosition = -1
    private var sortByCriticalityItemPosition = -1

    private var totalFilterCount = 0

    private var homeList = ArrayList<HomeDataList>()

    private val homeAdapter by lazy {
        HomeAdapter(){ view, position, item ->
            when(view.id){
                R.id.constraintTop -> {
                    navigator.loadActivity(IsolatedFullActivity::class.java, HomeDetailsFragment::class.java).start()
                }

                R.id.imageViewPriority -> {
                    bottomSheetPriority()
                }

                R.id.leftViewSwipe -> {
                    leftSwipeManage(position)
                }

                R.id.rightViewSwipe -> {
                    showMessage("Change Priority Clicked")
                }
            }
        }
    }

    private val nodeFilterAdapter by lazy {
        HomeFilterAdapter(){ view, position, item ->
            when(view.id){
                R.id.checkBoxFilter -> {
                    filterCount()
                }
            }
        }
    }

    private val alertStatusFilterAdapter by lazy {
        HomeFilterAdapter(){ view, position, item ->
            when(view.id){
                R.id.checkBoxFilter -> {
                    filterCount()
                }
            }
        }
    }

    private val notificationPriorityFilterAdapter by lazy {
        HomeFilterAdapter(){ view, position, item ->
            when(view.id){
                R.id.checkBoxFilter -> {
                    filterCount()
                }
            }
        }
    }

    private val classFilterAdapter by lazy {
        HomeFilterAdapter(){ view, position, item ->
            when(view.id){
                R.id.checkBoxFilter -> {
                    filterCount()
                }
            }
        }
    }

    private val typeAndComponentFilterAdapter by lazy {
        HomeFilterAdapter(){ view, position, item ->
            when(view.id){
                R.id.checkBoxFilter -> {
                    filterCount()
                }
            }
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

        drawerFilter()
    }

    override fun onResume() {
        super.onResume()
        if(appPreferences.getBoolean(Constant.APP_PREF_FROM_NOTIFICATION)){
            showMessage("You are viewing ${appPreferences.getString(Constant.APP_PREF_SPACE_NAME)}")
            appPreferences.putBoolean(Constant.APP_PREF_FROM_NOTIFICATION, false)
        }

        if(appPreferences.getString(Constant.APP_PREF_SPACE_NAME).isNotEmpty()){
            binding.includeToolbar.textViewSpace.text = appPreferences.getString(Constant.APP_PREF_SPACE_NAME)
        }
    }

    private fun toolbar() = with(binding) {
        includeToolbar.apply {
            textViewSpace.visible()
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

            homeAdapter.list.clear()
            homeAdapter.list.addAll(homeList)
            homeAdapter.notifyDataSetChanged()
        }

        buttonUnread.setOnClickListener {
            isAllButtonSelected = false
            binding.buttonAll.isSelected = isAllButtonSelected
            binding.buttonUnread.isSelected = !isAllButtonSelected

            val data = homeList.filter { !it.isRead }
            homeAdapter.list.clear()
            homeAdapter.list.addAll(data)

            homeAdapter.notifyDataSetChanged()
        }

        textViewLabelMarkAllAsRead.setOnClickListener {
            for(item in homeList){
                item.isRead = true
            }

            homeAdapter.list.clear()
            homeAdapter.list.addAll(homeList)

            homeAdapter.notifyDataSetChanged()
        }

        includeToolbar.imageViewSetting.setOnClickListener {
            navigator.loadActivity(IsolatedFullActivity::class.java, SettingsFragment::class.java).start()
        }

        includeToolbar.imageViewFilter.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
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

        textViewLabelClose.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        buttonApplyFilter.setOnClickListener {
            binding.apply {
                if(totalFilterCount != 0){
                    includeToolbar.textViewFilterCount.visible()
                    includeToolbar.textViewFilterCount.text = totalFilterCount.toString()
                } else {
                    includeToolbar.textViewFilterCount.gone()
                }
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
    }

    private fun setAdapter() = with(binding){
        recyclerViewHome.adapter = homeAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addData() {
        homeList.add(HomeDataList(
                "inbound",
                "24 second ago",
                "gke-gke",
                "disk-space",
                "warRooms1 warRooms2",
                "Type and Component",
                true
            ))

        homeList.add(HomeDataList(
            "inbound",
            "24 second ago",
            "gke-gke",
            "disk-space",
            "warRooms1 warRooms2",
            "Type and Component",
        ))

        homeList.add(HomeDataList(
            "inbound",
            "24 second ago",
            "gke-gke",
            "disk-space",
            "warRooms1 warRooms2",
            "Type and Component",
            true
        ))

        homeList.add(HomeDataList(
            "inbound",
            "24 second ago",
            "gke-gke",
            "disk-space",
            "warRooms1 warRooms2",
            "Type and Component",
        ))

        homeList.add(HomeDataList(
            "inbound",
            "24 second ago",
            "gke-gke",
            "disk-space",
            "warRooms1 warRooms2",
            "Type and Component",
            true
        ))

        homeList.add(HomeDataList(
            "inbound",
            "24 second ago",
            "gke-gke",
            "disk-space",
            "warRooms1 warRooms2",
            "Type and Component",
        ))

        homeAdapter.list.addAll(homeList)

        homeAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun leftSwipeManage(position: Int){
        if(isAllButtonSelected){
            homeAdapter.list[position].isRead = !homeAdapter.list[position].isRead
            homeAdapter.notifyDataSetChanged()
        } else {
            homeAdapter.list.removeAt(position)
            homeAdapter.notifyDataSetChanged()
        }
    }

    private fun bottomSheetPriority() {

        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.bottom_sheet_notification_priority, null)

        val textViewLabelEditPrioritySettings = view.findViewById<AppCompatTextView>(R.id.textViewLabelEditPrioritySettings)
        val buttonChangeNotificationPriority = view.findViewById<AppCompatButton>(R.id.buttonChangeNotificationPriority)

        val textViewLabelLowPriority = view.findViewById<AppCompatTextView>(R.id.textViewLabelLowPriority)
        val textViewLabelMediumPriority = view.findViewById<AppCompatTextView>(R.id.textViewLabelMediumPriority)
        val textViewLabelHighPriority = view.findViewById<AppCompatTextView>(R.id.textViewLabelHighPriority)

        val radioButtonLabelLowPriority = view.findViewById<AppCompatRadioButton>(R.id.radioButtonLabelLowPriority)
        val radioButtonLabelMediumPriority = view.findViewById<AppCompatRadioButton>(R.id.radioButtonLabelMediumPriority)
        val radioButtonLabelHighPriority = view.findViewById<AppCompatRadioButton>(R.id.radioButtonLabelHighPriority)

        val constraintNodes = view.findViewById<ConstraintLayout>(R.id.constraintNodes)
        val constraintCurrentNodes = view.findViewById<ConstraintLayout>(R.id.constraintCurrentNodes)
        val constraintAllNodes = view.findViewById<ConstraintLayout>(R.id.constraintAllNodes)

        /*val constraintLowPriority = view.findViewById<ConstraintLayout>(R.id.constraintLowPriority)
        val constraintMediumPriority = view.findViewById<ConstraintLayout>(R.id.constraintMediumPriority)
        val constraintHighPriority = view.findViewById<ConstraintLayout>(R.id.constraintHighPriority)*/

        val radioButtonCurrentNodes = view.findViewById<AppCompatRadioButton>(R.id.radioButtonCurrentNodes)
        val radioButtonAllNodes = view.findViewById<AppCompatRadioButton>(R.id.radioButtonAllNodes)

        val imageViewBigPriority = view.findViewById<AppCompatImageView>(R.id.imageViewBigPriority)
        val textViewPriorityName = view.findViewById<AppCompatTextView>(R.id.textViewPriorityName)

        radioButtonLabelHighPriority.isChecked = true

        buttonChangeNotificationPriority.setOnClickListener {
            buttonChangeNotificationPriority.gone()
            constraintNodes.visible()
            radioButtonLabelLowPriority.visible()
            radioButtonLabelMediumPriority.visible()
            radioButtonLabelHighPriority.visible()

            textViewLabelLowPriority.invisible()
            textViewLabelMediumPriority.invisible()
            textViewLabelHighPriority.invisible()
        }

        textViewLabelEditPrioritySettings.setOnClickListener {
            navigator.loadActivity(IsolatedFullActivity::class.java, SettingsFragment::class.java).start()
            dialog.dismiss()
        }

        constraintCurrentNodes.setOnClickListener {
            radioButtonCurrentNodes.isChecked = true
            radioButtonAllNodes.isChecked = false
        }

        constraintAllNodes.setOnClickListener {
            radioButtonCurrentNodes.isChecked = false
            radioButtonAllNodes.isChecked = true
        }

        radioButtonCurrentNodes.setOnClickListener {
            radioButtonCurrentNodes.isChecked = true
            radioButtonAllNodes.isChecked = false
        }

        radioButtonAllNodes.setOnClickListener {
            radioButtonCurrentNodes.isChecked = false
            radioButtonAllNodes.isChecked = true
        }

        radioButtonLabelLowPriority.setOnClickListener {
            radioButtonLabelLowPriority.isChecked = true
            radioButtonLabelMediumPriority.isChecked = false
            radioButtonLabelHighPriority.isChecked = false

            imageViewBigPriority.setImageResource(R.drawable.ic_low_priority)
            textViewPriorityName.text = getString(R.string.label_low_priority)
        }

        radioButtonLabelMediumPriority.setOnClickListener {
            radioButtonLabelLowPriority.isChecked = false
            radioButtonLabelMediumPriority.isChecked = true
            radioButtonLabelHighPriority.isChecked = false

            imageViewBigPriority.setImageResource(R.drawable.ic_medium_priority)
            textViewPriorityName.text = getString(R.string.label_medium_priority)
        }

        radioButtonLabelHighPriority.setOnClickListener {
            radioButtonLabelLowPriority.isChecked = false
            radioButtonLabelMediumPriority.isChecked = false
            radioButtonLabelHighPriority.isChecked = true

            imageViewBigPriority.setImageResource(R.drawable.ic_high_priority)
            textViewPriorityName.text = getString(R.string.label_high_priority)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun drawerFilter() = with(binding){
        recyclerViewNode.adapter = nodeFilterAdapter
        recyclerViewAlertStatus.adapter = alertStatusFilterAdapter
        recyclerViewNotificationPriority.adapter = notificationPriorityFilterAdapter
        recyclerViewClass.adapter = classFilterAdapter
        recyclerViewTypeAndComponent.adapter = typeAndComponentFilterAdapter

        nodeFilterAdapter.list.add(FilterList("debian-2gb-nbg1-2", "2"))
        nodeFilterAdapter.list.add(FilterList("debian-2gb-nbg1", "1"))
        nodeFilterAdapter.list.add(FilterList("debian-2gb", "1"))
        nodeFilterAdapter.list.add(FilterList("debian-2gb-nbg1-23442", "1"))
        nodeFilterAdapter.list.add(FilterList("debian-2gb-n", "1"))
        nodeFilterAdapter.list.add(FilterList("debian-2gb-nbg1", ""))
        nodeFilterAdapter.notifyDataSetChanged()

        alertStatusFilterAdapter.list.add(FilterList(AlertStatus.CRITICAL.type, "4"))
        alertStatusFilterAdapter.list.add(FilterList(AlertStatus.WARNING.type, "3"))
        alertStatusFilterAdapter.list.add(FilterList(AlertStatus.CLEAR.type, ""))
        alertStatusFilterAdapter.notifyDataSetChanged()

        notificationPriorityFilterAdapter.list.add(FilterList("High", "3", icon = R.drawable.ic_high_priority, isIcon = true))
        notificationPriorityFilterAdapter.list.add(FilterList("Medium", "2", icon = R.drawable.ic_medium_priority, isIcon = true))
        notificationPriorityFilterAdapter.list.add(FilterList("Low", "", icon = R.drawable.ic_low_priority, isIcon = true))
        notificationPriorityFilterAdapter.notifyDataSetChanged()

        classFilterAdapter.list.add(FilterList("Errors", "4"))
        classFilterAdapter.list.add(FilterList("Latency", "3"))
        classFilterAdapter.list.add(FilterList("Utilization", ""))
        classFilterAdapter.list.add(FilterList("Workload", ""))
        classFilterAdapter.notifyDataSetChanged()

        typeAndComponentFilterAdapter.list.add(FilterList("System", "1"))
        typeAndComponentFilterAdapter.list.add(FilterList("Other", "1"))
        typeAndComponentFilterAdapter.notifyDataSetChanged()
    }

    private fun filterCount(){
        val nodeCount = nodeFilterAdapter.list.filter { it.isSelected }
        val alertStatusCount = alertStatusFilterAdapter.list.filter { it.isSelected }
        val notificationPriorityCount = notificationPriorityFilterAdapter.list.filter { it.isSelected }
        val classCount = classFilterAdapter.list.filter { it.isSelected }
        val typAndComponentCount = typeAndComponentFilterAdapter.list.filter { it.isSelected }

        totalFilterCount = nodeCount.size + alertStatusCount.size + notificationPriorityCount.size + classCount.size + typAndComponentCount.size

        if(totalFilterCount != 0){
            binding.buttonApplyFilter.text = "Apply ($totalFilterCount) Filters"
        } else {
            binding.buttonApplyFilter.text = "Apply Filters"
        }


    }
}