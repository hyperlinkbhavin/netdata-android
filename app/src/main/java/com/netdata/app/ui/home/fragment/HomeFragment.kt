package com.netdata.app.ui.home.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.netdata.app.R
import com.netdata.app.data.pojo.enumclass.AlertStatus
import com.netdata.app.data.pojo.enumclass.Priority
import com.netdata.app.data.pojo.request.*
import com.netdata.app.data.pojo.response.HomeNotificationList
import com.netdata.app.data.pojo.response.RoomList
import com.netdata.app.databinding.HomeFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.auth.AuthActivity
import com.netdata.app.ui.auth.IsolatedFullActivity
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.adapter.*
import com.netdata.app.ui.notification.fragment.NotificationFragment
import com.netdata.app.ui.settings.fragment.SettingsFragment
import com.netdata.app.utils.*
import com.netdata.app.utils.customapi.ApiViewModel
import com.netdata.app.utils.localdb.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    lateinit var flexLayoutManager: FlexboxLayoutManager
    lateinit var dbHelper: DatabaseHelper

    private var roomsItemPosition = 0

    private var totalFilterCount = 0

    private var homeList = ArrayList<HomeNotificationList>()
    private var roomList = ArrayList<RoomList>()

    private var filterStatusList = ArrayList<FilterList>()
    private var filterPriorityList = ArrayList<FilterList>()
    private var filterNodesList = ArrayList<FilterList>()
    private var filterClassificationList = ArrayList<FilterList>()
    private var filterTypeCompList = ArrayList<FilterList>()

    private var sortByTimeItemPosition = -1
    private var sortByNotificationPriorityItemPosition = -1
    private var sortByCriticalityItemPosition = -1

    private var isCurrentNodes = true
    private var isApplyFilter = false
    private var isFilterBy = false

    private val homeAdapter by lazy {
        HomeAdapter() { view, position, item ->
            when (view.id) {
                R.id.constraintTop -> {
                    navigator.loadActivity(
                        IsolatedFullActivity::class.java,
                        HomeDetailsFragment::class.java
                    ).start()
                }

                R.id.imageViewPriority -> {
                    bottomSheetPriority(item)
                }

                R.id.leftViewSwipe -> {
                    leftSwipeManage(position, item)
                }

                R.id.rightViewSwipe -> {
                    bottomSheetPriority(item)
                }

                R.id.textViewWarRoomsListCount -> {
                    bottomSheetExistsInWarRooms()
                }
            }
        }
    }

    private val filterSelectedAdapter by lazy {
        FilterSelectedAdapter { view, position, item ->
            when (view.id) {
                R.id.imageViewClose -> {
                    removeFilterSelected(position, item)
                }
            }
        }
    }

    private val nodeFilterAdapter by lazy {
        HomeFilterNodeAdapter(filterNodesList) { view, _, _ ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    tempCount()
                }
            }
        }
    }

    private val alertStatusFilterAdapter by lazy {
        HomeFilterAdapter(filterStatusList) { view, _, _ ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    tempCount()
                }
            }
        }
    }

    private val notificationPriorityFilterAdapter by lazy {
        HomeFilterAdapter(filterPriorityList) { view, _, _ ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    tempCount()
                }
            }
        }
    }

    private val classFilterAdapter by lazy {
        HomeFilterClassAdapter(filterClassificationList) { view, pos, item ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    updateTypeOrClass(pos, true)
                    tempCount()
                }
            }
        }
    }

    private val typeAndComponentFilterAdapter by lazy {
        HomeFilterTypeCompAdapter(filterTypeCompList) { view, pos, item ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    updateTypeOrClass(pos, false)
                    tempCount()
                }
            }
        }
    }

    private fun updateTypeOrClass(pos: Int, isClass: Boolean) {
        if (isClass) {
            filterClassificationList[pos].isSelected = !filterClassificationList[pos].isSelected
            classFilterAdapter.notifyItemChanged(pos)
        } else {
            filterTypeCompList[pos].isSelected = !filterTypeCompList[pos].isSelected
            typeAndComponentFilterAdapter.notifyItemChanged(pos)
        }

    }

    private var isAllButtonSelected = true

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLinkDevice()
        observeFetchHomeNotification()
        observeRoomList()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(inflater, container, attachToRoot)
    }

    override fun bindData() {
        dbHelper = DatabaseHelper(requireContext())
        toolbar()
        manageClick()
        setAdapter()
//        addData()

        binding.buttonAll.isSelected = isAllButtonSelected
        binding.buttonUnread.isSelected = !isAllButtonSelected

        editTextHomeChanged()
        editTextFilterChanged()

//        dbHelper.deleteFetchNotificationOlderThanWeek(ConvertDateTimeFormat.getDaysBeforeDate(17))
    }

    override fun onResume() {
        super.onResume()
        callLinkDevice()
        if (appPreferences.getBoolean(Constant.APP_PREF_FROM_NOTIFICATION)) {
            showMessage("You are viewing ${appPreferences.getString(Constant.APP_PREF_SPACE_NAME)}")
            appPreferences.putBoolean(Constant.APP_PREF_FROM_NOTIFICATION, false)
        }

        if (appPreferences.getString(Constant.APP_PREF_SPACE_NAME).isNotEmpty()) {
            binding.includeToolbar.textViewSpace.text =
                appPreferences.getString(Constant.APP_PREF_SPACE_NAME)
        }
    }

    private fun insertDataIfEmpty() {
        if(dbHelper.getAllDataFromFetchNotification(isSimpleData = true).isEmpty()){
            val gson = Gson()
            val type = object : TypeToken<List<HomeNotificationList>>() {}.type
            val alarmDataList: List<HomeNotificationList> = gson.fromJson(Constant.dummyData, type)
            var lastId: Long = dbHelper.getLastIdFromTable("fetchNotifications")
            for (item in alarmDataList) {
                lastId++
                dbHelper.insertFetchNotificationData(lastId, item)
            }
        }
    }

    private fun toolbar() = with(binding) {
        includeToolbar.apply {
            textViewSpace.visible()
            imageViewSetting.visible()
            imageViewFilter.visible()
            imageViewNotification.visible()

            /*textViewNotificationCount.visible()
            textViewNotificationCount.text = "3"*/
        }
    }

    private fun editTextHomeChanged() {
        binding.editTextSearchServices.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

        })
    }

    private fun editTextFilterChanged() {
        binding.editTextSearchHere.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterSearch(s.toString())
            }

        })
    }

    @SuppressLint("SetTextI18n")
    fun filter(text: String?) {
        val temp = ArrayList<HomeNotificationList>()
        for (d in homeList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.data!!.alarm!!.name!!.contains(text!!, true) ||
                d.data!!.node!!.hostname!!.contains(text, true) ||
                d.data!!.alarm!!.chart!!.contains(text, true)
            ) {
                temp.add(d)
            }
        }
        //update recyclerview
        val unreadItem = temp.filter { !it.isRead }

        binding.buttonAll.text = "${getString(R.string.btn_all)} (${temp.size})"
        binding.buttonUnread.text = "${getString(R.string.btn_unread)} (${unreadItem.size})"
        if (!isAllButtonSelected) {
            homeAdapter.updateList(temp.filter { !it.isRead } as ArrayList<HomeNotificationList>)
        } else {
            homeAdapter.updateList(temp)
        }

    }

    fun filterSearch(text: String?) = with(binding) {
        val tsNode = ArrayList<FilterList>()
        val tsStatus = ArrayList<FilterList>()
        val tsPriority = ArrayList<FilterList>()
        val tsClass = ArrayList<FilterList>()
        val tsType = ArrayList<FilterList>()

        nodeFilterAdapter.updateList(getFilterResult(filterNodesList, tsNode, text))
        alertStatusFilterAdapter.updateList(getFilterResult(filterStatusList, tsStatus, text))
        notificationPriorityFilterAdapter.updateList(
            getFilterResult(
                filterPriorityList,
                tsPriority,
                text
            )
        )
        classFilterAdapter.updateList(getFilterResult(filterClassificationList, tsClass, text))
        typeAndComponentFilterAdapter.updateList(getFilterResult(filterTypeCompList, tsType, text))

        getFilterCondition(tsNode, constraintNode, viewNode)
        getFilterCondition(tsStatus, constraintAlertStatus, viewAlertStatus)
        getFilterCondition(tsPriority, constraintNotificationPriority, viewNotificationPriority)
        getFilterCondition(tsClass, constraintClass, viewClass)
        getFilterCondition(tsType, constraintTypeAndComponent, viewTypeAndComponent)

    }

    private fun getFilterResult(
        list1: ArrayList<FilterList>,
        list2: ArrayList<FilterList>,
        text: String?
    ): ArrayList<FilterList> {
        for (item in list1) {
            if (item.name.contains(text!!, true)) {
                list2.add(item)
            }
        }
        return list2
    }

    private fun getFilterCondition(list: ArrayList<FilterList>, view1: View, view2: View) {
        if (list.isEmpty()) {
            view1.gone()
            view2.gone()
        } else {
            view1.visible()
            view2.visible()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun manageClick() = with(binding) {
        buttonAll.setOnClickListener {
            isAllButtonSelected = true
            binding.buttonAll.isSelected = isAllButtonSelected
            binding.buttonUnread.isSelected = !isAllButtonSelected

            homeAdapter.list.clear()
            homeAdapter.list.addAll(getAllData())
            homeAdapter.notifyDataSetChanged()
        }

        buttonUnread.setOnClickListener {
            isAllButtonSelected = false
            binding.buttonAll.isSelected = isAllButtonSelected
            binding.buttonUnread.isSelected = !isAllButtonSelected

            homeAdapter.list.clear()
            homeAdapter.list.addAll(getAllData(isUnread = true))

            homeAdapter.notifyDataSetChanged()
        }

        textViewLabelMarkAllAsRead.setOnClickListener {
            dbHelper.updateFetchNotificationDataByAllRead()
            callFetchHomeNotification()
        }

        includeToolbar.imageViewSetting.setOnClickListener {
            navigator.loadActivity(IsolatedFullActivity::class.java, SettingsFragment::class.java)
                .start()
        }

        includeToolbar.imageViewFilter.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        includeToolbar.imageViewNotification.setOnClickListener {
            navigator.loadActivity(
                IsolatedFullActivity::class.java,
                NotificationFragment::class.java
            ).start()
        }

        includeToolbar.textViewSpace.setOnClickListener {
            navigator.loadActivity(
                IsolatedFullActivity::class.java,
                ChooseSpaceFragment::class.java
            ).start()
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
                if (totalFilterCount != 0) {
                    isFilterBy = true
                    isApplyFilter = true
                    constraintFilterSelected.visible()
                    filterSelectedAdapter.list.clear()
                    setFilterSelectedData(filterNodesList, 1)
                    setFilterSelectedData(filterStatusList, 2)
                    setFilterSelectedData(filterPriorityList, 3)
                    setFilterSelectedData(filterClassificationList, 4)
                    setFilterSelectedData(filterTypeCompList, 5)

                    filterSelectedAdapter.notifyDataSetChanged()

                    callFetchHomeNotification()
                    applyFilterCount()

                } else {
                    isFilterBy = false
                    includeToolbar.textViewFilterCount.gone()
                    constraintFilterSelected.gone()
                    callFetchHomeNotification()
                }
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }

        imageViewNodeDown.setOnClickListener {
            changeFilterRotation(imageViewNodeDown, recyclerViewNode)
        }

        imageViewAlertStatusDown.setOnClickListener {
            changeFilterRotation(imageViewAlertStatusDown, recyclerViewAlertStatus)
        }

        imageViewNotificationPriorityDown.setOnClickListener {
            changeFilterRotation(
                imageViewNotificationPriorityDown,
                recyclerViewNotificationPriority
            )
        }

        imageViewClassDown.setOnClickListener {
            changeFilterRotation(imageViewClassDown, recyclerViewClass)
        }

        imageViewTypeAndComponentDown.setOnClickListener {
            changeFilterRotation(imageViewTypeAndComponentDown, recyclerViewTypeAndComponent)
        }
    }

    private fun setFilterSelectedData(list1: ArrayList<FilterList>, id: Int) {
        if (list1.isNotEmpty()) {
            list1.forEach {
                if (it.isSelected) {
                    filterSelectedAdapter.list.add(FilterSelectedList(it.name, id))
                }
            }
        }
    }

    private fun changeFilterRotation(arrowView: View, recyclerView: View) {
        if (arrowView.rotation == 0.0F) {
            arrowView.rotation = 180.0F
            recyclerView.gone()
        } else {
            arrowView.rotation = 0.0F
            recyclerView.visible()
        }
    }

    private fun getAllData(isUnread: Boolean = false): ArrayList<HomeNotificationList> {
        val data = ArrayList<HomeNotificationList>()
        if (binding.editTextSearchServices.text!!.trim().isNotEmpty()) {
            for (d in homeList) {
                val text = binding.editTextSearchServices.text!!.trim()
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.data!!.alarm!!.name!!.contains(text, true) ||
                    d.data!!.node!!.hostname!!.contains(text, true) ||
                    d.data!!.alarm!!.chart!!.contains(text, true)
                ) {
                    data.add(d)
                }
            }
        } else {
            data.addAll(homeList)
        }

        return if (isUnread) data.filter { !it.isRead } as ArrayList<HomeNotificationList>
        else data
    }

    /*private fun addChip(text: String) {
        val chip = Chip(binding.chipGroupFilterSelected.context)
        chip.text = text
        chip.isCloseIconVisible = true

        chip.setOnClickListener {
            binding.chipGroupFilterSelected.removeView(chip)
        }

        binding.chipGroupFilterSelected.addView(chip)
    }*/

    @SuppressLint("NotifyDataSetChanged")
    private fun removeFilterSelected(position: Int, item: FilterSelectedList) {

        when (item.id) {
            1 -> changeRemoveFilterData(filterNodesList, item)
            2 -> changeRemoveFilterData(filterStatusList, item)
            3 -> changeRemoveFilterData(filterPriorityList, item)
            4 -> changeRemoveFilterData(filterClassificationList, item)
            5 -> changeRemoveFilterData(filterTypeCompList, item)
        }

        tempCount()
        applyFilterCount()
        notifyDrawer()
        callFetchHomeNotification()
        filterSelectedAdapter.list.removeAt(position)
        if (filterSelectedAdapter.list.size == 0) {
            binding.constraintFilterSelected.gone()
        }

        filterSelectedAdapter.notifyDataSetChanged()
    }

    private fun changeRemoveFilterData(list: ArrayList<FilterList>, item: FilterSelectedList) {
        for (i in list.indices) {
            if (list[i].name.equals(item.name, true)) {
                list[i].isSelected = false
            }
        }
    }

    private fun setAdapter() = with(binding) {
        /*flexLayoutManager = FlexboxLayoutManager(context)
        flexLayoutManager.flexDirection = FlexDirection.ROW*/

        recyclerViewHome.adapter = homeAdapter
//        recyclerViewFilterSelected.layoutManager = flexLayoutManager
        recyclerViewFilterSelected.adapter = filterSelectedAdapter
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun leftSwipeManage(position: Int, item: HomeNotificationList) {
        if (isAllButtonSelected) {
            dbHelper.updateFetchNotificationData(item)
//            homeAdapter.list[position].isRead = !homeAdapter.list[position].isRead
            homeAdapter.list.clear()
            homeAdapter.list.addAll(getAllData())
            homeAdapter.notifyDataSetChanged()
        } else {
            dbHelper.updateFetchNotificationData(item)
            homeAdapter.list.clear()
            homeAdapter.list.addAll(getAllData(isUnread = true))
            homeAdapter.notifyDataSetChanged()
        }
        binding.buttonAll.text = "${getString(R.string.btn_all)} (${getAllData().size})"
        binding.buttonUnread.text =
            "${getString(R.string.btn_unread)} (${getAllData(isUnread = true).size})"
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetPriority(item: HomeNotificationList) {
        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.bottom_sheet_notification_priority, null)

        var priority = item.priority

        val imageViewNotificationWarning =
            view.findViewById<AppCompatImageView>(R.id.imageViewNotificationWarning)
        val textViewSpaceWarningPercent =
            view.findViewById<AppCompatTextView>(R.id.textViewSpaceWarningPercent)
        val textViewSpaceWarningName =
            view.findViewById<AppCompatTextView>(R.id.textViewSpaceWarningName)
        val textViewNodeId = view.findViewById<AppCompatTextView>(R.id.textViewNodeId)
        val textViewDiskSpace = view.findViewById<AppCompatTextView>(R.id.textViewDiskSpace)
        val textViewWarRoomsList = view.findViewById<AppCompatTextView>(R.id.textViewWarRoomsList)
        val imageViewPriority = view.findViewById<AppCompatImageView>(R.id.imageViewPriority)
        val textViewPriority = view.findViewById<AppCompatTextView>(R.id.textViewPriority)

        val textViewLabelEditPrioritySettings =
            view.findViewById<AppCompatTextView>(R.id.textViewLabelEditPrioritySettings)
        val buttonChangeNotificationPriority =
            view.findViewById<AppCompatButton>(R.id.buttonChangeNotificationPriority)

        val constraintNodes = view.findViewById<ConstraintLayout>(R.id.constraintNodes)
        val constraintCurrentNodes =
            view.findViewById<ConstraintLayout>(R.id.constraintCurrentNodes)
        val constraintAllNodes = view.findViewById<ConstraintLayout>(R.id.constraintAllNodes)

        val radioButtonCurrentNodes =
            view.findViewById<AppCompatRadioButton>(R.id.radioButtonCurrentNodes)
        val radioButtonAllNodes = view.findViewById<AppCompatRadioButton>(R.id.radioButtonAllNodes)

        val imageViewBigPriority = view.findViewById<AppCompatImageView>(R.id.imageViewBigPriority)
        val textViewPriorityName = view.findViewById<AppCompatTextView>(R.id.textViewPriorityName)

        val seekBar = view.findViewById<RangeSeekBar>(R.id.seekbar)

        /*radioButtonCurrentNodes.isChecked = isCurrentNodes
        radioButtonAllNodes.isChecked = !isCurrentNodes*/

        if (item.data!!.alarm!!.status.equals(AlertStatus.CRITICAL.type, true)) {
            imageViewNotificationWarning.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorRedFF)
            )
        } else if (item.data!!.alarm!!.status.equals(AlertStatus.WARNING.type, true)) {
            imageViewNotificationWarning.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorYellowF9)
            )
        } else {
            imageViewNotificationWarning.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
        }

        if (item.priority.equals(Priority.HIGH_PRIORITY.shortName, true)) {
            imageViewPriority.setImageResource(R.drawable.ic_high_priority)
            imageViewBigPriority.setImageResource(R.drawable.ic_high_priority)
            seekBar.setProgress(75f)
        } else if (item.priority.equals(Priority.MEDIUM_PRIORITY.shortName, true)) {
            imageViewPriority.setImageResource(R.drawable.ic_medium_priority)
            imageViewBigPriority.setImageResource(R.drawable.ic_medium_priority)
            seekBar.setProgress(45f)
        } else {
            imageViewPriority.setImageResource(R.drawable.ic_low_priority)
            imageViewBigPriority.setImageResource(R.drawable.ic_low_priority)
            seekBar.setProgress(15f)
        }

        textViewSpaceWarningPercent.text = item.data!!.alarm!!.valueWithUnits
        textViewSpaceWarningName.text = item.data!!.alarm!!.name
        textViewNodeId.text = item.data!!.node!!.id
        textViewDiskSpace.text = item.data!!.alarm!!.chart
        textViewWarRoomsList.text = "War Room 1 • War Room 2 • War Room 3 • War Rom 4"
        textViewPriority.text = "${item.priority} ${getString(R.string.label_priority)}"
        textViewPriorityName.text = "${item.priority} ${getString(R.string.label_priority)}"

        buttonChangeNotificationPriority.setOnClickListener {
            /*if(buttonChangeNotificationPriority.text == getString(R.string.btn_done)){
                item.priority = priority
                dbHelper.updateFetchNotificationDataByNode(item, isCurrentNodes)
                callFetchHomeNotification()
                dialog.dismiss()
            } else {
                //            buttonChangeNotificationPriority.gone()
                constraintNodes.visible()
                seekBar.visible()
                buttonChangeNotificationPriority.text = getString(R.string.btn_done)
            }*/

            buttonChangeNotificationPriority.gone()
            constraintNodes.visible()
            seekBar.visible()
//            buttonChangeNotificationPriority.text = getString(R.string.btn_done)
        }

        textViewLabelEditPrioritySettings.setOnClickListener {
            navigator.loadActivity(IsolatedFullActivity::class.java, SettingsFragment::class.java)
                .start()
            dialog.dismiss()
        }

        constraintCurrentNodes.setOnClickListener {
            radioButtonCurrentNodes.isChecked = true
            radioButtonAllNodes.isChecked = false
            isCurrentNodes = true
            item.priority = priority
            dbHelper.updateFetchNotificationDataByNode(item, true)
            callFetchHomeNotification()
            dialog.dismiss()
        }

        constraintAllNodes.setOnClickListener {
            radioButtonCurrentNodes.isChecked = false
            radioButtonAllNodes.isChecked = true
            isCurrentNodes = false
            item.priority = priority
            dbHelper.updateFetchNotificationDataByNode(item, false)
            callFetchHomeNotification()
            dialog.dismiss()
        }

        radioButtonCurrentNodes.setOnClickListener {
            radioButtonCurrentNodes.isChecked = true
            radioButtonAllNodes.isChecked = false
        }

        radioButtonAllNodes.setOnClickListener {
            radioButtonCurrentNodes.isChecked = false
            radioButtonAllNodes.isChecked = true
        }

