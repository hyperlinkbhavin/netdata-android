package cloud.netdata.android.ui.settings.fragment

import android.Manifest
import android.content.res.ColorStateList
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.enumclass.Priority
import cloud.netdata.android.data.pojo.response.NotificationPriorityList
import cloud.netdata.android.databinding.NotificationPrioritySettingsFragmentBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.utils.*
import cloud.netdata.android.utils.localdb.DatabaseHelper
import com.fondesa.kpermissions.extension.onAccepted
import com.fondesa.kpermissions.extension.onDenied
import com.fondesa.kpermissions.extension.onPermanentlyDenied
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class NotificationPrioritySettingsFragment :
    BaseFragment<NotificationPrioritySettingsFragmentBinding>() {

    lateinit var dbHelper: DatabaseHelper
    private val AUDIO_PERMISSION_REQUEST_CODE = 123
    private val AUDIO_REQUEST_KEY = "audio_request"

    private lateinit var audioPickerLauncher: ActivityResultLauncher<String>
    private var notificationPriorityList = ArrayList<NotificationPriorityList>()

    /*private var isHighPrioritySound = false
    private var isMediumPrioritySound = false
    private var isLowPrioritySound = false*/

    private var isTempHighPrioritySound = false
    private var isTempMediumPrioritySound = false
    private var isTempLowPrioritySound = false

    private var conditionId = 0

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): NotificationPrioritySettingsFragmentBinding {
        return NotificationPrioritySettingsFragmentBinding.inflate(
            inflater,
            container,
            attachToRoot
        )
    }

    override fun bindData() {
        dbHelper = DatabaseHelper(requireContext())
        toolbar()
        manageClick()

        getPermission()
        audioActivityResult()

//        dbHelper.updateNotificationPriorityData(1,"Test", "url",1,1,1)

        setData()

        /*dbHelper.getAllDataFromNotificationPriority()*/
    }

    private fun toolbar() = with(binding) {
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text =
            getString(R.string.title_notification_priority_settings)

        /*checkSwitch(switchHighPrioritySound, buttonHighPriorityApplyCustomTune)
        checkSwitch(switchMediumPrioritySound, buttonMediumPriorityApplyCustomTune)
        checkSwitch(switchLowPrioritySound, buttonLowPriorityApplyCustomTune)*/
    }

    private fun setData() = with(binding) {
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

        notificationPriorityList = dbHelper.getAllDataFromNotificationPriority()

        notificationCheck(
            0,
            notificationPriorityList[0],
            switchHighPrioritySound,
            switchHighPriorityBanner,
            switchHighPriorityVibration,
            buttonHighPriorityApplyCustomTune,
            buttonHighPriorityChangeSoundTune,
            textViewHighPrioritySoundTune
        )
        notificationCheck(
            1,
            notificationPriorityList[1],
            switchMediumPrioritySound,
            switchMediumPriorityBanner,
            switchMediumPriorityVibration,
            buttonMediumPriorityApplyCustomTune,
            buttonMediumPriorityChangeSoundTune,
            textViewMediumPrioritySoundTune
        )
        notificationCheck(
            2,
            notificationPriorityList[2],
            switchLowPrioritySound,
            switchLowPriorityBanner,
            switchLowPriorityVibration,
            buttonLowPriorityApplyCustomTune,
            buttonLowPriorityChangeSoundTune,
            textViewLowPrioritySoundTune
        )
    }

    private fun notificationCheck(
        position: Int,
        item: NotificationPriorityList,
        switchSound: SwitchCompat,
        switchBanner: SwitchCompat,
        switchVibration: SwitchCompat,
        buttonView: AppCompatButton,
        buttonView2: AppCompatButton,
        textView: AppCompatTextView,
    ) {
        /*if (item.isSound == 1) {
            switchSound.isChecked = true
            buttonView.isClickable = true
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
            buttonView.invisible()
            buttonView2.visible()
            textView.visible()
            textView.text = notificationPriorityList[position].soundName
        } else {
            switchSound.isChecked = false
            buttonView.isClickable = false
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorGreyCF)
            )
        }*/

        switchSound.isChecked = item.isSound == 1
        switchBanner.isChecked = item.isBanner == 1
        switchVibration.isChecked = item.isVibration == 1
    }

    private fun manageClick() = with(binding) {
        switchHighPrioritySound.setOnCheckedChangeListener { buttonView, isChecked ->
            conditionId = 1
            switchCheckChanged(
                isChecked,
                buttonHighPriorityApplyCustomTune,
                buttonHighPriorityChangeSoundTune,
                textViewHighPrioritySoundTune
            )
        }

        switchMediumPrioritySound.setOnCheckedChangeListener { buttonView, isChecked ->
            conditionId = 2
            switchCheckChanged(
                isChecked,
                buttonMediumPriorityApplyCustomTune,
                buttonMediumPriorityChangeSoundTune,
                textViewMediumPrioritySoundTune
            )
        }

        switchLowPrioritySound.setOnCheckedChangeListener { buttonView, isChecked ->
            conditionId = 3
            switchCheckChanged(
                isChecked,
                buttonLowPriorityApplyCustomTune,
                buttonLowPriorityChangeSoundTune,
                textViewLowPrioritySoundTune
            )
        }

        binding.buttonHighPriorityApplyCustomTune.setOnClickListener {
            isTempHighPrioritySound = true
            isTempMediumPrioritySound = false
            isTempLowPrioritySound = false

            openAudioPicker()

            //            manageNightModeForBottomSheet()
        }

        buttonMediumPriorityApplyCustomTune.setOnClickListener {
            isTempHighPrioritySound = false
            isTempMediumPrioritySound = true
            isTempLowPrioritySound = false

            openAudioPicker()
            //            manageNightModeForBottomSheet()
        }

        buttonLowPriorityApplyCustomTune.setOnClickListener {
            isTempHighPrioritySound = false
            isTempMediumPrioritySound = false
            isTempLowPrioritySound = true

            openAudioPicker()

            //            manageNightModeForBottomSheet()
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

        switchHighPriorityBanner.setOnCheckedChangeListener { buttonView, isChecked ->
            dbHelper.updateNotificationPriorityData(
                isBanner = if (isChecked) 1 else 0,
                conditionID = 1
            )
        }

        switchMediumPriorityBanner.setOnCheckedChangeListener { buttonView, isChecked ->
            dbHelper.updateNotificationPriorityData(
                isBanner = if (isChecked) 1 else 0,
                conditionID = 2
            )
        }

        switchLowPriorityBanner.setOnCheckedChangeListener { buttonView, isChecked ->
            dbHelper.updateNotificationPriorityData(
                isBanner = if (isChecked) 1 else 0,
                conditionID = 3
            )
        }

        switchHighPriorityVibration.setOnCheckedChangeListener { buttonView, isChecked ->
            dbHelper.updateNotificationPriorityData(
                isVibration = if (isChecked) 1 else 0,
                conditionID = 1
            )
        }

        switchMediumPriorityVibration.setOnCheckedChangeListener { buttonView, isChecked ->
            dbHelper.updateNotificationPriorityData(
                isVibration = if (isChecked) 1 else 0,
                conditionID = 2
            )
        }

        switchLowPriorityVibration.setOnCheckedChangeListener { buttonView, isChecked ->
            dbHelper.updateNotificationPriorityData(
                isVibration = if (isChecked) 1 else 0,
                conditionID = 3
            )
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

    private fun openAudioPicker() {
        audioPickerLauncher.launch("audio/*")
    }

    private fun audioActivityResult() {
        audioPickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { audioUri ->
                if (audioUri != null) {
                    appPreferences.putString(Constant.APP_PREF_TEMP_AUDIO, audioUri.toString())
                    val audio = FileUtils.getRealPathFromURI(requireContext(), audioUri)
                    val audioName = audio!!.split("/").last()
                    Log.e("name", audioName)
                    binding.apply {
                        dbHelper.updateNotificationPriorityData(
                            isSound = 1,
                            soundName = audioName,
                            soundUrl = audio,
                            conditionID = conditionId
                        )
                        if (isTempHighPrioritySound) {
//                        isHighPrioritySound = true
                            buttonHighPriorityApplyCustomTune.invisible()
                            buttonHighPriorityChangeSoundTune.visible()
                            textViewHighPrioritySoundTune.visible()
                            textViewHighPrioritySoundTune.text = audioName
                        } else if (isTempMediumPrioritySound) {
//                        isMediumPrioritySound = true
                            buttonMediumPriorityApplyCustomTune.invisible()
                            buttonMediumPriorityChangeSoundTune.visible()
                            textViewMediumPrioritySoundTune.visible()
                            textViewMediumPrioritySoundTune.text = audioName
                        } else {
//                        isLowPrioritySound = true
                            buttonLowPriorityApplyCustomTune.invisible()
                            buttonLowPriorityChangeSoundTune.visible()
                            textViewLowPrioritySoundTune.visible()
                            textViewLowPrioritySoundTune.text = audioName
                        }
                    }
                    // Handle the selected audio URI here
                }
            }
    }

    private fun switchCheckChanged(
        isChecked: Boolean,
        buttonView: AppCompatButton,
        buttonView2: AppCompatButton,
        textView: AppCompatTextView
    ) {
        if(isChecked){
            dbHelper.updateNotificationPriorityData(
                isSound = 1,
                soundName = "",
                soundUrl = "",
                conditionID = conditionId
            )
        } else {
            dbHelper.updateNotificationPriorityData(
                isSound = 0,
                soundName = "",
                soundUrl = "",
                conditionID = conditionId
            )
        }
        /*if (isChecked) {
            buttonView.isClickable = true
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
        } else {
            dbHelper.updateNotificationPriorityData(
                isSound = 0,
                soundName = "",
                soundUrl = "",
                conditionID = conditionId
            )
            buttonView.visible()
            buttonView.isClickable = false
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorGreyCF)
            )
            buttonView2.gone()
            textView.gone()
        }*/
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

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
                //the selected audio.
                val uri: Uri? = data!!.data
            Log.e("audioFile", data.data.toString())

        }
    }*/

    /*private fun checkSwitch(switchView: SwitchCompat, buttonView: AppCompatButton) {
        if (switchView.isChecked) {
            buttonView.isClickable = true
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
        } else {
            buttonView.isClickable = false
            buttonView.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.colorGreyCF)
            )
        }
    }*/

    /*private fun manageNightModeForBottomSheet() {
        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> bottomSheetSingleAudioPicker(Color.WHITE)
            Configuration.UI_MODE_NIGHT_NO -> bottomSheetSingleAudioPicker(Color.DKGRAY)
        }
    }*/

    /*private fun bottomSheetSingleAudioPicker(color: Int) {
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
    }*/

    /*private fun audioActivityResult(){
        setFragmentResultListener(SingleAudioPicker.SINGLE_AUDIO_REQUEST_KEY) { _, bundle ->
            val loadedModel =
                bundle.getParcelable<AudioModel>(SingleAudioPicker.ON_SINGLE_AUDIO_PICK_KEY)
            loadedModel?.let {
                binding.apply {
                    dbHelper.updateNotificationPriorityData(
                        isSound = 1,
                        soundName = it.displayName,
                        soundUrl = FileUtils.getRealPathFromURI(requireContext(), it.contentUri),
                        conditionID = conditionId
                    )
                    if (isTempHighPrioritySound) {
//                        isHighPrioritySound = true
                        buttonHighPriorityApplyCustomTune.invisible()
                        buttonHighPriorityChangeSoundTune.visible()
                        textViewHighPrioritySoundTune.visible()
                        textViewHighPrioritySoundTune.text = it.displayName
                    } else if (isTempMediumPrioritySound) {
//                        isMediumPrioritySound = true
                        buttonMediumPriorityApplyCustomTune.invisible()
                        buttonMediumPriorityChangeSoundTune.visible()
                        textViewMediumPrioritySoundTune.visible()
                        textViewMediumPrioritySoundTune.text = it.displayName
                    } else {
//                        isLowPrioritySound = true
                        buttonLowPriorityApplyCustomTune.invisible()
                        buttonLowPriorityChangeSoundTune.visible()
                        textViewLowPrioritySoundTune.visible()
                        textViewLowPrioritySoundTune.text = it.displayName
                    }
                }

            }
        }
    }*/
}

