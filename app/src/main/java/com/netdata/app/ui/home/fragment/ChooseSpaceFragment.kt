package com.netdata.app.ui.home.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import com.netdata.app.R
import com.netdata.app.data.pojo.request.ChooseSpaceList
import com.netdata.app.data.pojo.response.SpaceList
import com.netdata.app.databinding.AuthFragmentWelcomeBinding
import com.netdata.app.databinding.ChooseSpaceFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.auth.IsolatedFullActivity
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.HomeActivity
import com.netdata.app.ui.home.adapter.ChooseSpaceAdapter
import com.netdata.app.ui.settings.fragment.SettingsFragment
import com.netdata.app.utils.AppUtils
import com.netdata.app.utils.Constant
import com.netdata.app.utils.customapi.ApiViewModel
import com.netdata.app.utils.localdb.DatabaseHelper
import com.netdata.app.utils.visible
import java.util.Locale.filter

class ChooseSpaceFragment: BaseFragment<ChooseSpaceFragmentBinding>() {

    lateinit var dbHelper: DatabaseHelper

    private var spaceList = ArrayList<SpaceList>()

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    private val chooseSpaceAdapter by lazy {
        ChooseSpaceAdapter(){ view, position, item ->
            when(view.id){
                R.id.constraintTop -> {
                    appPreferences.putString(Constant.APP_PREF_SPACE_ID, item.id!!)
                    appPreferences.putString(Constant.APP_PREF_SPACE_NAME, item.name!!)
                    navigator.loadActivity(HomeActivity::class.java).byFinishingAll().start()
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
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ChooseSpaceFragmentBinding {
        return ChooseSpaceFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        dbHelper = DatabaseHelper(requireContext())
        toolbar()
        manageClick()
        setAdapter()
        spannableString()
        editTextChanged()
    }

    override fun onResume() {
        super.onResume()
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

    private fun spannableString() {
        val spanString =
            SpannableString(getString(R.string.label_you_have_a_total_of_3_notifications_in_your_spaces))
        val termsAndCondition: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        spanString.setSpan(termsAndCondition, 20, 22, 0)
        spanString.setSpan(ForegroundColorSpan(Color.parseColor("#00AB44")), 20, 22, 0)
        spanString.setSpan(StyleSpan(Typeface.BOLD), 20, 22, 0)

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
        apiViewModel.spaceListLiveData.observe(this){
            hideLoader()
            if(it.responseCode == 200){
                if(it.data!!.isNotEmpty()){
                    /*it.data.forEach {item ->
                        dbHelper.insertSpaceData(item)
                    }*/
                    spaceList.clear()
                    chooseSpaceAdapter.list.clear()
                    spaceList.addAll(it.data)
                    chooseSpaceAdapter.list.addAll(it.data)
                    chooseSpaceAdapter.notifyDataSetChanged()
                }
            } else {
                showMessage("Something wrong")
            }
        }
    }
}