//        seekBar.setProgress(90f)
        seekBar.setOnRangeChangedListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                view: RangeSeekBar?,
                leftValue: Float,
                rightValue: Float,
                isFromUser: Boolean
            ) {
                when (leftValue.toInt()) {
                    in 0..30 -> {
                        priority = Priority.LOW_PRIORITY.shortName
                        imageViewBigPriority.setImageResource(R.drawable.ic_low_priority)
                        textViewPriorityName.text = Priority.LOW_PRIORITY.fullName
                    }
                    in 31..60 -> {
                        priority = Priority.MEDIUM_PRIORITY.shortName
                        imageViewBigPriority.setImageResource(R.drawable.ic_medium_priority)
                        textViewPriorityName.text = Priority.MEDIUM_PRIORITY.fullName
                    }
                    else -> {
                        priority = Priority.HIGH_PRIORITY.shortName
                        imageViewBigPriority.setImageResource(R.drawable.ic_high_priority)
                        textViewPriorityName.text = Priority.HIGH_PRIORITY.fullName
                    }
                }
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {}

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {}
        })

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
                when (view.id) {
                    R.id.constraintMain -> {
                        roomsItemPosition = position
                        binding.textViewLabelAllWarRooms.text = item.name
                        callFetchHomeNotification()
                        dialog.dismiss()
                    }
                }
            }
        }

        allWarRoomsAdapter.list.addAll(roomList)

        val recyclerViewSelectWarRooms =
            view.findViewById<RecyclerView>(R.id.recyclerViewSelectWarRooms)
        val textViewLabelClose = view.findViewById<AppCompatTextView>(R.id.textViewLabelClose)
        recyclerViewSelectWarRooms.adapter = allWarRoomsAdapter

        if (roomsItemPosition != -1) {
            allWarRoomsAdapter.selectionPosition = roomsItemPosition
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

    private fun bottomSheetExistsInWarRooms() {

        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.bottom_sheet_existis_war_rooms_list, null)

        val existisWarRoomsAdapter by lazy {
            ExistisWarRoomsAdapter() { view, position, item ->
                when (view.id) {
                    R.id.constraintMain -> {
                        roomsItemPosition = position
                        binding.textViewLabelAllWarRooms.text = item.name
                        dialog.dismiss()
                    }
                }
            }
        }

        existisWarRoomsAdapter.list.add(
            ExistisWarRoomsList(
                "War Rooms 1",
                R.drawable.ic_medium_priority
            )
        )
        existisWarRoomsAdapter.list.add(
            ExistisWarRoomsList(
                "War Rooms 2",
                R.drawable.ic_high_priority
            )
        )
        existisWarRoomsAdapter.list.add(
            ExistisWarRoomsList(
                "War Rooms 3",
                R.drawable.ic_medium_priority
            )
        )
        existisWarRoomsAdapter.list.add(
            ExistisWarRoomsList(
                "War Rooms 4",
                R.drawable.ic_low_priority
            )
        )
        existisWarRoomsAdapter.list.add(
            ExistisWarRoomsList(
                "War Rooms 5",
                R.drawable.ic_low_priority
            )
        )

        val recyclerViewNotificationExists =
            view.findViewById<RecyclerView>(R.id.recyclerViewNotificationExists)
        val imageViewWarRoomsBack =
            view.findViewById<AppCompatImageView>(R.id.imageViewWarRoomsBack)
        recyclerViewNotificationExists.adapter = existisWarRoomsAdapter

        imageViewWarRoomsBack.setOnClickListener {
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

        lateinit var sortByTimeAdapter: SortByAdapter
        lateinit var sortByNotificationPriorityAdapter: SortByAdapter
        lateinit var sortByCriticalityAdapter: SortByAdapter

        sortByTimeAdapter =
            SortByAdapter() { view, position, item ->
                when (view.id) {
                    R.id.constraintMain -> {
                        sortByTimeItemPosition = getItemPosition(item)
                        sortByNotificationPriorityItemPosition = -1
                        sortByCriticalityItemPosition = -1
                        sortByNotificationPriorityAdapter.selectedItemPosition = -1
                        sortByCriticalityAdapter.selectedItemPosition = -1

                        sortByNotificationPriorityAdapter.notifyDataSetChanged()
                        sortByCriticalityAdapter.notifyDataSetChanged()
                    }
                }
            }


        sortByNotificationPriorityAdapter =
            SortByAdapter() { view, position, item ->
                when (view.id) {
                    R.id.constraintMain -> {
                        sortByNotificationPriorityItemPosition = getItemPosition(item)
                        sortByTimeItemPosition = -1
                        sortByCriticalityItemPosition = -1
                        sortByTimeAdapter.selectedItemPosition = -1
                        sortByCriticalityAdapter.selectedItemPosition = -1

                        sortByTimeAdapter.notifyDataSetChanged()
                        sortByCriticalityAdapter.notifyDataSetChanged()
                    }
                }
            }

        sortByCriticalityAdapter =
            SortByAdapter() { view, position, item ->
                when (view.id) {
                    R.id.constraintMain -> {
                        sortByCriticalityItemPosition = getItemPosition(item)
                        sortByTimeItemPosition = -1
                        sortByNotificationPriorityItemPosition = -1
                        sortByTimeAdapter.selectedItemPosition = -1
                        sortByNotificationPriorityAdapter.selectedItemPosition = -1

                        sortByTimeAdapter.notifyDataSetChanged()
                        sortByNotificationPriorityAdapter.notifyDataSetChanged()
                    }
                }
            }

        val recyclerViewTime = view.findViewById<RecyclerView>(R.id.recyclerViewTime)
        val recyclerViewNotificationPriority =
            view.findViewById<RecyclerView>(R.id.recyclerViewNotificationPriority)
        val recyclerViewCriticality = view.findViewById<RecyclerView>(R.id.recyclerViewCriticality)
        val textViewLabelClose = view.findViewById<AppCompatTextView>(R.id.textViewLabelClose)
        recyclerViewTime.adapter = sortByTimeAdapter
        recyclerViewNotificationPriority.adapter = sortByNotificationPriorityAdapter
        recyclerViewCriticality.adapter = sortByCriticalityAdapter

        Constant.addData()
        /*sortByTimeAdapter.list.clear()
        sortByNotificationPriorityAdapter.list.clear()
        sortByTimeAdapter.list.clear()*/
        sortByTimeAdapter.list.addAll(Constant.sortByTimeList)
        sortByNotificationPriorityAdapter.list.addAll(Constant.sortByNotificationPriorityList)
        sortByCriticalityAdapter.list.addAll(Constant.sortByCriticalityList)
        /*sortByTimeAdapter.list.add(WarRoomsList("New on top"))
        sortByTimeAdapter.list.add(WarRoomsList("Old on top"))

        sortByNotificationPriorityAdapter.list.add(WarRoomsList("High to Low"))
        sortByNotificationPriorityAdapter.list.add(WarRoomsList("Low to High"))

        sortByCriticalityAdapter.list.add(WarRoomsList("Critical to Clear"))
        sortByCriticalityAdapter.list.add(WarRoomsList("Clear to Critical"))*/

        if (sortByTimeItemPosition != -1) {
            sortByTimeAdapter.selectedItemPosition = sortByTimeItemPosition
        }

        if (sortByNotificationPriorityItemPosition != -1) {
            sortByNotificationPriorityAdapter.selectedItemPosition =
                sortByNotificationPriorityItemPosition
        }

        if (sortByCriticalityItemPosition != -1) {
            sortByCriticalityAdapter.selectedItemPosition = sortByCriticalityItemPosition
        }

        sortByTimeAdapter.notifyDataSetChanged()
        sortByNotificationPriorityAdapter.notifyDataSetChanged()
        sortByCriticalityAdapter.notifyDataSetChanged()

        textViewLabelClose.setOnClickListener {
            callFetchHomeNotification()
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            callFetchHomeNotification()
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(true)

        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                callFetchHomeNotification()
                dialog.dismiss()
            }
            true
        }

        dialog.show()
    }

    private fun getItemPosition(list: ArrayList<WarRoomsList>): Int {
        for (i in list.indices) {
            if (list[i].isSelected) {
                return i
            }
        }
        return -1
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun drawerFilter() = with(binding) {
        recyclerViewNode.adapter = nodeFilterAdapter
        recyclerViewAlertStatus.adapter = alertStatusFilterAdapter
        recyclerViewNotificationPriority.adapter = notificationPriorityFilterAdapter
        recyclerViewClass.adapter = classFilterAdapter
        recyclerViewTypeAndComponent.adapter = typeAndComponentFilterAdapter

        if (filterStatusList.isEmpty()) {
            filterStatusList.add(FilterList(AlertStatus.CRITICAL.type, "4"))
            filterStatusList.add(FilterList(AlertStatus.WARNING.type, "3"))
            filterStatusList.add(FilterList(AlertStatus.CLEAR.type, ""))
        }

        if (filterPriorityList.isEmpty()) {
            filterPriorityList.add(
                FilterList(
                    "High",
                    "3",
                    icon = R.drawable.ic_high_priority,
                    isIcon = true
                )
            )
            filterPriorityList.add(
                FilterList(
                    "Medium",
                    "2",
                    icon = R.drawable.ic_medium_priority,
                    isIcon = true
                )
            )
            filterPriorityList.add(
                FilterList(
                    "Low",
                    "",
                    icon = R.drawable.ic_low_priority,
                    isIcon = true
                )
            )
        }

        notifyDrawer()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyDrawer() = with(binding) {
        nodeFilterAdapter.notifyDataSetChanged()
        alertStatusFilterAdapter.notifyDataSetChanged()
        classFilterAdapter.notifyDataSetChanged()
        typeAndComponentFilterAdapter.notifyDataSetChanged()
        notificationPriorityFilterAdapter.notifyDataSetChanged()
    }

    private fun filterCount() {
        val nodeCount = filterNodesList.filter { it.isSelected }
        val alertStatusCount = filterStatusList.filter { it.isSelected }
        val notificationPriorityCount = filterPriorityList.filter { it.isSelected }
        val classCount = filterClassificationList.filter { it.isSelected }
        val typAndComponentCount = filterTypeCompList.filter { it.isSelected }

        totalFilterCount =
            nodeCount.size + alertStatusCount.size + notificationPriorityCount.size + classCount.size + typAndComponentCount.size

    }

    private fun tempCount() = with(binding) {
        filterCount()

        if (totalFilterCount != 0) {
            buttonApplyFilter.text = "Apply ($totalFilterCount) Filters"
        } else {
            buttonApplyFilter.text = "Apply Filters"
            isFilterBy = false
        }

        isApplyFilter = false
    }

    private fun applyFilterCount() = with(binding) {
        filterCount()
        if (totalFilterCount != 0) {
            if (isFilterBy) {
                includeToolbar.textViewFilterCount.visible()
                includeToolbar.textViewFilterCount.text = totalFilterCount.toString()
                textViewLabelFilterSelected.text = "Filter Selected ($totalFilterCount)"
            } else {
                includeToolbar.textViewFilterCount.gone()
            }

        } else {
            includeToolbar.textViewFilterCount.gone()
            isFilterBy = false
        }
    }

    private fun callLinkDevice() {
        showLoader()
        apiViewModel.callLinkDevice(APIRequest(token = session.deviceId))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLinkDevice() {
        apiViewModel.linkDeviceLiveData.observe(this) {
            hideLoader()
            if (it.responseCode == 200) {
                callRoomList()
            } else {
                showToast("Session expired! Please login again")
                appPreferences.putBoolean(Constant.APP_PREF_IS_LOGIN, false)
                appPreferences.putString(Constant.APP_PREF_SPACE_NAME, "")
                navigator.loadActivity(AuthActivity::class.java).byFinishingAll().start()
            }
        }
//            Log.e("link device", it.toString())
    }

    fun getCurrentUTCTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        val currentTimeInMillis = System.currentTimeMillis()
        return dateFormat.format(Date(currentTimeInMillis))
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun callFetchHomeNotification() {
//        showLoader()
        apiViewModel.callFetchHomeNotification()
        /*homeList.clear()
        homeAdapter.list.clear()

         if (isFilterBy) {
                var tempNodeList = ArrayList<String>()
                if (filterNodesList.isNotEmpty()) {
                    tempNodeList = filterNodesList.filter { it.isSelected }
                        .map { it.otherName } as ArrayList<String>
                }

                val tempClassList: ArrayList<String> = getFilterTempList(filterClassificationList)
                val tempStatusList: ArrayList<String> = getFilterTempList(filterStatusList)
                val tempPriorityList: ArrayList<String> = getFilterTempList(filterPriorityList)
                val tempTypeList: ArrayList<String> = getFilterTempList(filterTypeCompList)

             if(sortByTimeItemPosition != -1 || sortByNotificationPriorityItemPosition != -1 || sortByCriticalityItemPosition != -1){
                 homeList.addAll(
                     dbHelper.getAllDataFromFetchNotification(
                         spaceID = appPreferences.getString(Constant.APP_PREF_SPACE_ID),
                         roomID = roomList[roomsItemPosition].id!!,
                         isFilterBy = true,
                         isSortBy = true,
                         statusFilters = tempStatusList,
                         priorityFilters = tempPriorityList,
                         nodesFilters = tempNodeList,
                         classFilters = tempClassList,
                         typeFilters = tempTypeList,
                         sortByTimeItemPosition = sortByTimeItemPosition,
                         sortByNotificationPriorityItemPosition = sortByNotificationPriorityItemPosition,
                         sortByCriticalityItemPosition = sortByCriticalityItemPosition
                     )
                 )
             } else {
                 homeList.addAll(
                     dbHelper.getAllDataFromFetchNotification(
                         spaceID = appPreferences.getString(Constant.APP_PREF_SPACE_ID),
                         roomID = roomList[roomsItemPosition].id!!,
                         isFilterBy = true,
                         statusFilters = tempStatusList,
                         priorityFilters = tempPriorityList,
                         nodesFilters = tempNodeList,
                         classFilters = tempClassList,
                         typeFilters = tempTypeList
                     )
                 )
             }
                homeAdapter.list.addAll(homeList)
        } else if (sortByTimeItemPosition != -1 || sortByNotificationPriorityItemPosition != -1 || sortByCriticalityItemPosition != -1) {
            homeList.addAll(
                dbHelper.getAllDataFromFetchNotification(
                    spaceID = appPreferences.getString(Constant.APP_PREF_SPACE_ID),
                    roomID = roomList[roomsItemPosition].id!!,
                    isSortBy = true,
                    sortByTimeItemPosition = sortByTimeItemPosition,
                    sortByNotificationPriorityItemPosition = sortByNotificationPriorityItemPosition,
                    sortByCriticalityItemPosition = sortByCriticalityItemPosition
                )
            )
            homeAdapter.list.addAll(homeList)
        } else {
            homeList.addAll(
                dbHelper.getAllDataFromFetchNotification(
                    spaceID = appPreferences.getString(Constant.APP_PREF_SPACE_ID),
                    roomID = roomList[roomsItemPosition].id!!
                )
            )
            homeAdapter.list.addAll(homeList)
        }

        val tempNodeList = ArrayList<HomeNotificationList.Data.Node>()
        val tempClassList = ArrayList<HomeNotificationList.Data.Alarm>()
        val tempTypeCompList = ArrayList<HomeNotificationList.Data.Alarm>()

        if (filterNodesList.isEmpty()) {
            homeList.forEach {
                tempNodeList.add(it.data!!.node!!)
            }
            val tempList = tempNodeList.distinctBy { it.hostname }
            for (item in tempList) {
                filterNodesList.add(
                    FilterList(
                        item.hostname!!,
                        otherName = item.id,
                        isSelected = false
                    )
                )
            }
        }
        if (filterClassificationList.isEmpty()) {
            homeList.forEach {
                tempClassList.add(it.data!!.alarm!!)
            }
            val tempList = tempClassList.distinctBy { it.classification }.map { it.classification }
            for (item in tempList) {
                filterClassificationList.add(FilterList(item!!, isSelected = false))
            }
        }
        if (filterTypeCompList.isEmpty()) {
            homeList.forEach {
                tempTypeCompList.add(it.data!!.alarm!!)
            }
            val tempList = tempTypeCompList.distinctBy { it.family }.map { it.family }
            for (item in tempList) {
                filterTypeCompList.add(FilterList(item!!, isSelected = false))
            }

        }

        val unreadItem = homeList.filter { !it.isRead }

        binding.buttonAll.text = "${getString(R.string.btn_all)} (${homeList.size})"
        binding.buttonUnread.text = "${getString(R.string.btn_unread)} (${unreadItem.size})"

        homeAdapter.notifyDataSetChanged()
        drawerFilter()*/
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeFetchHomeNotification() {
        apiViewModel.fetchHomeNotificationLiveData.observe(this) {
            hideLoader()
            if (!it.isError || it.responseCode == 200) {
                /*homeAdapter.list.addAll(it.data!!)
                homeAdapter.notifyDataSetChanged()*/
            }
        }
    }

    private fun getFilterTempList(list1: ArrayList<FilterList>): ArrayList<String> {
        var list2 = ArrayList<String>()
        if (list1.isNotEmpty()) {
            list2 = list1.filter { it.isSelected }
                .map { it.name } as ArrayList<String>
        }
        return list2
    }

    private fun callRoomList() {
        showLoader()
        apiViewModel.callGetRoomsList(appPreferences.getString(Constant.APP_PREF_SPACE_ID))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRoomList() {
        apiViewModel.roomListLiveData.observe(this) {
            hideLoader()
            if (!it.isError && it.responseCode == 200) {
                roomList.clear()
                roomList.addAll(it.data!!)
                binding.textViewLabelAllWarRooms.text = roomList[0].name
                insertDataIfEmpty()
                Handler(Looper.getMainLooper()).postDelayed({
                    callFetchHomeNotification()
                },1000)
            }
        }
    }

    override fun onBackActionPerform(): Boolean {
        return if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            false
        } else {
            super.onBackActionPerform()
        }
    }
}