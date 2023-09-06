package cloud.netdata.android.ui.home.fragment

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.enumclass.AlertStatus
import cloud.netdata.android.data.pojo.enumclass.Priority
import cloud.netdata.android.data.pojo.request.APIRequest
import cloud.netdata.android.data.pojo.request.FilterList
import cloud.netdata.android.data.pojo.request.FilterSelectedList
import cloud.netdata.android.data.pojo.request.WarRoomsList
import cloud.netdata.android.data.pojo.response.HomeNotificationList
import cloud.netdata.android.data.pojo.response.NotificationRetention
import cloud.netdata.android.data.pojo.response.RoomList
import cloud.netdata.android.databinding.HomeFragmentBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.auth.AuthActivity
import cloud.netdata.android.ui.auth.IsolatedFullActivity
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.ui.home.adapter.*
import cloud.netdata.android.ui.notification.NotificationBroadcastReceiver
import cloud.netdata.android.ui.notification.fragment.NotificationFragment
import cloud.netdata.android.ui.settings.fragment.SettingsFragment
import cloud.netdata.android.utils.*
import cloud.netdata.android.utils.customapi.ApiViewModel
import cloud.netdata.android.utils.customapi.DynamicViewModel
import cloud.netdata.android.utils.localdb.DatabaseHelper
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import kotlinx.android.synthetic.main.bottom_sheet_notification_priority.*
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    private val dynamicViewModel by lazy {
        ViewModelProvider(this)[DynamicViewModel::class.java]
    }

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    private var notificationReceiver: NotificationBroadcastReceiver? = null
    lateinit var flexLayoutManager: FlexboxLayoutManager
    lateinit var dbHelper: DatabaseHelper

    private var roomsItemPosition = 0

    private var totalFilterCount = 0

    private var homeList = ArrayList<HomeNotificationList>()
    private var roomList = ArrayList<RoomList>()
    private var deeplink = ""

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
                    readUnreadNotification(item, isSwipeRead = true)
                    val url = "https://app.netdata.cloud/webviews/alerts/${item.data!!.netdata!!.alert!!.transition!!.id}" +
                            "?space_id=${item.data!!.netdata!!.space!!.id}&room_id=${item.data!!.netdata!!.room[0].id}#token=${session.userSession.drop(7)}"
                    navigator.loadActivity(
                        IsolatedFullActivity::class.java,
                        HomeDetailsFragment::class.java
                    )
                        .addBundle(bundleOf(Constant.BUNDLE_URL to url))
                        .start()
                }

                R.id.imageViewPriority -> {
                    bottomSheetPriority(item)
                }

                R.id.leftViewSwipe -> {
                    readUnreadNotification(item, isSwipeRead = true)
                }

                R.id.textViewUndo -> {
                    homeList[position].isTempMessageRead = false
                    readUnreadNotification(item, isSwipeRead = true)
                }

                R.id.rightViewSwipe -> {
                    bottomSheetPriority(item)
                }

                R.id.textViewWarRoomsListCount -> {
                    bottomSheetExistsInWarRooms(item)
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
                    filterCount()
                }
            }
        }
    }

    private val alertStatusFilterAdapter by lazy {
        HomeFilterAdapter(filterStatusList) { view, _, _ ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    tempCount()
                    filterCount()
                }
            }
        }
    }

    private val notificationPriorityFilterAdapter by lazy {
        HomeFilterAdapter(filterPriorityList) { view, _, _ ->
            when (view.id) {
                R.id.checkBoxFilter -> {
                    tempCount()
                    filterCount()
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
                    filterCount()
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
                    filterCount()
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
        observeGetHomeNotificationOnNotify()
        observeRoomList()
        observeDynamicLink()
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

        try {
            manageNotificationRetentionData()
        } catch (e: Exception) {
        }

        binding.buttonAll.isSelected = isAllButtonSelected
        binding.buttonUnread.isSelected = !isAllButtonSelected

        editTextHomeChanged()
        editTextFilterChanged()

        deeplink = arguments?.getString(Constant.BUNDLE_DEEPLINK).toString()

        if (appPreferences.getString(Constant.APP_PREF_SORTING_BY_TIME).isEmpty()) {
            appPreferences.putString(Constant.APP_PREF_SORTING_BY_TIME, "-1")
        }
        if(appPreferences.getString(Constant.APP_PREF_SORTING_BY_PRIORITY).isEmpty()){
            appPreferences.putString(Constant.APP_PREF_SORTING_BY_PRIORITY, "-1")
        }
        if(appPreferences.getString(Constant.APP_PREF_SORTING_BY_CRITICALITY).isEmpty()){
            appPreferences.putString(Constant.APP_PREF_SORTING_BY_CRITICALITY, "-1")
        }
//        dbHelper.deleteFetchNotificationOlderThanWeek(ConvertDateTimeFormat.getDaysBeforeDate(17))
    }

    override fun onResume() {
        super.onResume()


        if (deeplink.isNotEmpty()) {
            callDynamicLink(deeplink)
            deeplink = ""
        } else {
//            session.getFirebaseDeviceId { deviceId ->
//                session.deviceId = deviceId
                callLinkDevice()
//            }
        }

        notificationReceiver = NotificationBroadcastReceiver(this)
        context?.registerReceiver(notificationReceiver, IntentFilter(Constant.MY_NOTIFICATION_ACTION))

        if (appPreferences.getBoolean(Constant.APP_PREF_FROM_NOTIFICATION)) {
            showSnackBar("You are viewing ${appPreferences.getString(Constant.APP_PREF_SPACE_NAME)}", binding.editTextSearchServices)
            /*val msg = "You are viewing ${appPreferences.getString(Constant.APP_PREF_SPACE_NAME)}"
            val snackBar = Snackbar.make(binding.constraintAllWarRooms, msg, Snackbar.LENGTH_LONG)
            // Change background color
            snackBar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorYellowF9))

            // Change text color
            snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            snackBar.show()*/
            appPreferences.putBoolean(Constant.APP_PREF_FROM_NOTIFICATION, false)
        }

        if (appPreferences.getString(Constant.APP_PREF_SPACE_NAME).isNotEmpty()) {
            binding.includeToolbar.textViewSpace.text =
                appPreferences.getString(Constant.APP_PREF_SPACE_NAME)
        }
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(notificationReceiver)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun manageNotificationRetentionData() {
        var notificationRetentionList = ArrayList<NotificationRetention>()
        if (appPreferences.getString(Constant.APP_PREF_NOTIFICATION_RETENTION).isEmpty()) {
            notificationRetentionList.clear()
            notificationRetentionList.add(NotificationRetention("1 day", 1))
            notificationRetentionList.add(NotificationRetention("1 week", 7))
            notificationRetentionList.add(NotificationRetention("2 weeks", 14, true))
            notificationRetentionList.add(NotificationRetention("1 month", 30))
            appPreferences.putString(
                Constant.APP_PREF_NOTIFICATION_RETENTION,
                Gson().toJson(notificationRetentionList)
            )
        } else {
            val gson = Gson()
            val type = object : TypeToken<List<NotificationRetention>>() {}.type
            notificationRetentionList = gson.fromJson(
                appPreferences.getString(Constant.APP_PREF_NOTIFICATION_RETENTION),
                type
            )
        }

        val data = notificationRetentionList.find { it.isSelected }
        if (data != null) {
            dbHelper.deleteFetchNotificationOlderThanWeek(
                ConvertDateTimeFormat.getDaysBeforeDate(data.value)
            )
        }
        homeAdapter.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    fun filter(text: String?) {
        val temp = ArrayList<HomeNotificationList>()
        for (d in homeList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.data!!.netdata!!.alert!!.name[0].contains(text!!, true) ||
                d.data!!.netdata!!.alert!!.type!!.contains(text, true) ||
                d.data!!.netdata!!.alert!!.component!!.contains(text, true) ||
                d.data!!.netdata!!.chart!!.id!!.contains(text, true)) {
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

    private fun getNotificationCount() = with(binding) {
        val count = dbHelper.getAllDataFromFetchNotification(isSimpleData = true).filter {
            !it.isNotificationRead
//            it.data!!.netdata!!.space!!.id == appPreferences.getString(Constant.APP_PREF_SPACE_ID) && !it.isNotificationRead
        }.size
        if (count != 0) {
            includeToolbar.textViewNotificationCount.visible()
            includeToolbar.textViewNotificationCount.text = count.toString()
        } else {
            includeToolbar.textViewNotificationCount.gone()
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
                if (d.data!!.netdata!!.alert!!.name[0].contains(text, true)) {
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

    private val handler = Handler(Looper.getMainLooper())
    private var currentItem: HomeNotificationList? = null
    private var timeOnCurrentItem: Long = 0

    private fun setAdapter() = with(binding) {
        /*flexLayoutManager = FlexboxLayoutManager(context)
        flexLayoutManager.flexDirection = FlexDirection.ROW*/

        recyclerViewHome.adapter = homeAdapter
//        recyclerViewFilterSelected.layoutManager = flexLayoutManager
        recyclerViewFilterSelected.adapter = filterSelectedAdapter

        recyclerViewHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                updateVisibleItems()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateVisibleItems() {
        try {
            val layoutManager = binding.recyclerViewHome.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && firstVisibleItemPosition != RecyclerView.NO_POSITION) {
                val visibleItem = homeList[firstVisibleItemPosition]
                Log.e("pos", "$firstVisibleItemPosition $lastVisibleItemPosition")
                if (currentItem == null || currentItem != visibleItem) {
                    currentItem = visibleItem
                    timeOnCurrentItem = System.currentTimeMillis()

                    // Update the UI to reflect the current visible item
                    // You can change the text, background, or any other view properties here


                    handler.removeCallbacksAndMessages(null) // Remove any previous callbacks
                    handler.postDelayed({
                        // Check if the user stayed on the current item for 5 seconds
                        if (System.currentTimeMillis() - timeOnCurrentItem >= 8000) {
                            // Change the value of the visible item here
                            for (i in firstVisibleItemPosition..lastVisibleItemPosition) {
                                Log.e("read", homeList[i].isAutoReadStop.toString())
                                if(!homeList[i].isAutoReadStop && !homeList[i].isRead){
                                    val item = homeList[i]
                                    homeList[i].isRead = true
                                    homeList[i].isNotificationRead = true
                                    homeList[i].isTempMessageRead = true
                                    readUnreadNotification(item, isPermanentRead = true)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        homeList[i].isTempMessageRead = false
                                        homeAdapter.notifyDataSetChanged()
                                    }, 4000)
                                }
                            }
                        }
                    }, 8000)
                }
            }
        } catch (_: Exception){

        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun readUnreadNotification(
        item: HomeNotificationList,
        isPermanentRead: Boolean = false,
        isSwipeRead: Boolean = false
    ) {
        dbHelper.updateFetchNotificationData(item, isPermanentRead = isPermanentRead, isSwipeRead = isSwipeRead)
        homeAdapter.list.clear()
        if (isAllButtonSelected) {
            homeAdapter.list.addAll(getAllData())
        } else {
            homeAdapter.list.addAll(getAllData(isUnread = true))
        }
        homeAdapter.notifyDataSetChanged()
        binding.buttonAll.text = "${getString(R.string.btn_all)} (${getAllData().size})"
        binding.buttonUnread.text =
            "${getString(R.string.btn_unread)} (${getAllData(isUnread = true).size})"
        getNotificationCount()
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

        if (item.data!!.netdata!!.alert!!.current!!.status[0].equals(AlertStatus.CRITICAL.type, true)) {
            imageViewNotificationWarning.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorRedFF)
            )
        } else if (item.data!!.netdata!!.alert!!.current!!.status[0].equals(AlertStatus.WARNING.type, true)) {
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

        textViewSpaceWarningPercent.text = AppUtils.convertTwoDecimal(item.data!!.netdata!!.alert!!.current!!.value!!, true)
        textViewSpaceWarningName.text = item.data!!.netdata!!.alert!!.name[0]
        textViewNodeId.text = item.data!!.host[0].name
        textViewDiskSpace.text = item.data!!.netdata!!.chart!!.name
        var roomList = ""
        for(i in item.data!!.netdata!!.room){
            roomList += "${i.name} • "
        }
        textViewWarRoomsList.text = roomList.dropLast(3)
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
            constraintCurrentNodes.performClick()
        }

        radioButtonAllNodes.setOnClickListener {
            constraintAllNodes.performClick()
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

    private fun bottomSheetExistsInWarRooms(item: HomeNotificationList) {

        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.bottom_sheet_existis_war_rooms_list, null)

        val existisWarRoomsAdapter by lazy {
            ExistisWarRoomsAdapter(item.priority!!) { view, position, item ->
                when (view.id) {
                    R.id.constraintMain -> {
//                        binding.textViewLabelAllWarRooms.text = item.name
                        dialog.dismiss()
                    }
                }
            }
        }

        existisWarRoomsAdapter.list.addAll(item.data!!.netdata!!.room)

        val recyclerViewNotificationExists =
            view.findViewById<RecyclerView>(R.id.recyclerViewNotificationExists)
        val imageViewWarRoomsBack =
            view.findViewById<AppCompatImageView>(R.id.imageViewWarRoomsBack)
        recyclerViewNotificationExists.adapter = existisWarRoomsAdapter

        val imageViewWarRoomsWarning =
            view.findViewById<AppCompatImageView>(R.id.imageViewWarRoomsWarning)
        val textViewSpaceWarningPercent =
            view.findViewById<AppCompatTextView>(R.id.textViewSpaceWarningPercent)
        val textViewSpaceWarningText =
            view.findViewById<AppCompatTextView>(R.id.textViewSpaceWarningText)
        val textViewGKE = view.findViewById<AppCompatTextView>(R.id.textViewGKE)
        val textViewDiskSpace = view.findViewById<AppCompatTextView>(R.id.textViewDiskSpace)

        /*radioButtonCurrentNodes.isChecked = isCurrentNodes
        radioButtonAllNodes.isChecked = !isCurrentNodes*/

        if (item.data!!.netdata!!.alert!!.current!!.status[0].equals(AlertStatus.CRITICAL.type, true)) {
            imageViewWarRoomsWarning.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorRedFF)
            )
        } else if (item.data!!.netdata!!.alert!!.current!!.status[0].equals(AlertStatus.WARNING.type, true)) {
            imageViewWarRoomsWarning.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorYellowF9)
            )
        } else {
            imageViewWarRoomsWarning.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
        }

        textViewSpaceWarningPercent.text = AppUtils.convertTwoDecimal(item.data!!.netdata!!.alert!!.current!!.value!!, true)
        textViewSpaceWarningText.text = item.data!!.netdata!!.alert!!.name[0]
        textViewGKE.text = item.data!!.host[0].name
        textViewDiskSpace.text = item.data!!.netdata!!.chart!!.name

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
                        /*sortByTimeItemPosition = getItemPosition(item)
                        sortByNotificationPriorityItemPosition = -1
                        sortByCriticalityItemPosition = -1*/
                        appPreferences.putString(Constant.APP_PREF_SORTING_BY_TIME, getItemPosition(item).toString())
                        appPreferences.putString(Constant.APP_PREF_SORTING_BY_PRIORITY, "-1")
                        appPreferences.putString(Constant.APP_PREF_SORTING_BY_CRITICALITY, "-1")
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
                        /*sortByNotificationPriorityItemPosition = getItemPosition(item)
                        sortByTimeItemPosition = -1
                        sortByCriticalityItemPosition = -1*/
                        appPreferences.putString(Constant.APP_PREF_SORTING_BY_TIME, "-1")
                        appPreferences.putString(Constant.APP_PREF_SORTING_BY_PRIORITY, getItemPosition(item).toString())
                        appPreferences.putString(Constant.APP_PREF_SORTING_BY_CRITICALITY, "-1")
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
                        /*sortByCriticalityItemPosition = getItemPosition(item)
                        sortByTimeItemPosition = -1
                        sortByNotificationPriorityItemPosition = -1*/
                        appPreferences.putString(Constant.APP_PREF_SORTING_BY_TIME, "-1")
                        appPreferences.putString(Constant.APP_PREF_SORTING_BY_PRIORITY, "-1")
                        appPreferences.putString(Constant.APP_PREF_SORTING_BY_CRITICALITY, getItemPosition(item).toString())
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

        if (appPreferences.getString(Constant.APP_PREF_SORTING_BY_TIME).toInt() != -1) {
            sortByTimeAdapter.selectedItemPosition = appPreferences.getString(Constant.APP_PREF_SORTING_BY_TIME).toInt()
        }

        if (appPreferences.getString(Constant.APP_PREF_SORTING_BY_PRIORITY).toInt() != -1) {
            sortByNotificationPriorityAdapter.selectedItemPosition =
                appPreferences.getString(Constant.APP_PREF_SORTING_BY_PRIORITY).toInt()
        }

        if (appPreferences.getString(Constant.APP_PREF_SORTING_BY_CRITICALITY).toInt() != -1) {
            sortByCriticalityAdapter.selectedItemPosition = appPreferences.getString(Constant.APP_PREF_SORTING_BY_CRITICALITY).toInt()
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

    private fun filterCount(){
        var tempNodeList = ArrayList<String>()
        if (filterNodesList.isNotEmpty()) {
            tempNodeList = filterNodesList.filter { it.isSelected }
                .map { it.otherName } as ArrayList<String>
        }

        val tempClassList: ArrayList<String> = getFilterTempList(filterClassificationList)
        val tempStatusList: ArrayList<String> = getFilterTempList(filterStatusList)
        val tempPriorityList: ArrayList<String> = getFilterTempList(filterPriorityList)
        val tempTypeList: ArrayList<String> = getFilterTempList(filterTypeCompList)

         val itemList = dbHelper.getAllDataFromFetchNotification(
                    spaceID = appPreferences.getString(Constant.APP_PREF_SPACE_ID),
                    roomID = roomList[roomsItemPosition].id!!,
                    isFilterBy = true,
                    statusFilters = tempStatusList,
                    priorityFilters = tempPriorityList,
                    nodesFilters = tempNodeList,
                    classFilters = tempClassList,
                    typeFilters = tempTypeList
                )

        for (item in filterNodesList) {
            val matchingDataList = itemList.filter { it.data!!.host[0].id.equals(item.otherName, true)}
            item.count = matchingDataList.size
        }
        for (item in filterStatusList) {
            val matchingDataList = itemList.filter { it.data!!.netdata!!.alert!!.current!!.status[0].equals(item.name, true)}
            item.count = matchingDataList.size
        }
        for (item in filterPriorityList) {
            val matchingDataList = itemList.filter { it.priority.equals(item.name, true)}
            item.count = matchingDataList.size
        }
        for (item in filterClassificationList) {
            val matchingDataList = itemList.filter { it.data!!.netdata!!.alert!!.classes.equals(item.name, true)}
            item.count = matchingDataList.size
        }
        for (item in filterTypeCompList) {
            val matchingDataList = itemList.filter { it.data!!.netdata!!.alert!!.type.equals(item.name, true)
                    || it.data!!.netdata!!.alert!!.component.equals(item.name, true) }
            item.count = matchingDataList.size
        }

        notifyDrawer()
    }

    private fun filterTotalCount() {
        val nodeCount = filterNodesList.filter { it.isSelected }
        val alertStatusCount = filterStatusList.filter { it.isSelected }
        val notificationPriorityCount = filterPriorityList.filter { it.isSelected }
        val classCount = filterClassificationList.filter { it.isSelected }
        val typAndComponentCount = filterTypeCompList.filter { it.isSelected }

        totalFilterCount =
            nodeCount.size + alertStatusCount.size + notificationPriorityCount.size + classCount.size + typAndComponentCount.size
    }

    private fun tempCount() = with(binding) {
        filterTotalCount()

        if (totalFilterCount != 0) {
            buttonApplyFilter.text = "Apply ($totalFilterCount) Filters"
        } else {
            buttonApplyFilter.text = "Apply Filters"
            isFilterBy = false
        }

        isApplyFilter = false
    }

    private fun applyFilterCount() = with(binding) {
        filterTotalCount()
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

    private fun callFetchHomeNotification() {
        showLoader()
        apiViewModel.callFetchHomeNotification()
    }

    private fun observeFetchHomeNotification() {
        apiViewModel.fetchHomeNotificationLiveData.observe(this) {
            hideLoader()
            if (!it.isError || it.responseCode == 200) {
                insertDataIfEmpty(it.data!!)
            }
        }
    }

    private fun callGetHomeNotificationOnNotify() {
        apiViewModel.callFetchHomeNotification()
    }

    private fun observeGetHomeNotificationOnNotify() {
        apiViewModel.fetchHomeNotificationLiveData.observe(this) {
            if (!it.isError || it.responseCode == 200) {
                if(it.data!!.isNotEmpty()){
                    /*val gson = Gson()
                    val type = object : TypeToken<List<HomeNotificationList>>() {}.type
                    val alarmDataList: List<HomeNotificationList> = gson.fromJson(Constant.dummyData, type)*/
                    var lastId: Long = dbHelper.getLastIdFromTable("fetchNotifications")
                    for (item in it.data) {
                        lastId++
                        dbHelper.insertFetchNotificationData(lastId, item)
                    }

                    getNotificationCount()
                }
            }
        }
    }

    private fun insertDataIfEmpty(alertDataList: ArrayList<HomeNotificationList>) {
        if(dbHelper.getAllDataFromFetchNotification(isSimpleData = true).isEmpty()){
//        if(alertDataList.isNotEmpty()){
            val gson = Gson()
            val type = object : TypeToken<List<HomeNotificationList>>() {}.type
            val alarmDataList: List<HomeNotificationList> = gson.fromJson(Constant.dummyData, type)
            var lastId: Long = dbHelper.getLastIdFromTable("fetchNotifications")
            for (item in alarmDataList) {
                lastId++
                dbHelper.insertFetchNotificationData(lastId, item)
            }
        }
        manageTableData()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun manageTableData(){
        homeList.clear()
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

             if(appPreferences.getString(Constant.APP_PREF_SORTING_BY_TIME).toInt() != -1 ||
                 appPreferences.getString(Constant.APP_PREF_SORTING_BY_PRIORITY).toInt() != -1 ||
                 appPreferences.getString(Constant.APP_PREF_SORTING_BY_CRITICALITY).toInt() != -1){
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
                         sortByTimeItemPosition = appPreferences.getString(Constant.APP_PREF_SORTING_BY_TIME).toInt(),
                         sortByNotificationPriorityItemPosition = appPreferences.getString(Constant.APP_PREF_SORTING_BY_PRIORITY).toInt(),
                         sortByCriticalityItemPosition = appPreferences.getString(Constant.APP_PREF_SORTING_BY_CRITICALITY).toInt()
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

        } else if (appPreferences.getString(Constant.APP_PREF_SORTING_BY_TIME).toInt() != -1 ||
             appPreferences.getString(Constant.APP_PREF_SORTING_BY_PRIORITY).toInt() != -1 ||
             appPreferences.getString(Constant.APP_PREF_SORTING_BY_CRITICALITY).toInt() != -1) {
            homeList.addAll(
                dbHelper.getAllDataFromFetchNotification(
                    spaceID = appPreferences.getString(Constant.APP_PREF_SPACE_ID),
                    roomID = roomList[roomsItemPosition].id!!,
                    isSortBy = true,
                    sortByTimeItemPosition = appPreferences.getString(Constant.APP_PREF_SORTING_BY_TIME).toInt(),
                    sortByNotificationPriorityItemPosition = appPreferences.getString(Constant.APP_PREF_SORTING_BY_PRIORITY).toInt(),
                    sortByCriticalityItemPosition = appPreferences.getString(Constant.APP_PREF_SORTING_BY_CRITICALITY).toInt()
                )
            )
//            homeAdapter.list.addAll(homeList)
        } else {
            homeList.addAll(
                dbHelper.getAllDataFromFetchNotification(
                    spaceID = appPreferences.getString(Constant.APP_PREF_SPACE_ID),
                    roomID = roomList[roomsItemPosition].id!!
                )
            )
//            homeAdapter.list.addAll(homeList)
        }

        addFilterListData()
        val itemsToAdd = mutableListOf<HomeNotificationList>() // Change ItemType to the actual type of your items

        itemsToAdd.addAll(homeList.filter { it.data!!.netdata!!.room.any { room -> room.id == roomList[roomsItemPosition].id!!}})
        /*for (i in homeList) {
            var isContain = false
            for (j in i.data!!.netdata!!.room) {
                if (j.id == roomList[roomsItemPosition].id!!) {
                    isContain = true
                }
            }
            if(isContain) itemsToAdd.add(i)
        }*/
//        homeAdapter.list.addAll(homeList.filter { it.data!!.netdata!!.room.any { room -> room.id == roomList[roomsItemPosition].id!!}})
        homeAdapter.list.addAll(itemsToAdd)
        homeList.clear()
        homeList.addAll(homeAdapter.list)
        val unreadItem = homeAdapter.list.filter { !it.isRead }

        binding.buttonAll.text = "${getString(R.string.btn_all)} (${homeAdapter.list.size})"
        binding.buttonUnread.text = "${getString(R.string.btn_unread)} (${unreadItem.size})"

        homeAdapter.notifyDataSetChanged()
        filterCount()
        drawerFilter()
        getNotificationCount()
    }

    private fun getFilterTempList(list1: ArrayList<FilterList>): ArrayList<String> {
        var list2 = ArrayList<String>()
        if (list1.isNotEmpty()) {
            list2 = list1.filter { it.isSelected }
                .map { it.name } as ArrayList<String>
        }
        return list2
    }

    private fun addFilterListData(){
        val tempNodeList = ArrayList<HomeNotificationList.Data.Host>()
        val tempClassList = ArrayList<HomeNotificationList.Data.Netdata.Alert>()
        val tempTypeCompList = ArrayList<HomeNotificationList.Data.Netdata.Alert>()

        if (filterStatusList.isEmpty()) {
            filterStatusList.add(FilterList(AlertStatus.CRITICAL.type, "4", 0))
            filterStatusList.add(FilterList(AlertStatus.WARNING.type, "3", 0))
            filterStatusList.add(FilterList(AlertStatus.CLEAR.type, "", 0))
        }

        if (filterPriorityList.isEmpty()) {
            filterPriorityList.add(
                FilterList(
                    Priority.HIGH_PRIORITY.shortName,
                    "3",
                    0,
                    icon = R.drawable.ic_high_priority,
                    isIcon = true
                )
            )
            filterPriorityList.add(
                FilterList(
                    Priority.MEDIUM_PRIORITY.shortName,
                    "2",
                    0,
                    icon = R.drawable.ic_medium_priority,
                    isIcon = true
                )
            )
            filterPriorityList.add(
                FilterList(
                    Priority.LOW_PRIORITY.shortName,
                    "",
                    0,
                    icon = R.drawable.ic_low_priority,
                    isIcon = true
                )
            )
        }
        if (filterNodesList.isEmpty()) {
            homeList.forEach {
                tempNodeList.add(it.data!!.host[0])
            }
            val tempList = tempNodeList.distinctBy { it.name }
            for (item in tempList) {
                filterNodesList.add(
                    FilterList(
                        item.name!!,
                        otherName = item.id,
                        isSelected = false
                    )
                )
            }
        }
        if (filterClassificationList.isEmpty()) {
            homeList.forEach {
                tempClassList.add(it.data!!.netdata!!.alert!!)
            }
            val tempList = tempClassList.distinctBy { it.classes }.mapNotNull { it.classes }
            for (item in tempList) {
                filterClassificationList.add(FilterList(item, isSelected = false))
            }
        }
        if (filterTypeCompList.isEmpty()) {
            homeList.forEach {
                tempTypeCompList.add(it.data!!.netdata!!.alert!!)
            }
            val tempList = tempTypeCompList.distinctBy { it.type }.mapNotNull { it.type }
            val tempCompList =
                tempTypeCompList.distinctBy { it.component }.mapNotNull { it.component }
            for (item in tempList) {
                filterTypeCompList.add(FilterList(item, isSelected = false))
            }
            for (item in tempCompList) {
                filterTypeCompList.add(FilterList(item, isSelected = false))
            }

        }
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
                var position = 0

                for (index in roomList.indices) {
                    val room = roomList[index]
                    if (room.name?.contains("all node", ignoreCase = true) == true) {
                        position = index
                        break
                    }
                }
                roomsItemPosition = position
                binding.textViewLabelAllWarRooms.text = roomList[position].name
                Handler(Looper.getMainLooper()).postDelayed({
                    callFetchHomeNotification()
                },1000)
            }
        }
    }

    private fun callDynamicLink(link: String) {
        showLoader()
        dynamicViewModel.callDynamicLink(link)
    }

    private fun observeDynamicLink() {
        dynamicViewModel.liveData.observe(this) {
            Handler(Looper.getMainLooper()).postDelayed({
                hideLoader()
                if (Constant.dynamicResponseUrl.contains("app.netdata.cloud/spaces")) {
                    navigator.loadActivity(
                        IsolatedFullActivity::class.java,
                        HomeDetailsFragment::class.java
                    ).addBundle(bundleOf(Constant.BUNDLE_URL to Constant.dynamicResponseUrl))
                        .start()
                } else {
                    if (deeplink.isNotEmpty()) {
                        showMessage("Fail to view alert due to a bad URL")
                    }
                    callLinkDevice()
                }

            },500)

            /*if (it is CookiesHandlerError) {
                if (it.map.isNotEmpty()) {
                    appPreferences.putString(Constant.APP_PREF_COOKIE_SI, it.map["s_i"]!!)
                    appPreferences.putString(Constant.APP_PREF_COOKIE_SV, it.map["s_v_${it.map["s_i"]}"]!!)
                    Constant.COOKIE_SI = it.map["s_i"]!!
                    Constant.COOKIE_SV = it.map["s_v_${it.map["s_i"]}"]!!
                    callLinkDevice()
                    Log.e("cookie", Constant.COOKIE_SI)
                } else {
                    Log.e("else", "cookie")
                }
            } else {
                Log.e("else", "else")
            }*/
        }
    }

    fun showNotificationSnackbar() {
        hideKeyBoard()
        callGetHomeNotificationOnNotify()
        if (view != null) {
            val message = "New Notification Generated in ${Constant.MY_NOTIFICATION_MESSAGE}!"
            val snackbar = Snackbar.make(binding.editTextSearchServices, message, Snackbar.LENGTH_LONG)
            snackbar.duration = 20000
            snackbar.setActionTextColor(Color.BLACK)
            snackbar.setAction("View") {
                if(homeList.size != 0){
                    binding.recyclerViewHome.scrollToPosition(0)
                }
                snackbar.dismiss()
            }
            val snackView = snackbar.view
            val params = snackView.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            params.setMargins(0,70,0,0)
            snackView.layoutParams = params
            val textView =
                snackView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            textView.maxLines = 4

            snackView.background = ResourcesCompat.getDrawable(resources, R.color.colorYellowF9, null)
            /*snackView.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.colorPrimary,
                    null
                )
            )*/
            snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
            snackbar.show()
        }
        else {
            Log.e("view", "view null")
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