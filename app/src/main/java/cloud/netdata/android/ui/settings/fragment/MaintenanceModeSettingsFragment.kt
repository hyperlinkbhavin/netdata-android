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
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.DateTimeFormatter
import cloud.netdata.android.utils.customapi.ApiViewModel
import cloud.netdata.android.utils.gone
import cloud.netdata.android.utils.localdb.DatabaseHelper
import cloud.netdata.android.utils.visible
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class MaintenanceModeSettingsFragment: BaseFragment<MaintenanceModeSettingsFragmentBinding>() {

    lateinit var dbHelper: DatabaseHelper
    private var spaceList = ArrayList<SpaceList>()
    private var spaceListPosition = -1
    private var manageSpaceList = ArrayList<SpaceList>()

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    private val maintenanceModeSettingsAdapter by lazy {
        MaintenanceModeSettingsAdapter(spaceList) { view, position, item ->
            when (view.id) {
                R.id.switchDisableAllNotifications -> {
                    spaceListPosition = position
                    updateData(position, item, 1)
                    getCheckData()
                }
                R.id.textViewUntilDate -> {
                    spaceListPosition = position
                    updateData(position, item, 2)
                    getCheckData()
                }
                R.id.radioButtonForever -> {
                    spaceListPosition = position
                    updateData(position, item, 3)
                    getCheckData()
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
        observeSilenceSpace()
        observeUnsilenceSpace()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): MaintenanceModeSettingsFragmentBinding {
        return MaintenanceModeSettingsFragmentBinding.inflate(inflater, container, attachToRoot)
    }

    override fun bindData() {
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
            for(i in spaceList.indices){
                spaceListPosition = i
                spaceList[i].isSelected = switchDisableAllNotifications.isChecked
                spaceList[i].isForever = switchDisableAllNotifications.isChecked
                spaceList[i].isUntil = false
                Handler(Looper.getMainLooper()).postDelayed({
                    callSilenceSpace(spaceList[i])
                },1000)
            }
            if (switchDisableAllNotifications.isChecked) {
                radioGroupAllNotifications.visible()
            } else {
                radioGroupAllNotifications.gone()
            }
            maintenanceModeSettingsAdapter.notifyDataSetChanged()
        }

        textViewUntilDate.setOnClickListener {
            datePicker()
        }

        radioButtonForever.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                textViewUntilDate.text = "DD/MM/YY, HH:MM"
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding) {
        recyclerViewMaintenanceMode.adapter = maintenanceModeSettingsAdapter

        maintenanceModeSettingsAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateData(pos: Int, item: SpaceList, clickPos: Int) {
        when (clickPos) {
            1 -> {
                spaceList[pos].isSelected = !spaceList[pos].isSelected
                spaceList[pos].isForever = !spaceList[pos].isForever
            }
            2 -> {
                spaceList[pos].isForever = false
                spaceList[pos].isUntil = true
            }
            3 -> {
                spaceList[pos].isForever = true
                spaceList[pos].isUntil = false
            }
        }

        /*if(spaceList[pos].isSelected){
            callSilenceSpace(item)
        } else {
            callUnsilenceSpace(item)
        }*/
        callSilenceSpace(item)

        maintenanceModeSettingsAdapter.notifyItemChanged(pos)
    }

    private fun getCheckData() {
        val data = spaceList.find { !it.isSelected }
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
                val date2 = dates+"/"+months+"/"+year.toString().drop(2)
                timePicker(date2)
            },
            mCalendar.get(Calendar.YEAR),
            mCalendar.get(Calendar.MONTH),
            mCalendar.get(Calendar.DATE)
        )
        datePickerDialog.datePicker.minDate = mCalendar.time.time
        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun timePicker(date: String) =with(binding){
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker = TimePickerDialog( requireContext(),
            { timePicker, selectedHour, selectedMinute ->
                val minutes = if(selectedMinute < 10){
                    "0$selectedMinute"
                } else {
                    "$selectedMinute"
                }

                textViewUntilDate.text = "$date, $selectedHour:$minutes"
                radioButtonUntil.isChecked = true
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

    private fun callSilenceSpace(item: SpaceList) {
        showLoader()
        apiViewModel.callSilenceSpace(item.id!!, APIRequest(
            name = "Netdata-MobileApp-${item.id}",
            accountId = session.userId,
            integrationIds = arrayListOf("607bfd3c-02c1-4da2-b67a-0d01b518ce5d"),
            startsAt = DateTimeFormatter.currentUTCTime
        ))
    }

    private fun observeSilenceSpace() {
        apiViewModel.silenceSpaceLiveData.observe(this) {
            hideLoader()
            if (it.responseCode == 200) {
                spaceList[spaceListPosition].silenceRuleId = it.data!!.id
                spaceList[spaceListPosition].silenceRuleName = it.data.name
                appPreferences.putString(Constant.APP_PREF_SPACE_LIST_MAINTAIN, Gson().toJson(spaceList))
            } else {
                showMessage("Something wrong")
            }
        }
    }

    private fun callUnsilenceSpace(item: SpaceList) {
        showLoader()
        var ruleId = ""
        if(!item.silenceRuleId.isNullOrEmpty()){
            ruleId = item.silenceRuleId!!
        }
        apiViewModel.callUnsilenceSpace(item.id!!, APIRequest(
            noRule = arrayListOf(ruleId)
        ))
    }

    private fun observeUnsilenceSpace() {
        apiViewModel.unsilenceSpaceLiveData.observe(this) {
            hideLoader()
            if (it.responseCode == 200) {
                spaceList[spaceListPosition].silenceRuleId = ""
                spaceList[spaceListPosition].silenceRuleName = ""
                appPreferences.putString(Constant.APP_PREF_SPACE_LIST_MAINTAIN, Gson().toJson(spaceList))
            } else {
                showMessage("Something wrong")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkAndStoreData(item: ArrayList<SpaceList>) {
        spaceList.clear()

        val type = object : TypeToken<ArrayList<SpaceList>>() {}.type
        val arrayList: ArrayList<SpaceList> =
            Gson().fromJson(appPreferences.getString(Constant.APP_PREF_SPACE_LIST_MAINTAIN), type)

        val idsInFirstList = item.map { it.id }
        val idsInSecondList = arrayList.map { it.id }

        val newDataToAdd = item.filter { !idsInSecondList.contains(it.id) }

        arrayList.addAll(newDataToAdd)

        val dataToRemove = arrayList.filter { it.id !in idsInFirstList }

        arrayList.removeAll(dataToRemove.toSet())

        spaceList.addAll(arrayList)

        getCheckData()
        maintenanceModeSettingsAdapter.notifyDataSetChanged()
    }
}