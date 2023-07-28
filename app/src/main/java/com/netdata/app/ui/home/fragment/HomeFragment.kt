package com.netdata.app.ui.home.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.netdata.app.R
import com.netdata.app.data.pojo.HomeDataList
import com.netdata.app.data.pojo.enumclass.AlertStatus
import com.netdata.app.data.pojo.enumclass.Priority
import com.netdata.app.data.pojo.request.*
import com.netdata.app.data.pojo.response.HomeNotificationList
import com.netdata.app.databinding.HomeFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.auth.AuthActivity
import com.netdata.app.ui.auth.IsolatedFullActivity
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.adapter.*
import com.netdata.app.ui.notification.fragment.NotificationFragment
import com.netdata.app.ui.settings.fragment.SettingsFragment
import com.netdata.app.utils.*
import com.netdata.app.utils.Constant.isFilterBy
import com.netdata.app.utils.Constant.sortByCriticalityItemPosition
import com.netdata.app.utils.Constant.sortByNotificationPriorityItemPosition
import com.netdata.app.utils.Constant.sortByTimeItemPosition
import com.netdata.app.utils.customapi.ApiViewModel
import com.netdata.app.utils.localdb.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    lateinit var flexLayoutManager: FlexboxLayoutManager
    lateinit var dbHelper: DatabaseHelper

    private var warRoomsItemPosition = 0

    private var totalFilterCount = 0

    private var homeList = ArrayList<HomeNotificationList>()
    private var tempHomeList = ArrayList<HomeNotificationList>()

    private var isCurrentNodes = true

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
        FilterSelectedAdapter() { view, position, item ->
            when (view.id) {
                R.id.imageViewClose -> {
                    removeFilterSelected(position)
                }
            }
        }
    }

    private val nodeFilterAdapter by lazy {
        HomeFilterAdapter() { view, position, item ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    filterCount()
                }
            }
        }
    }

    private val alertStatusFilterAdapter by lazy {
        HomeFilterAdapter() { view, position, item ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    filterCount()
                }
            }
        }
    }

    private val notificationPriorityFilterAdapter by lazy {
        HomeFilterAdapter() { view, position, item ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    filterCount()
                }
            }
        }
    }

    private val classFilterAdapter by lazy {
        HomeFilterAdapter() { view, position, item ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    filterCount()
                }
            }
        }
    }

    private val typeAndComponentFilterAdapter by lazy {
        HomeFilterAdapter() { view, position, item ->
            when (view.id) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLinkDevice()
        observeFetchHomeNotification()
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

        drawerFilter()
        editTextChanged()
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

    private fun toolbar() = with(binding) {
        includeToolbar.apply {
            textViewSpace.visible()
            imageViewSetting.visible()
            imageViewFilter.visible()
            imageViewNotification.visible()

            textViewNotificationCount.visible()
            textViewNotificationCount.text = "3"
        }
    }

    private fun editTextChanged() {
        binding.editTextSearchServices.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
//                callSearchCity(s.toString())
            }

        })
    }

    @SuppressLint("SetTextI18n")
    fun filter(text: String?) {
        val temp = ArrayList<HomeNotificationList>()
        for (d in homeList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.data!!.alarm!!.name!!.contains(text!!, true)) {
                temp.add(d)
            }
        }
        //update recyclerview
        val unreadItem = temp.filter { !it.isRead }

        binding.buttonAll.text = "${getString(R.string.btn_all)} (${temp.size})"
        binding.buttonUnread.text = "${getString(R.string.btn_unread)} (${unreadItem.size})"
        if(!isAllButtonSelected){
            homeAdapter.updateList(temp.filter { !it.isRead } as ArrayList<HomeNotificationList>)
        } else {
            homeAdapter.updateList(temp)
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
            homeAdapter.list.addAll(getUnreadData())

            homeAdapter.notifyDataSetChanged()
        }

        textViewLabelMarkAllAsRead.setOnClickListener {
            for (item in homeList) {
                item.isRead = true
            }

            homeAdapter.list.clear()
            homeAdapter.list.addAll(homeList)

            homeAdapter.notifyDataSetChanged()
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
                    includeToolbar.textViewFilterCount.visible()
                    includeToolbar.textViewFilterCount.text = totalFilterCount.toString()
                    constraintFilterSelected.visible()
                    filterSelectedAdapter.list.add(FilterSelectedList("Priority:High"))
                    filterSelectedAdapter.list.add(FilterSelectedList("Priority:Medium"))
                    filterSelectedAdapter.list.add(FilterSelectedList("Priority:Low"))
                    filterSelectedAdapter.list.add(FilterSelectedList("Priority:Medium"))
                    filterSelectedAdapter.list.add(FilterSelectedList("Priority:High"))
                    filterSelectedAdapter.notifyDataSetChanged()

                    callFetchHomeNotification()

                    /*addChip("Priority: High")
                    addChip("Priority: Medium")
                    addChip("Priority: Low")
                    addChip("Class: High1")
                    addChip("Class: High2")
                    addChip("Class: High3")*/


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
            if (imageViewNodeDown.rotation == 0.0F) {
                imageViewNodeDown.rotation = 180.0F
                recyclerViewNode.gone()
            } else {
                imageViewNodeDown.rotation = 0.0F
                recyclerViewNode.visible()
            }
        }

        imageViewAlertStatusDown.setOnClickListener {
            if (imageViewAlertStatusDown.rotation == 0.0F) {
                imageViewAlertStatusDown.rotation = 180.0F
                recyclerViewAlertStatus.gone()
            } else {
                imageViewAlertStatusDown.rotation = 0.0F
                recyclerViewAlertStatus.visible()
            }
        }

        imageViewNotificationPriorityDown.setOnClickListener {
            if (imageViewNotificationPriorityDown.rotation == 0.0F) {
                imageViewNotificationPriorityDown.rotation = 180.0F
                recyclerViewNotificationPriority.gone()
            } else {
                imageViewNotificationPriorityDown.rotation = 0.0F
                recyclerViewNotificationPriority.visible()
            }
        }

        imageViewClassDown.setOnClickListener {
            if (imageViewClassDown.rotation == 0.0F) {
                imageViewClassDown.rotation = 180.0F
                recyclerViewClass.gone()
            } else {
                imageViewClassDown.rotation = 0.0F
                recyclerViewClass.visible()
            }
        }

        imageViewTypeAndComponentDown.setOnClickListener {
            if (imageViewTypeAndComponentDown.rotation == 0.0F) {
                imageViewTypeAndComponentDown.rotation = 180.0F
                recyclerViewTypeAndComponent.gone()
            } else {
                imageViewTypeAndComponentDown.rotation = 0.0F
                recyclerViewTypeAndComponent.visible()
            }
        }
    }

    private fun getAllData(): ArrayList<HomeNotificationList> {
        val data = ArrayList<HomeNotificationList>()
        if (binding.editTextSearchServices.text!!.trim().isNotEmpty()) {
            for (d in homeList) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.data!!.alarm!!.name!!.contains(
                        binding.editTextSearchServices.text!!.trim(),
                        true
                    )
                ) {
                    data.add(d)
                }
            }
        } else {
            data.addAll(homeList)
        }

        return data
    }

    private fun getUnreadData() : ArrayList<HomeNotificationList>{
        val data = ArrayList<HomeNotificationList>()
        if (binding.editTextSearchServices.text!!.trim().isNotEmpty()) {
            for (d in homeList) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.data!!.alarm!!.name!!.contains(
                        binding.editTextSearchServices.text!!.trim(),
                        true
                    )
                ) {
                    data.add(d)
                }
            }
        } else {
            data.addAll(homeList)
        }

        return data.filter { !it.isRead } as ArrayList<HomeNotificationList>
    }

    private fun addChip(text: String) {
        val chip = Chip(binding.chipGroupFilterSelected.context)
        chip.text = text
        chip.isCloseIconVisible = true

        chip.setOnClickListener {
            binding.chipGroupFilterSelected.removeView(chip)
        }

        binding.chipGroupFilterSelected.addView(chip)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeFilterSelected(position: Int) {
        filterSelectedAdapter.list.removeAt(position)
        if (filterSelectedAdapter.list.size == 0) {
            binding.constraintFilterSelected.gone()
        }

        filterSelectedAdapter.notifyDataSetChanged()
    }

    private fun setAdapter() = with(binding) {
        flexLayoutManager = FlexboxLayoutManager(context)
        flexLayoutManager.flexDirection = FlexDirection.ROW

        recyclerViewHome.adapter = homeAdapter
        recyclerViewFilterSelected.layoutManager = flexLayoutManager
        recyclerViewFilterSelected.adapter = filterSelectedAdapter
    }

    /* @SuppressLint("NotifyDataSetChanged")
     private fun addData() {
         homeList.add(
             HomeDataList(
                 "inbound packets dropped ratio",
                 "24 seconds ago · 04/04/2022 - 15:44:23",
                 "gke-staging-streamnative-202103050938-3a4480ce",
                 "disk_space._boot_efi",
                 "War Room 1•War Room 2•War Room 3•War Room 4",
                 "Type & Component : System • Network",
                 true
             )
         )

         homeList.add(
             HomeDataList(
                 "inbound packets dropped ratio",
                 "24 seconds ago · 04/04/2022 - 15:44:23",
                 "gke-staging-streamnative-202103050938-3a4480ce",
                 "disk_space._boot_efi",
                 "War Room 1•War Room 2•War Room 3•War Room 4",
                 "Type & Component : System • Network",
             )
         )

         homeList.add(
             HomeDataList(
                 "inbound packets dropped ratio",
                 "24 seconds ago · 04/04/2022 - 15:44:23",
                 "gke-staging-streamnative-202103050938-3a4480ce",
                 "disk_space._boot_efi",
                 "War Room 1•War Room 2•War Room 3•War Room 4",
                 "Type & Component : System • Network",
                 true
             )
         )

         homeList.add(
             HomeDataList(
                 "inbound packets dropped ratio",
                 "24 seconds ago · 04/04/2022 - 15:44:23",
                 "gke-staging-streamnative-202103050938-3a4480ce",
                 "disk_space._boot_efi",
                 "War Room 1•War Room 2•War Room 3•War Room 4",
                 "Type & Component : System • Network",
             )
         )

         homeList.add(
             HomeDataList(
                 "inbound packets dropped ratio",
                 "24 seconds ago · 04/04/2022 - 15:44:23",
                 "gke-staging-streamnative-202103050938-3a4480ce",
                 "disk_space._boot_efi",
                 "War Room 1•War Room 2•War Room 3•War Room 4",
                 "Type & Component : System • Network",
                 true
             )
         )

         homeList.add(
             HomeDataList(
                 "inbound packets dropped ratio",
                 "24 seconds ago · 04/04/2022 - 15:44:23",
                 "gke-staging-streamnative-202103050938-3a4480ce",
                 "disk_space._boot_efi",
                 "War Room 1•War Room 2•War Room 3•War Room 4",
                 "Type & Component : System • Network",
             )
         )

         homeAdapter.list.addAll(homeList)

         homeAdapter.notifyDataSetChanged()
     }*/

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun leftSwipeManage(position: Int, item: HomeNotificationList) {
        if (isAllButtonSelected) {
            dbHelper.updateFetchNotificationData(item)
//            homeAdapter.list[position].isRead = !homeAdapter.list[position].isRead
            homeAdapter.notifyDataSetChanged()
        } else {
            dbHelper.updateFetchNotificationData(item)
            homeAdapter.list.removeAt(position)
            homeAdapter.notifyDataSetChanged()
        }
        binding.buttonAll.text = "${getString(R.string.btn_all)} (${getAllData().size})"
        binding.buttonUnread.text = "${getString(R.string.btn_unread)} (${getUnreadData().size})"
    }

    @SuppressLint("SetTextI18n")
    private fun bottomSheetPriority(item: HomeNotificationList) {
        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.bottom_sheet_notification_priority, null)

        var priority = item.priority

        val imageViewNotificationWarning = view.findViewById<AppCompatImageView>(R.id.imageViewNotificationWarning)
        val textViewSpaceWarningPercent = view.findViewById<AppCompatTextView>(R.id.textViewSpaceWarningPercent)
        val textViewSpaceWarningName = view.findViewById<AppCompatTextView>(R.id.textViewSpaceWarningName)
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

        if(item.data!!.alarm!!.status.equals(AlertStatus.CRITICAL.type, true)){
            imageViewNotificationWarning.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorRedFF))
        } else if(item.data!!.alarm!!.status.equals(AlertStatus.WARNING.type, true)){
            imageViewNotificationWarning.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorYellowF9))
        } else {
            imageViewNotificationWarning.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        }

        if(item.priority.equals(Priority.HIGH_PRIORITY.shortName, true)){
            imageViewPriority.setImageResource(R.drawable.ic_high_priority)
            imageViewBigPriority.setImageResource(R.drawable.ic_high_priority)
            seekBar.setProgress(75f)
        } else if(item.priority.equals(Priority.MEDIUM_PRIORITY.shortName, true)){
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

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }
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

        val recyclerViewSelectWarRooms =
            view.findViewById<RecyclerView>(R.id.recyclerViewSelectWarRooms)
        val textViewLabelClose = view.findViewById<AppCompatTextView>(R.id.textViewLabelClose)
        recyclerViewSelectWarRooms.adapter = allWarRoomsAdapter

        if (warRoomsItemPosition != -1) {
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

    private fun bottomSheetExistsInWarRooms() {

        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.bottom_sheet_existis_war_rooms_list, null)

        val existisWarRoomsAdapter by lazy {
            ExistisWarRoomsAdapter() { view, position, item ->
                when (view.id) {
                    R.id.constraintMain -> {
                        warRoomsItemPosition = position
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
                        Log.e("item 1", sortByTimeItemPosition.toString())
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
                        Log.e("item 2", sortByNotificationPriorityItemPosition.toString())
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
                        Log.e("item 3", sortByCriticalityItemPosition.toString())
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

        notificationPriorityFilterAdapter.list.add(
            FilterList(
                "High",
                "3",
                icon = R.drawable.ic_high_priority,
                isIcon = true
            )
        )
        notificationPriorityFilterAdapter.list.add(
            FilterList(
                "Medium",
                "2",
                icon = R.drawable.ic_medium_priority,
                isIcon = true
            )
        )
        notificationPriorityFilterAdapter.list.add(
            FilterList(
                "Low",
                "",
                icon = R.drawable.ic_low_priority,
                isIcon = true
            )
        )
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

    private fun filterCount() {
        val nodeCount = nodeFilterAdapter.list.filter { it.isSelected }
        val alertStatusCount = alertStatusFilterAdapter.list.filter { it.isSelected }
        val notificationPriorityCount =
            notificationPriorityFilterAdapter.list.filter { it.isSelected }
        val classCount = classFilterAdapter.list.filter { it.isSelected }
        val typAndComponentCount = typeAndComponentFilterAdapter.list.filter { it.isSelected }

        totalFilterCount =
            nodeCount.size + alertStatusCount.size + notificationPriorityCount.size + classCount.size + typAndComponentCount.size

        if (totalFilterCount != 0) {
            binding.buttonApplyFilter.text = "Apply ($totalFilterCount) Filters"
        } else {
            binding.buttonApplyFilter.text = "Apply Filters"
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
            if (it.isError || it.responseCode != 200) {

                showToast("Session expired! Please login again")
                appPreferences.putBoolean(Constant.APP_PREF_IS_LOGIN, false)
                appPreferences.putString(Constant.APP_PREF_SPACE_NAME, "")
                navigator.loadActivity(AuthActivity::class.java).byFinishingAll().start()

            } else {
                callFetchHomeNotification()
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
//        apiViewModel.callFetchHomeNotification()

        /*val gson = Gson()
                val type = object : TypeToken<List<HomeNotificationList>>() {}.type
                val alarmDataList: List<HomeNotificationList> = gson.fromJson(Constant.dummyData, type)
                var lastId: Long = dbHelper.getLastIdFromTable("fetchNotifications")
                for(item in alarmDataList){
                    lastId++
                    Log.e("id",lastId.toString())
                    dbHelper.insertFetchNotificationData(lastId, item)
                }*/
//                Log.e("current", getCurrentUTCTime())
        Log.e("db data", dbHelper.getAllDataFromFetchNotification(appPreferences.getString(Constant.APP_PREF_SPACE_ID), statusFilters = listOf(), priorityFilters = listOf()).toString())

        homeList.clear()
        homeAdapter.list.clear()
        Log.e("posi", "$sortByTimeItemPosition $sortByNotificationPriorityItemPosition $sortByCriticalityItemPosition")

        if(sortByTimeItemPosition != -1 || sortByNotificationPriorityItemPosition != -1 || sortByCriticalityItemPosition != -1){
            homeList.addAll(dbHelper.getAllDataFromFetchNotification(appPreferences.getString(Constant.APP_PREF_SPACE_ID), isSortBy = true, statusFilters = listOf(), priorityFilters = listOf()))
            homeAdapter.list.addAll(homeList)
        } else if(isFilterBy){
            homeList.addAll(dbHelper.getAllDataFromFetchNotification(appPreferences.getString(Constant.APP_PREF_SPACE_ID), isFilterBy = true, statusFilters = listOf("critical"), priorityFilters = listOf("high")))
            homeAdapter.list.addAll(homeList)
        } else {
            homeList.addAll(dbHelper.getAllDataFromFetchNotification(appPreferences.getString(Constant.APP_PREF_SPACE_ID), statusFilters = listOf(), priorityFilters = listOf()))
            homeAdapter.list.addAll(homeList)
        }

        val unreadItem = homeList.filter { !it.isRead }

        binding.buttonAll.text = "${getString(R.string.btn_all)} (${homeList.size})"
        binding.buttonUnread.text = "${getString(R.string.btn_unread)} (${unreadItem.size})"

        homeAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeFetchHomeNotification() {
        apiViewModel.fetchHomeNotificationLiveData.observe(this) {
            hideLoader()
            if (!it.isError || it.responseCode == 200) {
                Log.e("home data", it.data.toString())
                homeAdapter.list.addAll(it.data!!)
                homeAdapter.notifyDataSetChanged()
            }
        }
    }
}