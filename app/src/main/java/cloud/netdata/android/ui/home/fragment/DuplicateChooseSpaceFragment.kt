package cloud.netdata.android.ui.home.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.enumclass.Priority
import cloud.netdata.android.data.pojo.response.HomeNotificationList
import cloud.netdata.android.data.pojo.response.NotificationPriorityList
import cloud.netdata.android.data.pojo.response.NotificationRetention
import cloud.netdata.android.data.pojo.response.SpaceList
import cloud.netdata.android.databinding.ChooseSpaceFragmentBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.auth.AuthActivity
import cloud.netdata.android.ui.auth.IsolatedFullActivity
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.ui.home.HomeActivity
import cloud.netdata.android.ui.home.adapter.ChooseSpaceAdapter
import cloud.netdata.android.ui.settings.fragment.SettingsFragment
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.ConvertDateTimeFormat
import cloud.netdata.android.utils.customapi.ApiViewModel
import cloud.netdata.android.utils.localdb.DatabaseHelper
import cloud.netdata.android.utils.visible
import com.fondesa.kpermissions.extension.onAccepted
import com.fondesa.kpermissions.extension.onDenied
import com.fondesa.kpermissions.extension.onPermanentlyDenied
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Locale.filter

class DuplicateChooseSpaceFragment: BaseFragment<ChooseSpaceFragmentBinding>() {

    lateinit var dbHelper: DatabaseHelper

