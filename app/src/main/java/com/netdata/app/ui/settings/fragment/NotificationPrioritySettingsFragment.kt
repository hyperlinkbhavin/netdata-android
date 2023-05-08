package com.netdata.app.ui.settings.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import com.crazylegend.audiopicker.audios.AudioModel
import com.crazylegend.audiopicker.pickers.SingleAudioPicker
import com.crazylegend.core.modifiers.TitleTextModifier
import com.fondesa.kpermissions.extension.onAccepted
import com.fondesa.kpermissions.extension.onDenied
import com.fondesa.kpermissions.extension.onPermanentlyDenied
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.netdata.app.R
import com.netdata.app.databinding.NotificationPrioritySettingsFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.utils.gone
import com.netdata.app.utils.invisible
import com.netdata.app.utils.visible


class NotificationPrioritySettingsFragment: BaseFragment<NotificationPrioritySettingsFragmentBinding>() {

    private var isHighPrioritySound = false
    private var isMediumPrioritySound = false
    private var isLowPrioritySound = false

    private var isTempHighPrioritySound = false
    private var isTempMediumPrioritySound = false
    private var isTempLowPrioritySound = false

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): NotificationPrioritySettingsFragmentBinding {
        return NotificationPrioritySettingsFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        toolbar()
        manageClick()

        setFragmentResultListener(SingleAudioPicker.SINGLE_AUDIO_REQUEST_KEY) { _, bundle ->
            val loadedModel = bundle.getParcelable<AudioModel>(SingleAudioPicker.ON_SINGLE_AUDIO_PICK_KEY)
            loadedModel?.let {
                binding.apply {
                    if(isTempHighPrioritySound){
                        isHighPrioritySound = true
                        buttonHighPriorityApplyCustomTune.invisible()
                        buttonHighPriorityChangeSoundTune.visible()
                        textViewHighPrioritySoundTune.visible()
                        textViewHighPrioritySoundTune.text = it.displayName
                    } else if(isTempMediumPrioritySound){
                        isMediumPrioritySound = true
                        buttonMediumPriorityApplyCustomTune.invisible()
                        buttonMediumPriorityChangeSoundTune.visible()
                        textViewMediumPrioritySoundTune.visible()
                        textViewMediumPrioritySoundTune.text = it.displayName
                    } else {
                        isLowPrioritySound = true
                        buttonLowPriorityApplyCustomTune.invisible()
                        buttonLowPriorityChangeSoundTune.visible()
                        textViewLowPrioritySoundTune.visible()
                        textViewLowPrioritySoundTune.text = it.displayName
                    }
                }

            }
        }
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_notification_priority_settings)

        checkSwitch(switchHighPrioritySound, buttonHighPriorityApplyCustomTune)
        checkSwitch(switchMediumPrioritySound, buttonMediumPriorityApplyCustomTune)
        checkSwitch(switchLowPrioritySound, buttonLowPriorityApplyCustomTune)
    }

    private fun manageClick() = with(binding){
        switchHighPrioritySound.setOnCheckedChangeListener { buttonView, isChecked ->
            switchCheckChanged(isChecked, buttonHighPriorityApplyCustomTune, buttonHighPriorityChangeSoundTune, textViewHighPrioritySoundTune)
        }

        switchMediumPrioritySound.setOnCheckedChangeListener { buttonView, isChecked ->
            switchCheckChanged(isChecked, buttonMediumPriorityApplyCustomTune, buttonMediumPriorityChangeSoundTune, textViewMediumPrioritySoundTune)
        }

        switchLowPrioritySound.setOnCheckedChangeListener { buttonView, isChecked ->
            switchCheckChanged(isChecked, buttonLowPriorityApplyCustomTune, buttonLowPriorityChangeSoundTune, textViewLowPrioritySoundTune)
        }

        binding.buttonHighPriorityApplyCustomTune.setOnClickListener {
            isTempHighPrioritySound = true
            isTempMediumPrioritySound = false
            isTempLowPrioritySound = false

            val nightModeFlags = requireContext().resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> bottomSheetSingleAudioPicker(Color.WHITE)
                Configuration.UI_MODE_NIGHT_NO -> bottomSheetSingleAudioPicker(Color.DKGRAY)
            }

        }

        buttonMediumPriorityApplyCustomTune.setOnClickListener {
            isTempHighPrioritySound = false
            isTempMediumPrioritySound = true
            isTempLowPrioritySound = false

            val nightModeFlags = requireContext().resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> bottomSheetSingleAudioPicker(Color.WHITE)
                Configuration.UI_MODE_NIGHT_NO -> bottomSheetSingleAudioPicker(Color.DKGRAY)
            }
        }

        buttonLowPriorityApplyCustomTune.setOnClickListener {
            isTempHighPrioritySound = false
            isTempMediumPrioritySound = false
            isTempLowPrioritySound = true

            val nightModeFlags = requireContext().resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            when (nightModeFlags) {
                Configuration.UI_MODE_NIGHT_YES -> bottomSheetSingleAudioPicker(Color.WHITE)
                Configuration.UI_MODE_NIGHT_NO -> bottomSheetSingleAudioPicker(Color.DKGRAY)
            }
        }

        buttonHighPriorityChangeSoundTune.setOnClickListener {
            buttonHighPriorityApplyCustomTune.performClick()
        }

        buttonMediumPriorityChangeSoundTune.setOnClickListener {
            buttonMediumPriorityApplyCustomTune.performClick()
        }

        buttonLowPriorityChangeSoundTune.setOnClickListener {
            buttonLowPriorityApplyCustomTune.performClick()
        }
    }

    private fun getPermission() {
//        hideLoader()
        val activityPermission by lazy {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionsBuilder(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_AUDIO
                ).build()
            } else {
                permissionsBuilder(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ).build()
            }
        }
        activityPermission
            .onAccepted {

            }
            .onDenied {
                showMessage(getString(R.string.msg_access_media_permission_required))
            }
            .onPermanentlyDenied {
                showMessage(getString(R.string.msg_access_media_permission_required))
                /*Handler(Looper.getMainLooper()).postDelayed({
                    showPermissionDeniedDialog()
                }, 1500)*/
            }
            .send()
    }

    /*private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this.requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton("Settings",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                })
            .setNegativeButton("Cancel", null)
            .show()
    }*/

    private fun bottomSheetSingleAudioPicker(color: Int){
        getPermission()

        SingleAudioPicker.showPicker(requireContext(), {
            setupViewHolderTitleText {
                textColor = color
                textPadding = 10 // use dp or sp this is only for demonstration purposes
            }
            setupBaseModifier(
                loadingIndicatorColor = R.color.minusColor,
                titleTextModifications = {
                    textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                    textStyle = TitleTextModifier.TextStyle.NORMAL
                    textColor = color
                    marginBottom = 30 // use dp or sp this is only for demonstration purposes
                    textPadding = 5 // use dp or sp this is only for demonstration purposes
                    textSize = 20f  // use sp this is only for demonstration purposes
                    textString = "Pick a tune"
                },
                placeHolderModifications = {
                    resID = R.drawable.ic_image
                }
            )
        })
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
                //the selected audio.
                val uri: Uri? = data!!.data
            Log.e("audioFile", data.data.toString())

        }
    }*/

    private fun checkSwitch(switchView: SwitchCompat, buttonView: AppCompatButton){
        if(switchView.isChecked){
            buttonView.isClickable = true
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        } else {
            buttonView.isClickable = false
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorGreyCF))
        }
    }

    private fun switchCheckChanged(isChecked: Boolean, buttonView: AppCompatButton, buttonView2: AppCompatButton, textView: AppCompatTextView){
        if(isChecked){
            buttonView.isClickable = true
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        } else {
            buttonView.visible()
            buttonView.isClickable = false
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorGreyCF))
            buttonView2.gone()
            textView.gone()
        }
    }
}

