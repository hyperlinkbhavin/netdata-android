package com.netdata.app.ui.home.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.netdata.app.R
import com.netdata.app.data.pojo.request.ChooseSpaceList
import com.netdata.app.databinding.AuthFragmentWelcomeBinding
import com.netdata.app.databinding.ChooseSpaceFragmentBinding
import com.netdata.app.databinding.HomeDetailsFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.auth.IsolatedFullActivity
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.HomeActivity
import com.netdata.app.ui.home.adapter.*
import com.netdata.app.ui.settings.fragment.SettingsFragment
import com.netdata.app.utils.Constant
import com.netdata.app.utils.visible

class HomeDetailsFragment: BaseFragment<HomeDetailsFragmentBinding>() {

    private val timeValuesAdapter by lazy {
        TimeValuesAdapter(){ view, position, item ->

        }
    }

    private val alertInfoAdapter by lazy {
        AlertInfoAdapter(){ view, position, item ->

        }
    }

    private val configurationAdapter by lazy {
        ConfigurationAdapter(){ view, position, item ->

        }
    }

    private val severityConditionsAdapter by lazy {
        SeverityConditionsAdapter(){ view, position, item ->

        }
    }

    private val instanceValuesAdapter by lazy {
        InstanceValuesAdapter(){ view, position, item ->

        }
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): HomeDetailsFragmentBinding {
        return HomeDetailsFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        toolbar()
        manageClick()
        setAdapter()
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewSpace.visible()
        includeToolbar.textViewSpace.text = appPreferences.getString(Constant.APP_PREF_SPACE_NAME)
    }

    private fun manageClick() = with(binding){

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding){
        recyclerViewTimeValues.adapter = timeValuesAdapter
        recyclerViewAlertInfo.adapter = alertInfoAdapter
        recyclerViewConfiguration.adapter = configurationAdapter
        recyclerViewSeverityConditions.adapter = severityConditionsAdapter
        recyclerViewInstanceValues.adapter = instanceValuesAdapter
    }

    private fun addData(){

    }
}