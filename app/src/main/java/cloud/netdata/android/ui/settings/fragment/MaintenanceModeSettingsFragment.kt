package cloud.netdata.android.ui.settings.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.request.APIRequest
import cloud.netdata.android.data.pojo.response.SpaceList
import cloud.netdata.android.databinding.MaintenanceModeSettingsFragmentBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.ui.settings.adapter.MaintenanceModeSettingsAdapter
import cloud.netdata.android.utils.*
import cloud.netdata.android.utils.customapi.ApiViewModel
import cloud.netdata.android.utils.localdb.DatabaseHelper
import com.google.android.datatransport.cct.internal.LogEvent
import java.util.*
import kotlin.collections.ArrayList

class MaintenanceModeSettingsFragment: BaseFragment<MaintenanceModeSettingsFragmentBinding>() {

    lateinit var dbHelper: DatabaseHelper
    private var spaceList = ArrayList<SpaceList>()
    private var spaceListItemPosition = 0
    private var itemPosition = -1
    private var clickPosition = -1
    private var isChanged = false
    private var manageSpaceList = ArrayList<SpaceList>()
    private var isAllSilence = false
    private var isAllUnsilence = false
    private var isUntil = false
    private var untilDate = ""
    private var allChangeIndex = -1

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    private val maintenanceModeSettingsAdapter by lazy {
        MaintenanceModeSettingsAdapter(spaceList) { view, position, item, isChecked ->
            when (view.id) {
                R.id.switchDisableAllNotifications -> {
                    itemPosition = position
                    clickPosition = 1
                    isChanged = false
                    if (!isChecked) {
                        val ruleId = ArrayList<String>()
                        ruleId.addAll(item.silenceRuleIdList)
                        spaceList[itemPosition].isSelected = false
                        spaceList[itemPosition].isForever = false
                        spaceList[itemPosition].isUntil = false
                        spaceList[itemPosition].untilDate = ""
                        callUnsilenceSpace(item, ruleId)
                    }

                }
                R.id.textViewUntilDate -> {
                    changeUntil(item, position)
                }
                R.id.radioButtonUntil -> {
                    changeUntil(item, position)
                }
                R.id.radioButtonForever -> {
                    changeForever(item, position)
                }
            }
        }
    }

    private fun changeUntil(item: SpaceList, position: Int){
        if(!item.isUntil){
            itemPosition = position
            clickPosition = 2
            spaceList[itemPosition].isSelected = true
            spaceList[itemPosition].isUntil = true
            if(item.isForever){
                isChanged = true
                spaceList[itemPosition].isForever = false
                val ruleId = ArrayList<String>()
                ruleId.addAll(spaceList[itemPosition].silenceRuleIdList)
                callUnsilenceSpace(spaceList[itemPosition], ruleId)
            } else {
                isChanged = false
                callSilenceSpace(spaceList[itemPosition])
            }
        }
    }

    private fun changeForever(item: SpaceList, position: Int){
        if(!item.isForever){
            itemPosition = position
            clickPosition = 3
            spaceList[itemPosition].isForever = true
            spaceList[itemPosition].isSelected = true
            if(item.isUntil){
                isChanged = true
                spaceList[itemPosition].isUntil = false
                spaceList[itemPosition].untilDate = ""
                val ruleId = ArrayList<String>()
                ruleId.addAll(spaceList[itemPosition].silenceRuleIdList)
                callUnsilenceSpace(spaceList[itemPosition], ruleId)
            } else {
                isChanged = false
                callSilenceSpace(spaceList[itemPosition])
            }
        }
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeGetSpaceList()
        observeSilenceSpace()
        observeUnsilenceSpace()
        observeGetSilencingRules()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): MaintenanceModeSettingsFragmentBinding {
        return MaintenanceModeSettingsFragmentBinding.inflate(inflater, container, attachToRoot)
    }

    override fun bindData() {
        dbHelper = DatabaseHelper(requireContext())
        toolbar()
        manageClick()
        setAdapter()
        editTextChanged()
    }

    override fun onResume() {
        super.onResume()
        callGetSpaceList()
    }

