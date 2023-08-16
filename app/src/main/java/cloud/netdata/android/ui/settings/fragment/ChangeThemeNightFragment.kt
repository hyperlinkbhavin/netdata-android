package cloud.netdata.android.ui.settings.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import cloud.netdata.android.R
import cloud.netdata.android.data.pojo.enumclass.ThemeMode
import cloud.netdata.android.databinding.ChangeThemeNightFragmentBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.utils.Constant


class ChangeThemeNightFragment: BaseFragment<ChangeThemeNightFragmentBinding>() {

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ChangeThemeNightFragmentBinding {
        return ChangeThemeNightFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.colorBlack2B)


        toolbar()
        manageClick()

        /*val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.radioButtonNightTheme.isChecked = true
                binding.radioButtonDayTheme.isChecked = false
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.radioButtonNightTheme.isChecked = false
                binding.radioButtonDayTheme.isChecked = true
            }
        }*/
    }

    private fun toolbar() = with(binding){
        imageViewBack.setOnClickListener { navigator.goBack() }
        textViewToolbarTitle.text = getString(R.string.title_change_theme)
    }

    private fun manageClick() = with(binding){
        constraintDayTheme.setOnClickListener {
            navigator.load(ChangeThemeDayFragment::class.java).replace(false)
            /*radioButtonDayTheme.isChecked = true
            radioButtonNightTheme.isChecked = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            setNightMode(requireContext(), false)*/
        }

        constraintNightTheme.setOnClickListener {
            /*radioButtonDayTheme.isChecked = false
            radioButtonNightTheme.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            setNightMode(requireContext(), true)*/
        }

        radioButtonDayTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            navigator.load(ChangeThemeDayFragment::class.java).replace(false)
            /*radioButtonDayTheme.isChecked = isChecked
            radioButtonNightTheme.isChecked = !isChecked
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            setNightMode(requireContext(), false)*/
        }

        radioButtonNightTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            /*radioButtonDayTheme.isChecked = !isChecked
            radioButtonNightTheme.isChecked = isChecked
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            setNightMode(requireContext(), true)*/
        }

        buttonDone.setOnClickListener {
            appPreferences.putString(Constant.APP_PREF_DAY_NIGHT_MODE, ThemeMode.Night.name)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            requireActivity().recreate()
            /*if(radioButtonNightTheme.isChecked){
                appPreferences.putString(Constant.APP_PREF_DAY_NIGHT_MODE, ThemeMode.Night.name)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                appPreferences.putString(Constant.APP_PREF_DAY_NIGHT_MODE, ThemeMode.Day.name)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }*/
            navigator.finish()
        }
    }

   /* @SuppressLint("ObsoleteSdkInt")
    fun setNightMode(target: Context, state: Boolean) {
        val uiManager = target.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (VERSION.SDK_INT <= 22) {
            uiManager.enableCarMode(0)
        }
        if (state) {
            uiManager.nightMode = UiModeManager.MODE_NIGHT_YES
        } else {
            uiManager.nightMode = UiModeManager.MODE_NIGHT_NO
        }
    }*/
}