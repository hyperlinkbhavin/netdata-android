package com.netdata.app.ui.notification.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.netdata.app.R
import com.netdata.app.data.pojo.enumclass.Priority
import com.netdata.app.data.pojo.request.NotificationsList
import com.netdata.app.databinding.AuthFragmentWelcomeBinding
import com.netdata.app.databinding.ChooseSpaceFragmentBinding
import com.netdata.app.databinding.NotificationFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.notification.adapter.NotificationAdapter

class NotificationFragment : BaseFragment<NotificationFragmentBinding>() {

    private val notificationsAdapter by lazy {
        NotificationAdapter() { view, position, item ->

        }
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): NotificationFragmentBinding {
        return NotificationFragmentBinding.inflate(inflater, container, attachToRoot)
    }

    override fun bindData() {
        toolbar()
        manageClick()
        setAdapter()
    }

    private fun toolbar() = with(binding) {
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_new_notifications)
    }

    private fun manageClick() = with(binding) {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding) {
        recyclerViewNotification.adapter = notificationsAdapter
        addData()
        notificationsAdapter.notifyDataSetChanged()
    }

    private fun addData() {
        notificationsAdapter.list.add(
            NotificationsList(
                "Space 1",
                "54.3% ${getString(R.string.label_dot)} ",
                "24 seconds ago · 04/04/2022 - 15:44:23",
                Priority.HIGH_PRIORITY
            )
        )

        notificationsAdapter.list.add(
            NotificationsList(
                "Space 3",
                "54.3% ${getString(R.string.label_dot)} ",
                "24 seconds ago · 04/04/2022 - 15:44:23",
                Priority.MEDIUM_PRIORITY
            )
        )

        notificationsAdapter.list.add(
            NotificationsList(
                "Space 2",
                "54.3% ${getString(R.string.label_dot)} ",
                "24 seconds ago · 04/04/2022 - 15:44:23",
                Priority.LOW_PRIORITY
            )
        )
    }
}