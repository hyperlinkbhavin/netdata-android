package com.netdata.app.ui.notification.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.netdata.app.R
import com.netdata.app.data.pojo.response.HomeNotificationList
import com.netdata.app.data.pojo.response.SpaceList
import com.netdata.app.databinding.NotificationFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.HomeActivity
import com.netdata.app.ui.home.adapter.AllSpacesAdapter
import com.netdata.app.ui.notification.adapter.NotificationAdapter
import com.netdata.app.utils.Constant
import com.netdata.app.utils.customapi.ApiViewModel
import com.netdata.app.utils.localdb.DatabaseHelper

class NotificationFragment : BaseFragment<NotificationFragmentBinding>() {

    lateinit var dbHelper: DatabaseHelper

    private var spaceList = ArrayList<SpaceList>()
    private var notificationList = ArrayList<HomeNotificationList>()

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    private var spacesItemPosition = 0

    private val notificationsAdapter by lazy {
        NotificationAdapter() { view, position, item ->
            when (view.id) {
                R.id.constraintTop -> {
//                    dbHelper.updateFetchNotificationData(item, isNotificationRead = true)
                    appPreferences.putString(Constant.APP_PREF_SPACE_NAME, item.data!!.netdata!!.space!!.name!!)
                    appPreferences.putString(Constant.APP_PREF_SPACE_ID, item.data!!.netdata!!.space!!.id!!)
                    appPreferences.putBoolean(Constant.APP_PREF_FROM_NOTIFICATION, true)
                    navigator.loadActivity(HomeActivity::class.java).byFinishingAll().start()
                }

                R.id.constraintNotificationRight -> {
                    removeData(position)
                }
            }
        }
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeGetSpaceList()
        observeFetchHomeNotification()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): NotificationFragmentBinding {
        return NotificationFragmentBinding.inflate(inflater, container, attachToRoot)
    }

    override fun bindData() {
        dbHelper = DatabaseHelper(requireContext())
        toolbar()
        manageClick()
        setAdapter()
    }

    override fun onResume() {
        super.onResume()
        callGetSpaceList()
    }

    private fun toolbar() = with(binding) {
        includeToolbar.imageViewBack.setOnClickListener { navigator.loadActivity(HomeActivity::class.java).byFinishingAll().start() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_new_notifications)
    }

    private fun manageClick() = with(binding) {
        constraintAllWarRooms.setOnClickListener {
            bottomSheetAllWarRooms()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding) {
        recyclerViewNotification.adapter = notificationsAdapter
        notificationsAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeData(position: Int){
        notificationsAdapter.list.removeAt(position)
        notificationsAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bottomSheetAllWarRooms() {

        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.bottom_sheet_select_war_rooms, null)

        val allSpacesAdapter by lazy {
            AllSpacesAdapter() { view, position, item ->
                when (view.id) {
                    R.id.constraintMain -> {
                        spacesItemPosition = position
                        binding.textViewLabelAllWarRooms.text = item.name
                        countSpaceList()
                        dialog.dismiss()
                    }
                }
            }
        }

        allSpacesAdapter.list.addAll(spaceList)

        val recyclerViewSelectWarRooms =
            view.findViewById<RecyclerView>(R.id.recyclerViewSelectWarRooms)
        val textViewLabelClose = view.findViewById<AppCompatTextView>(R.id.textViewLabelClose)
        recyclerViewSelectWarRooms.adapter = allSpacesAdapter

        if (spacesItemPosition != -1) {
            allSpacesAdapter.selectionPosition = spacesItemPosition
        }

        allSpacesAdapter.notifyDataSetChanged()

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

    private fun callGetSpaceList() {
        showLoader()
        apiViewModel.callGetSpaceList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeGetSpaceList() {
        apiViewModel.spaceListLiveData.observe(this) {
            hideLoader()
            if (it.responseCode == 200) {
                if (it.data!!.isNotEmpty()) {
                    /*it.data.forEach {item ->
                        dbHelper.insertSpaceData(item)
                    }*/
                    spaceList.clear()
                    val space = SpaceList()
                    space.id = "1"
                    space.name = "All Spaces"
                    spaceList.add(space)
                    spaceList.addAll(it.data)
                    if (appPreferences.getString(Constant.APP_PREF_SPACE_LIST_MAINTAIN).isEmpty()) {
                        appPreferences.putString(
                            Constant.APP_PREF_SPACE_LIST_MAINTAIN,
                            Gson().toJson(spaceList)
                        )
                    }
                    callFetchHomeNotification()
                }
            } else {
                showMessage("Something wrong! Try again")
            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun callFetchHomeNotification() {
        showLoader()
        apiViewModel.callFetchHomeNotification()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeFetchHomeNotification() {
        apiViewModel.fetchHomeNotificationLiveData.observe(this) {
            hideLoader()
            if (!it.isError || it.responseCode == 200) {
                insertDataIfEmpty(it.data!!)
            }
        }
    }

    private fun insertDataIfEmpty(alertDataList: ArrayList<HomeNotificationList>) {
//        if(dbHelper.getAllDataFromFetchNotification(isSimpleData = true).isEmpty()){
        if (alertDataList.isNotEmpty()) {
            /*val gson = Gson()
            val type = object : TypeToken<List<HomeNotificationList>>() {}.type
            val alarmDataList: List<HomeNotificationList> = gson.fromJson(Constant.dummyData, type)*/
            var lastId: Long = dbHelper.getLastIdFromTable("fetchNotifications")
            for (item in alertDataList) {
                lastId++
                dbHelper.insertFetchNotificationData(lastId, item)
            }
        }
        notificationList.addAll(dbHelper.getAllDataFromFetchNotification(isSimpleData = true))
        countSpaceList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun countSpaceList() {
        val matchingDataList = if (spacesItemPosition != 0) {
            notificationList.filter { it.data!!.netdata!!.space!!.id == spaceList[spacesItemPosition].id && !it.isNotificationRead } as ArrayList<HomeNotificationList>
        } else {
            notificationList.filter { !it.isNotificationRead } as ArrayList<HomeNotificationList>
        }

        notificationsAdapter.list.clear()
        notificationsAdapter.list.addAll(matchingDataList)
        notificationsAdapter.notifyDataSetChanged()
    }

    override fun onBackActionPerform(): Boolean {
        navigator.loadActivity(HomeActivity::class.java).byFinishingAll().start()
        return super.onBackActionPerform()
    }

    /*private fun callRoomList() {
        showLoader()
        apiViewModel.callGetRoomsList(appPreferences.getString(Constant.APP_PREF_SPACE_ID))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRoomList() {
        apiViewModel.roomListLiveData.observe(this) {
            hideLoader()
            if (!it.isError && it.responseCode == 200) {
                roomList.addAll(it.data!!)
                binding.textViewLabelAllWarRooms.text = roomList[0].name
            }
        }
    }*/
}