package com.netdata.app.ui.notification.fragment

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.netdata.app.R
import com.netdata.app.data.pojo.enumclass.Priority
import com.netdata.app.data.pojo.request.NotificationsList
import com.netdata.app.data.pojo.request.WarRoomsList
import com.netdata.app.databinding.NotificationFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.ui.home.adapter.AllWarRoomsAdapter
import com.netdata.app.ui.notification.adapter.NotificationAdapter
import com.netdata.app.utils.Constant

class NotificationFragment : BaseFragment<NotificationFragmentBinding>() {

    private var warRoomsItemPosition = 0

    private val notificationsAdapter by lazy {
        NotificationAdapter() { view, position, item ->
            when(view.id){
                R.id.constraintTop -> {
                    if(position == 0){
                        appPreferences.putString(Constant.APP_PREF_SPACE_NAME, "Space 1")
                    } else if(position == 1) {
                        appPreferences.putString(Constant.APP_PREF_SPACE_NAME, "Space 3")
                    } else {
                        appPreferences.putString(Constant.APP_PREF_SPACE_NAME, "Space 2")
                    }
                    appPreferences.putBoolean(Constant.APP_PREF_FROM_NOTIFICATION, true)
                    navigator.goBack()
                }
            }
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
        constraintAllWarRooms.setOnClickListener {
            bottomSheetAllWarRooms()
        }
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

    @SuppressLint("NotifyDataSetChanged")
    private fun bottomSheetAllWarRooms() {

        val dialog = BottomSheetDialog(requireContext())

        val view = layoutInflater.inflate(R.layout.bottom_sheet_select_war_rooms, null)

        val allWarRoomsAdapter by lazy {
            AllWarRoomsAdapter() { view, position, item ->
                when(view.id){
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

        val recyclerViewSelectWarRooms = view.findViewById<RecyclerView>(R.id.recyclerViewSelectWarRooms)
        val textViewLabelClose = view.findViewById<AppCompatTextView>(R.id.textViewLabelClose)
        recyclerViewSelectWarRooms.adapter = allWarRoomsAdapter

        if(warRoomsItemPosition != -1){
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
}