    private var spaceList = ArrayList<SpaceList>()
    private var notificationList = ArrayList<HomeNotificationList>()
    private var spaceListItemPosition = 0

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    private val chooseSpaceAdapter by lazy {
        ChooseSpaceAdapter(){ view, position, item ->
            when(view.id){
                R.id.constraintTop -> {
                    if (item.plan.equals(Constant.EARLY_BIRD, true) || item.plan.equals(Constant.COMMUNITY, true)) {
                    showSnackBar("This Space is not on a paid plan and cannot receive notifications on the Mobile App. Please upgrade.", binding.textViewLabelChooseSpace)
                    }
                    else {
                        appPreferences.putString(Constant.APP_PREF_SPACE_ID, item.id!!)
                        appPreferences.putString(Constant.APP_PREF_SPACE_NAME, item.name!!)
                        /*if(item.silenceRuleIdList.isNotEmpty()){
                            appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, true)
                        } else {
                            appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, false)
                        }*/
                        navigator.loadActivity(HomeActivity::class.java).
                            addBundle(bundleOf(Constant.BUNDLE_SPACE_ID to item.id,
                            Constant.BUNDLE_SPACE_NAME to item.name)).byFinishingAll().start()
                    }
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
        observeGetSilencingRules()
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ChooseSpaceFragmentBinding {
        return ChooseSpaceFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        getPermission()
        dbHelper = DatabaseHelper(requireContext())
        toolbar()
        manageClick()
        setAdapter()
        editTextChanged()

        try {
            if(dbHelper.getAllDataFromNotificationPriority().isEmpty()){
                dbHelper.insertNotificationPriorityData(
                    NotificationPriorityList(
                        1,
                        Priority.HIGH_PRIORITY.shortName,
                        1,
                        "",
                        "",
                        1,
                        1
                    )
                )
                dbHelper.insertNotificationPriorityData(
                    NotificationPriorityList(
                        2,
                        Priority.MEDIUM_PRIORITY.shortName,
                        0,
                        "",
                        "",
                        0,
                        0
                    )
                )
                dbHelper.insertNotificationPriorityData(
                    NotificationPriorityList(
                        3,
                        Priority.LOW_PRIORITY.shortName,
                        0,
                        "",
                        "",
                        0,
                        0
                    )
                )
            }
        } catch (e: Exception){}
    }

    override fun onResume() {
        super.onResume()
        manageNotificationRetentionData()
        callGetSpaceList()
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewNetdata.visible()
        includeToolbar.imageViewSetting.visible()
    }

    private fun manageClick() = with(binding){
        includeToolbar.imageViewSetting.setOnClickListener {
            navigator.loadActivity(IsolatedFullActivity::class.java, SettingsFragment::class.java).start()
        }
        swipeRefresh.setOnRefreshListener {
            callGetSpaceList()
            swipeRefresh.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding){
        recyclerViewChooseSpace.adapter = chooseSpaceAdapter
        chooseSpaceAdapter.notifyDataSetChanged()
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

    fun filter(text: String?) {
        val temp = ArrayList<SpaceList>()
        for (d in spaceList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.name!!.contains(text!!,true)) {
                temp.add(d)
            }
        }
        //update recyclerview
        chooseSpaceAdapter.updateList(temp)
    }

    /*private fun addData(){
        chooseSpaceAdapter.list.add(ChooseSpaceList("Space 1","1"))
        chooseSpaceAdapter.list.add(ChooseSpaceList("Space 3","3"))
        chooseSpaceAdapter.list.add(ChooseSpaceList("Space 2",""))
        chooseSpaceAdapter.list.add(ChooseSpaceList("Space 4",""))
    }*/

    private fun spannableString(count: String) {
        val spanString =
            SpannableString("You have a total of $count unread notifications in your spaces")
        val termsAndCondition: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        spanString.setSpan(termsAndCondition, 20, (20+count.length), 0)
        spanString.setSpan(ForegroundColorSpan(Color.parseColor("#00AB44")), 20, (20+count.length), 0)
        spanString.setSpan(StyleSpan(Typeface.BOLD), 20, (20+count.length), 0)

        binding.textViewChooseSpaceText.movementMethod =
            LinkMovementMethod.getInstance()
        binding.textViewChooseSpaceText.setText(
            spanString,
            TextView.BufferType.SPANNABLE
        )
    }

    private fun callGetSpaceList(){
        showLoader()
        Log.e("spacecall", "spacecall")
        apiViewModel.callGetSpaceList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeGetSpaceList(){
        apiViewModel.spaceListLiveData.observe(this){ list ->
            hideLoader()
            if(list.responseCode == 200){
                if(list.data!!.isNotEmpty()){
                    /*it.data.forEach {item ->
                        dbHelper.insertSpaceData(item)
                    }*/
                    spaceList.clear()
                    chooseSpaceAdapter.list.clear()
                    val tempSpaceList = list.data.sortedWith(
                        compareBy(
                            // Custom order based on plan type
                            { it.plan in listOf(Constant.COMMUNITY, Constant.EARLY_BIRD) }, // Plans other than "community" and "free" come first
                            { it.plan != Constant.EARLY_BIRD }, // "community" plans come before "free" plans
                            { it.plan } // Sort alphabetically within the same plan type
                        )
                    )
                    spaceList.addAll(tempSpaceList)
                    if(dbHelper.getMaintenanceMode().isEmpty()){
                        dbHelper.insertMaintenanceMode(Gson().toJson(spaceList))
                    }
                    chooseSpaceAdapter.list.addAll(tempSpaceList)
                    chooseSpaceAdapter.notifyDataSetChanged()
                    if(spaceList[spaceListItemPosition].plan != Constant.COMMUNITY
                        && spaceList[spaceListItemPosition].plan != Constant.EARLY_BIRD){
                        callGetSilencingRules(spaceList[spaceListItemPosition].id!!)
                    }
                    callFetchHomeNotification()
                    Handler(Looper.getMainLooper()).postDelayed({
                        if(!appPreferences.getBoolean(Constant.APP_PREF_IS_FIRST_LOGIN)){
                            showMessage("You will only receive new notifications from the alerts")
                            appPreferences.putBoolean(Constant.APP_PREF_IS_FIRST_LOGIN, true)
                        }
                    },1000)
                }
            } else {
                showToast("Session expired! Please login again")
                appPreferences.putBoolean(Constant.APP_PREF_IS_LOGIN, false)
                appPreferences.putString(Constant.APP_PREF_SPACE_NAME, "")
                navigator.loadActivity(AuthActivity::class.java).byFinishingAll().start()
            }
        }
    }

    private fun callGetSilencingRules(spaceId: String) {
        apiViewModel.callGetSilencingRules(spaceId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeGetSilencingRules() {
        apiViewModel.getSilencingRulesLiveData.observe(this) {
            hideLoader()
            if (it.responseCode == 200) {
                if(!it.data.isNullOrEmpty()){
                    val silencingRuleIdList = ArrayList<String>()
                    try {
                        it.data.forEach { silenceRule ->
                            if(silenceRule.state.equals("ACTIVE", true)
                                && silenceRule.integrationIds[0].equals("607bfd3c-02c1-4da2-b67a-0d01b518ce5d", true)){
                                silencingRuleIdList.add(silenceRule.id!!)
                            }
                        }
                    } catch (e: Exception){}

                    spaceList[spaceListItemPosition].silenceRuleIdList.addAll(silencingRuleIdList)
                    spaceListItemPosition++
                    if(spaceListItemPosition != spaceList.size - 1){
                        callGetSilencingRules(spaceList[spaceListItemPosition].id!!)
                    }
                }
                chooseSpaceAdapter.notifyDataSetChanged()
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
        notificationList.clear()
//        if(dbHelper.getAllDataFromFetchNotification(isSimpleData = true).isEmpty()){
        if(alertDataList.isNotEmpty()){
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
        Log.e("data", dbHelper.getAllDataFromFetchNotification(isSimpleData = true).toString())
        countSpaceList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun countSpaceList(){
        var totalCount = 0
        for (space in spaceList) {
//            val matchingDataList = notificationList.filter { it.data!!.netdata!!.space!!.id == space.id && !it.isSpaceRead }
            val matchingDataList = notificationList.filter { it.data!!.netdata!!.space!!.id == space.id && !it.isRead }
            space.count = matchingDataList.size
            totalCount += matchingDataList.size
        }

        spannableString(totalCount.toString())
        chooseSpaceAdapter.list.clear()
        chooseSpaceAdapter.list.addAll(spaceList)
        chooseSpaceAdapter.notifyDataSetChanged()
    }

    private fun getPermission() {
//        hideLoader()
        val activityPermission by lazy {
            permissionsBuilder(
                Manifest.permission.POST_NOTIFICATIONS
            ).build()
        }
        activityPermission
            .onAccepted {

            }
            .onDenied {
                showMessage("You need to grant the notification permission to receive notifications from this app.")
                Handler(Looper.getMainLooper()).postDelayed({
                    getPermission()
                },3000)
            }
            .onPermanentlyDenied {

            }
            .send()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun manageNotificationRetentionData() {
        try {
            var notificationRetentionList = java.util.ArrayList<NotificationRetention>()
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
        } catch (e: Exception) {
        }
    }
}