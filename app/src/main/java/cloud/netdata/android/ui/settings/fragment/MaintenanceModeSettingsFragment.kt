package cloud.netdata.android.ui.settings.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
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
    private var isAllChange = false
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
                    if (isChecked) {
                        /*if(item.id == appPreferences.getString(Constant.APP_PREF_SPACE_ID)){
                            appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, true)
                        }
                        callSilenceSpace(item)*/
                    } else {
                        if(item.id == appPreferences.getString(Constant.APP_PREF_SPACE_ID)){
                            appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, false)
                        }
                        val ruleId = ArrayList<String>()
                        /*if(!item.silenceRuleId.isNullOrEmpty()){
                            ruleId.add(item.silenceRuleId!!)
                        }*/
                        ruleId.addAll(item.silenceRuleIdList)
                        callUnsilenceSpace(item, ruleId, position)
//                        updateData(itemPosition, clickPosition)
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
        itemPosition = position
        clickPosition = 2
        item.isUntil = true
        if(item.silenceRuleIdList.isNotEmpty()){
            isChanged = true
            val ruleId = ArrayList<String>()
            ruleId.addAll(item.silenceRuleIdList)
            callUnsilenceSpace(item, ruleId, position)
//                        updateData(itemPosition, clickPosition)
        } else {
            isChanged = false
            callSilenceSpace(item)
        }
    }

    private fun changeForever(item: SpaceList, position: Int){
        item.isForever = true
        item.isUntil = false
        item.untilDate = ""
        itemPosition = position
        clickPosition = 3
        if(item.silenceRuleIdList.isNotEmpty()){
            isChanged = true
            val ruleId = ArrayList<String>()
            ruleId.addAll(item.silenceRuleIdList)
            callUnsilenceSpace(item, ruleId, position)
//                        updateData(itemPosition, clickPosition)
        } else {
            isChanged = false
            callSilenceSpace(item)
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
            getString(R.string.title_maintenance_mode_settings)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun manageClick() = with(binding) {
        switchDisableAllNotifications.setOnClickListener {
            if(switchDisableAllNotifications.isChecked){
                binding.constraintDisableNotifications.isSelected = true
                binding.radioGroupAllNotifications.visible()
            } else {
                changeAllNotificationData(switchDisableAllNotifications.isChecked)
            }
        }

        textViewUntilDate.setOnClickListener {
            isChanged = true
            datePicker()
        }

        radioButtonForever.setOnClickListener {
            if (radioButtonForever.isChecked) {
                textViewUntilDate.text = "DD/MM/YY, HH:MM"
            }
            spaceList.forEach {
                it.isForever = true
                it.isUntil = false
                it.untilDate = ""
            }

            changeAllNotificationData(true)
        }

        radioButtonUntil.setOnClickListener {
            isChanged = true
            datePicker()
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    private fun changeAllNotificationData(isChecked: Boolean, isUntil: Boolean = false) {
        if (isChecked) {
            appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, true)
            binding.radioGroupAllNotifications.visible()
            allChangeIndex += 1
            if(allChangeIndex != spaceList.size){
                isAllChange = true

                if(!spaceList[allChangeIndex].isSelected
                    && !spaceList[allChangeIndex].plan.equals(Constant.COMMUNITY, true)
                    && !spaceList[allChangeIndex].plan.equals(Constant.EARLY_BIRD, true)
                    && !isUntil){
                    itemPosition = allChangeIndex
                    clickPosition = 1
                    callSilenceSpace(spaceList[allChangeIndex])
                } else if(isUntil
                    && !spaceList[allChangeIndex].plan.equals(Constant.COMMUNITY, true)
                    && !spaceList[allChangeIndex].plan.equals(Constant.EARLY_BIRD, true)){
                    itemPosition = allChangeIndex
                    clickPosition = 1
                    isChanged = true
                    callUnsilenceSpace(spaceList[allChangeIndex], spaceList[allChangeIndex].silenceRuleIdList, clickPosition)
                } else {
                    changeAllNotificationData(true)
                }
            } else {
                isAllChange = false
                allChangeIndex = -1
                getCheckData()
            }


            /*for ((index, item) in spaceList.withIndex()) {
                if (!item.isSelected) {
                    itemPosition = index
                    clickPosition = 1
                    *//*item.isSelected = switchDisableAllNotifications.isChecked
                    item.isForever = switchDisableAllNotifications.isChecked
                    item.isUntil = false
                    item.untilDate = ""*//*

//                    Handler(Looper.getMainLooper()).postDelayed({
                    callSilenceSpace(item)
//                    },2000)
                }
            }*/
        } else {
            appPreferences.putBoolean(Constant.APP_PREF_IS_SPACE_SILENCE, true)
            allChangeIndex += 1
            if(allChangeIndex != spaceList.size){
                isAllChange = true

                if(!spaceList[allChangeIndex].plan.equals(Constant.COMMUNITY, true)
                    && !spaceList[allChangeIndex].plan.equals(Constant.EARLY_BIRD, true)){
                    itemPosition = allChangeIndex
                    clickPosition = 4
                    callUnsilenceSpace(spaceList[allChangeIndex], spaceList[allChangeIndex].silenceRuleIdList, allChangeIndex)
                } else {
                    changeAllNotificationData(false)
                }
            } else {
                isAllChange = false
                allChangeIndex = -1
                getCheckData()
            }
        }
        maintenanceModeSettingsAdapter.notifyDataSetChanged()
    }

    private fun changeDataForUnsilence(position: Int){
        spaceList[position].silenceRuleId = ""
        spaceList[position].silenceRuleName = ""
        spaceList[position].silenceRuleIdList = arrayListOf()
        updateData(position, clickPosition)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding) {
        recyclerViewMaintenanceMode.adapter = maintenanceModeSettingsAdapter
        maintenanceModeSettingsAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateData(pos: Int, clickPos: Int) {

//        appPreferences.putString(Constant.APP_PREF_SPACE_LIST_MAINTAIN, Gson().toJson(spaceList))

        /*if(spaceList[pos].isSelected){
            callSilenceSpace(item)
        } else {
            callUnsilenceSpace(item)
        }*/

        try{
            when (clickPos) {
                1 -> {
                    spaceList[pos].isSelected = !spaceList[pos].isSelected
                    spaceList[pos].isForever = !spaceList[pos].isForever
                    spaceList[pos].isUntil = false
                    spaceList[pos].untilDate = ""
                }
                2 -> {
                    spaceList[pos].isForever = false
                    spaceList[pos].isUntil = true
                }
                3 -> {
                    spaceList[pos].isForever = true
                    spaceList[pos].isUntil = false
                    spaceList[pos].untilDate = ""
                }
                4 -> {
                    spaceList[pos].isSelected = false
                    spaceList[pos].isForever = false
                    spaceList[pos].isUntil = false
                    spaceList[pos].untilDate = ""
                }
            }

            getCheckData()

            /*dbHelper.updateMaintenanceMode(Gson().toJson(spaceList.map {space ->
                mapOf(
                    "id" to space.id,
                    "slug" to space.slug,
                    "name" to space.name,
                    "description" to space.description,
                    "iconURL" to space.iconURL,
                    "createdAt" to space.createdAt,
                    "permissions" to space.permissions,
                    "plan" to space.plan,
                    "planDefinition" to space.planDefinition,
                    "isSelected" to space.isSelected,
                    "isForever" to space.isForever,
                    "isUntil" to space.isUntil,
                    "untilDate" to space.untilDate,
                    "silenceRuleId" to space.silenceRuleId,
                    "silenceRuleIdList" to arrayListOf(""),
                    "silenceRuleName" to space.silenceRuleName,
                    "count" to space.count,
                )
            }))*/
            maintenanceModeSettingsAdapter.notifyDataSetChanged()
        }
        catch (_: Exception){
        }
    }

    private fun getCheckData() {
        val data = spaceList.find { !it.isSelected && !it.plan.equals(Constant.COMMUNITY, true)
                && !it.plan.equals(Constant.EARLY_BIRD, true) }
        if (data?.name.isNullOrEmpty()) {
            binding.switchDisableAllNotifications.isChecked = true
            binding.radioGroupAllNotifications.visible()
        } else {
            binding.switchDisableAllNotifications.isChecked = false
            binding.radioGroupAllNotifications.gone()
        }
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

                spaceList.forEach {
                    it.isForever = false
                    it.isUntil = true
                    it.untilDate = "$date, $selectedHour:$minutes"
                }

                changeAllNotificationData(true, true)
                textViewUntilDate.text = "$date, $selectedHour:$minutes"
                radioButtonUntil.isChecked = true
                maintenanceModeSettingsAdapter.notifyDataSetChanged()
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
                                if(silenceRule.lastsUntil != null){
                                    spaceList[spaceListItemPosition].untilDate = ConvertDateTimeFormat.convertDate(silenceRule.lastsUntil!!,
                                        DateTimeFormats.SERVER_DATE_TIME_FORMAT_NEW_ONE, DateTimeFormats.MAINTENANCE_MODE_DATE_FORMAT)
                                    spaceList[spaceListItemPosition].isUntil = true
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
                updateData(itemPosition, clickPosition)
                if(isAllChange){
                    changeAllNotificationData(true)
                }
            } else {
                if(isAllChange){
                    changeAllNotificationData(true)
                }
                showMessage("Something wrong for ${spaceList[itemPosition].name}")
                maintenanceModeSettingsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun callUnsilenceSpace(item: SpaceList, ruleId: ArrayList<String>, position: Int) {
        showLoader()
        changeDataForUnsilence(position)
        val ruleIdArrayList = ArrayList(ruleId.filter { it.isNotEmpty() }.distinct())
        apiViewModel.callUnsilenceSpace(item.id!!, ruleIdArrayList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeUnsilenceSpace() {
        apiViewModel.unsilenceSpaceLiveData.observe(this) {
            hideLoader()
            if(it.isError){
                if(isChanged){
                    isChanged = false
                    callSilenceSpace(spaceList[itemPosition])
                }
                if(isAllChange){
                    changeAllNotificationData(false)
                }
            } else {
                if(isAllChange){
                    changeAllNotificationData(false)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkAndStoreData(item: ArrayList<SpaceList>) {
        spaceList.clear()
        /*if(dbHelper.getMaintenanceMode().isEmpty()){
            dbHelper.insertMaintenanceMode(Gson().toJson(item))
        }

        val type = object : TypeToken<ArrayList<SpaceList>>() {}.type
        val arrayList: ArrayList<SpaceList> =
            Gson().fromJson(dbHelper.getMaintenanceMode(), type)

        val idsInFirstList = item.map { it.id }
        val idsInSecondList = arrayList.map { it.id }

        val newDataToAdd = item.filter { !idsInSecondList.contains(it.id) }

        arrayList.addAll(newDataToAdd)

        val dataToRemove = arrayList.filter { it.id !in idsInFirstList }

        arrayList.removeAll(dataToRemove.toSet())*/

        val tempSpaceList = item.sortedWith(
            compareBy(
                // Custom order based on plan type
                { it.plan in listOf(Constant.COMMUNITY, Constant.EARLY_BIRD) }, // Plans other than "community" and "free" come first
                { it.plan != Constant.EARLY_BIRD }, // "community" plans come before "free" plans
                { it.plan } // Sort alphabetically within the same plan type
            )
        )

        spaceList.addAll(tempSpaceList)

        /*dbHelper.updateMaintenanceMode(Gson().toJson(spaceList.map {space ->
            mapOf(
                "id" to space.id,
                "slug" to space.slug,
                "name" to space.name,
                "description" to space.description,
                "iconURL" to space.iconURL,
                "createdAt" to space.createdAt,
                "permissions" to space.permissions,
                "plan" to space.plan,
                "planDefinition" to space.planDefinition,
                "isSelected" to space.isSelected,
                "isForever" to space.isForever,
                "isUntil" to space.isUntil,
                "untilDate" to space.untilDate,
                "silenceRuleId" to space.silenceRuleId,
                "silenceRuleName" to space.silenceRuleName,
                "count" to space.count,
            )
        }))*/

        /*val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())

        for((index, items) in spaceList.withIndex()){
            try{
                val ruleId = ArrayList<String>()
                if(!items.silenceRuleId.isNullOrEmpty()){
                    ruleId.add(items.silenceRuleId!!)
                }
                val dataDate = dateFormat.parse(items.untilDate!!)
                if(dataDate != null && dataDate.before(currentDate)){
                    callUnsilenceSpace(items, ruleId, index)
                }
            } catch (e: Exception){
            }
        }*/
        callGetSilencingRules(spaceList[spaceListItemPosition].id!!)
    }
}