    private fun toolbar() = with(binding) {
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text =
            getString(R.string.title_maintenance_mode)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun manageClick() = with(binding) {
        switchDisableAllNotifications.setOnClickListener {
            if(switchDisableAllNotifications.isChecked){
                binding.constraintDisableNotifications.isSelected = true
                binding.radioGroupAllNotifications.visible()
            } else {
                unsilenceAllNotification()
            }
        }

        textViewUntilDate.setOnClickListener {
            datePicker()
        }

        radioButtonForever.setOnClickListener {
            if (radioButtonForever.isChecked) {
                textViewUntilDate.text = "DD/MM/YY, HH:MM"
            }
            isUntil = false
            /*spaceList.filter {
                !it.plan.equals(Constant.COMMUNITY, true)
                        && !it.plan.equals(Constant.EARLY_BIRD, true)
            }.forEach {
                it.isForever = true
                it.isUntil = false
                it.untilDate = ""
            }*/

            changeAllNotificationData(true)
        }

        radioButtonUntil.setOnClickListener {
            datePicker()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    private fun changeAllNotificationData(isUnsilence: Boolean = false) {
        appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, true)
        if(isUnsilence){
            allChangeIndex += 1
            if(allChangeIndex != spaceList.size){
                isAllSilence = true
                itemPosition = allChangeIndex

                if(!spaceList[allChangeIndex].plan.equals(Constant.COMMUNITY, true)
                    && !spaceList[allChangeIndex].plan.equals(Constant.EARLY_BIRD, true)){
                    itemPosition = allChangeIndex
                    clickPosition = 1
                    Log.e("if item", spaceList[allChangeIndex].toString())
                    callUnsilenceSpace(spaceList[allChangeIndex], spaceList[allChangeIndex].silenceRuleIdList)
                } else {
                    isAllSilence = false
                    allChangeIndex = -1
                    Log.e("else item", spaceList[1].toString())
                    changeAllNotificationData(false)
                }
            } else {
                isAllSilence = false
                allChangeIndex = -1
                Log.e("else item", spaceList[1].toString())
                changeAllNotificationData(false)
            }
        } else {
            allChangeIndex += 1
            if(allChangeIndex != spaceList.size){
                isAllSilence = true
                itemPosition = allChangeIndex

                if(!spaceList[allChangeIndex].plan.equals(Constant.COMMUNITY, true)
                    && !spaceList[allChangeIndex].plan.equals(Constant.EARLY_BIRD, true)){
                    itemPosition = allChangeIndex
                    clickPosition = 3
                    callSilenceSpace(spaceList[allChangeIndex])
                } else {
                    isAllSilence = false
                    allChangeIndex = -1
                    Log.e("else item", spaceList[1].toString())
                    getCheckData()
                }
            } else {
                isAllSilence = false
                allChangeIndex = -1
                getCheckData()
            }
        }

        maintenanceModeSettingsAdapter.notifyDataSetChanged()
    }

    private fun unsilenceAllNotification(){
        appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, false)
        allChangeIndex += 1
        if(allChangeIndex != spaceList.size){
            isAllUnsilence = true
            itemPosition = allChangeIndex

            if(!spaceList[allChangeIndex].plan.equals(Constant.COMMUNITY, true)
                && !spaceList[allChangeIndex].plan.equals(Constant.EARLY_BIRD, true)){
                itemPosition = allChangeIndex
                clickPosition = 1
                callUnsilenceSpace(spaceList[allChangeIndex], spaceList[allChangeIndex].silenceRuleIdList)
            }
        } else {
            isAllUnsilence = false
            allChangeIndex = -1
            getCheckData()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    private fun changeAllNotificationUntilData(isUnsilence: Boolean = false) {
            appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, true)
        if(isUnsilence){
            allChangeIndex += 1
            if(allChangeIndex != spaceList.size){
                isAllSilence = true
                itemPosition = allChangeIndex

                if(!spaceList[allChangeIndex].plan.equals(Constant.COMMUNITY, true)
                    && !spaceList[allChangeIndex].plan.equals(Constant.EARLY_BIRD, true)){
                    itemPosition = allChangeIndex
                    clickPosition = -1
                    Log.e("if item", spaceList[allChangeIndex].toString())
                    callUnsilenceSpace(spaceList[allChangeIndex], spaceList[allChangeIndex].silenceRuleIdList)
                } else {
                    isAllSilence = false
                    allChangeIndex = -1
                    Log.e("else item", spaceList[1].toString())
                    changeAllNotificationUntilData(false)
                }
            } else {
                isAllSilence = false
                allChangeIndex = -1
                Log.e("else item", spaceList[1].toString())
                changeAllNotificationUntilData(false)
            }
        } else {
            allChangeIndex += 1
            if(allChangeIndex != spaceList.size){
                isAllSilence = true
                itemPosition = allChangeIndex

                if(!spaceList[allChangeIndex].plan.equals(Constant.COMMUNITY, true)
                    && !spaceList[allChangeIndex].plan.equals(Constant.EARLY_BIRD, true)){
                    itemPosition = allChangeIndex
                    clickPosition = 4
                    callSilenceSpace(spaceList[allChangeIndex])
                } else {
                    isAllSilence = false
                    allChangeIndex = -1
                    Log.e("else item", spaceList[1].toString())
                    getCheckData()
                }
            } else {
                isAllSilence = false
                allChangeIndex = -1
                getCheckData()
            }
        }
        maintenanceModeSettingsAdapter.notifyDataSetChanged()
    }

    private fun changeDataForUnsilence(){
        spaceList[itemPosition].silenceRuleId = ""
        spaceList[itemPosition].silenceRuleName = ""
        spaceList[itemPosition].silenceRuleIdList = arrayListOf()
        updateData(clickPosition)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding) {
        recyclerViewMaintenanceMode.adapter = maintenanceModeSettingsAdapter
        maintenanceModeSettingsAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateData(clickPos: Int) {

        try{
            when (clickPos) {
                1 -> {
                    spaceList[itemPosition].isSelected = false
                    spaceList[itemPosition].isForever = false
                    spaceList[itemPosition].isUntil = false
                    spaceList[itemPosition].untilDate = ""
                }
                2 -> {
                    spaceList[itemPosition].isSelected = true
                    spaceList[itemPosition].isForever = false
                    spaceList[itemPosition].isUntil = true
                }
                3 -> {
                    spaceList[itemPosition].isSelected = true
                    spaceList[itemPosition].isForever = true
                    spaceList[itemPosition].isUntil = false
                    spaceList[itemPosition].untilDate = ""
                }
                4 -> {
                    spaceList[itemPosition].isSelected = true
                    spaceList[itemPosition].isForever = false
                    spaceList[itemPosition].isUntil = true
                }
            }

            getCheckData()

            maintenanceModeSettingsAdapter.notifyDataSetChanged()
        }
        catch (_: Exception){
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getCheckData() = with(binding) {
        val data = spaceList.find { !it.isSelected && !it.plan.equals(Constant.COMMUNITY, true)
                && !it.plan.equals(Constant.EARLY_BIRD, true) }
        if (data?.name.isNullOrEmpty()) {
            switchDisableAllNotifications.isChecked = true
            radioGroupAllNotifications.visible()
            val newData = spaceList.find { it.isSelected && !it.plan.equals(Constant.COMMUNITY, true)
                    && !it.plan.equals(Constant.EARLY_BIRD, true) }
            if(newData!!.isUntil){
                radioButtonUntil.isChecked = true
                textViewUntilDate.text = newData.untilDate
            } else {
                radioButtonForever.isChecked = true
            }
        } else {
            switchDisableAllNotifications.isChecked = false
            textViewUntilDate.text = "DD/MM/YY, HH:MM"
            radioGroupAllNotifications.gone()
        }
        val sData = spaceList.find { it.id == appPreferences.getString(Constant.APP_PREF_SPACE_ID)
                && (it.isUntil || it.isForever)}
        if(sData != null){
            appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, true)
        } else {
            appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, false)
        }
        maintenanceModeSettingsAdapter.notifyDataSetChanged()
    }
    fun datePicker()=with(binding){
        val mCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, date ->
                val dates = if(date < 10){
                    "0${date}"
                } else {
                    (date).toString()
                }

                var months = ""
                months = if(month < 9){
                    "0${month+1}"
                } else {
                    (month+1).toString()
                }
                timePicker(dates + "/" + months + "/" + year.toString().drop(2))
            },
            mCalendar.get(Calendar.YEAR),
            mCalendar.get(Calendar.MONTH),
            mCalendar.get(Calendar.DATE)
        )
        datePickerDialog.datePicker.minDate = mCalendar.time.time
        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    fun timePicker(date: String) =with(binding){
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker = TimePickerDialog( requireContext(),
            { timePicker, selectedHour, selectedMinute ->
                val minutes = if (selectedMinute < 10) {
                    "0$selectedMinute"
                } else {
                    "$selectedMinute"
                }

                isUntil = true
                spaceList.filter {
                    !it.plan.equals(Constant.COMMUNITY, true)
                            && !it.plan.equals(Constant.EARLY_BIRD, true)
                }.forEach {
                    it.isSelected = true
                    it.isForever = false
                    it.isUntil = true
                    it.untilDate = "$date, $selectedHour:$minutes"
                    Log.e("ittt", it.toString())
                }

                radioButtonUntil.isChecked = true
                textViewUntilDate.text = "$date, $selectedHour:$minutes"
                maintenanceModeSettingsAdapter.notifyDataSetChanged()
                changeAllNotificationUntilData(true)
            },
            hour,
            minute,
            false
        ) //Yes 24 hour time

        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
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
            if (d.name!!.contains(text!!, true)) {
                temp.add(d)
            }
        }
        //update recyclerview
        maintenanceModeSettingsAdapter.updateList(temp)
    }

    private fun callGetSpaceList() {
        showLoader()
        apiViewModel.callGetSpaceList()
    }

    private fun observeGetSpaceList() {
        apiViewModel.spaceListLiveData.observe(this) {
            hideLoader()
            if (it.responseCode == 200) {
                if (it.data!!.isNotEmpty()) {
                    checkAndStoreData(it.data)
                }
            } else {
                showMessage("Something wrong")
            }
        }
    }

    private fun callGetSilencingRules(spaceId: String) {
        showLoader()
        apiViewModel.callGetSilencingRules(spaceId)
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    private fun observeGetSilencingRules() {
        apiViewModel.getSilencingRulesLiveData.observe(this) {
            hideLoader()
            if (it.responseCode == 200) {
                if(!it.data.isNullOrEmpty()) {
                    it.data.forEach { silenceRule ->
                        if (silenceRule.state.equals("ACTIVE", true)
                            && silenceRule.integrationIds[0].equals(
                                "607bfd3c-02c1-4da2-b67a-0d01b518ce5d",
                                true
                            )
                        ) {
                            if(!spaceList[spaceListItemPosition].silenceRuleIdList.contains(silenceRule.id)
                                && !silenceRule.id.isNullOrEmpty()){
                                spaceList[spaceListItemPosition].silenceRuleIdList.add(silenceRule.id!!)
                                spaceList[spaceListItemPosition].isSelected = true
                                if(silenceRule.lastsUntil != null){
                                    spaceList[spaceListItemPosition].untilDate = ConvertDateTimeFormat.convertUTCToLocalDate(silenceRule.lastsUntil!!,
                                        DateTimeFormats.SERVER_DATE_TIME_FORMAT_NEW_ONE, DateTimeFormats.MAINTENANCE_MODE_DATE_FORMAT)
                                    spaceList[spaceListItemPosition].isUntil = true
                                } else {
                                    spaceList[spaceListItemPosition].isForever = true
                                }
                            }
                        }
                    }


                }
                spaceList[spaceListItemPosition].isSelected = !spaceList[spaceListItemPosition].silenceRuleIdList.isNullOrEmpty()
                    spaceListItemPosition++
                    if (spaceListItemPosition != spaceList.size - 1
                        && !spaceList[spaceListItemPosition].plan.equals(Constant.COMMUNITY, true)
                        && !spaceList[spaceListItemPosition].plan.equals(Constant.EARLY_BIRD, true)
                    ) {
                        callGetSilencingRules(spaceList[spaceListItemPosition].id!!)
                    } else {
                        getCheckData()
                    }
                maintenanceModeSettingsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun callSilenceSpace(item: SpaceList) {
        showLoader()
        if (item.isUntil) {
            apiViewModel.callSilenceSpace(
                item.id!!, APIRequest(
                    name = "Netdata-MobileApp-${item.id}",
                    accountId = session.user!!.id,
                    integrationIds = arrayListOf("607bfd3c-02c1-4da2-b67a-0d01b518ce5d"),
                    startsAt = DateTimeFormatter.currentUTCTime,
                    lastsUntil = ConvertDateTimeFormat.convertLocalToUtcDate(
                        item.untilDate!!,
                        DateTimeFormats.MAINTENANCE_MODE_DATE_FORMAT,
                        DateTimeFormats.SERVER_DATE_TIME_FORMAT_NEW
                    )
                )
            )
        } else {
            apiViewModel.callSilenceSpace(
                item.id!!, APIRequest(
                    name = "Netdata-MobileApp-${item.id}",
                    accountId = session.user!!.id,
                    integrationIds = arrayListOf("607bfd3c-02c1-4da2-b67a-0d01b518ce5d"),
                    startsAt = DateTimeFormatter.currentUTCTime
                )
            )
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeSilenceSpace() {
        apiViewModel.silenceSpaceLiveData.observe(this) {
            hideLoader()
            if (it.responseCode == 200) {
                spaceList[itemPosition].silenceRuleId = it.data!!.id
                spaceList[itemPosition].silenceRuleIdList.add(it.data.id!!)
                spaceList[itemPosition].silenceRuleName = it.data.name
                updateData(clickPosition)
                if(isAllSilence){
                    if(isUntil){
                        changeAllNotificationUntilData(false)
                    } else {
                        changeAllNotificationData(false)
                    }
                }
                Log.e("item", spaceList[itemPosition].toString())
            } else {
                if(isAllSilence){
                    if(isUntil){
                        changeAllNotificationUntilData(false)
                    } else {
                        changeAllNotificationData(false)
                    }
                }
            }
        }
    }

    private fun callUnsilenceSpace(item: SpaceList, ruleId: ArrayList<String>) {
        showLoader()
        Log.e("rule", ruleId.toString())
        val ruleIdArrayList = ArrayList(ruleId.filter { it.isNotEmpty() }.distinct())
        apiViewModel.callUnsilenceSpace(item.id!!, ruleIdArrayList)
        changeDataForUnsilence()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeUnsilenceSpace() {
        apiViewModel.unsilenceSpaceLiveData.observe(this) {
            hideLoader()
            if(it.isError){
                spaceList[itemPosition].silenceRuleIdList = ArrayList()
                if(isChanged){
                    callSilenceSpace(spaceList[itemPosition])
                }
                if(isAllUnsilence){
                    unsilenceAllNotification()
                }
                if(isAllSilence){
                    if(isUntil){
                        changeAllNotificationUntilData(true)
                    } else {
                        changeAllNotificationData(true)
                    }
                }
            } else {
                if(isChanged){
                    callSilenceSpace(spaceList[itemPosition])
                }
                if(isAllUnsilence){
                    unsilenceAllNotification()
                }
                if(isAllSilence){
                    if(isUntil){
                        changeAllNotificationUntilData(true)
                    } else {
                        changeAllNotificationData(true)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkAndStoreData(item: ArrayList<SpaceList>) {
        spaceList.clear()

        val tempSpaceList = item.sortedWith(
            compareBy(
                // Custom order based on plan type
                { it.plan in listOf(Constant.COMMUNITY, Constant.EARLY_BIRD) }, // Plans other than "community" and "free" come first
                { it.plan != Constant.EARLY_BIRD }, // "community" plans come before "free" plans
                { it.plan } // Sort alphabetically within the same plan type
            )
        )

        spaceList.addAll(tempSpaceList)

        callGetSilencingRules(spaceList[spaceListItemPosition].id!!)
    }
}
