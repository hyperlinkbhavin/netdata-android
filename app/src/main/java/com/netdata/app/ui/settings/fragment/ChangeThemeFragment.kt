package com.netdata.app.ui.settings.fragment

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.netdata.app.R
import com.netdata.app.data.pojo.enumclass.ThemeMode
import com.netdata.app.databinding.ChangeThemeFragmentBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.utils.Constant


class ChangeThemeFragment: BaseFragment<ChangeThemeFragmentBinding>() {

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ChangeThemeFragmentBinding {
        return ChangeThemeFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        toolbar()
        manageClick()

        val nightModeFlags = requireContext().resources.configuration.uiMode and
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
        }
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewToolbarTitle.text = getString(R.string.title_change_theme)
    }

    private fun manageClick() = with(binding){
        constraintDayTheme.setOnClickListener {
            radioButtonDayTheme.isChecked = true
            radioButtonNightTheme.isChecked = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            setNightMode(requireContext(), false)
        }

        constraintNightTheme.setOnClickListener {
            radioButtonDayTheme.isChecked = false
            radioButtonNightTheme.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            setNightMode(requireContext(), true)
        }

        radioButtonDayTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            radioButtonDayTheme.isChecked = isChecked
            radioButtonNightTheme.isChecked = !isChecked
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            setNightMode(requireContext(), false)
        }

        radioButtonNightTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            radioButtonDayTheme.isChecked = !isChecked
            radioButtonNightTheme.isChecked = isChecked
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            setNightMode(requireContext(), true)
        }

        buttonDone.setOnClickListener {
            if(radioButtonNightTheme.isChecked){
                appPreferences.putString(Constant.APP_PREF_DAY_NIGHT_MODE, ThemeMode.Night.name)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                appPreferences.putString(Constant.APP_PREF_DAY_NIGHT_MODE, ThemeMode.Day.name)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            navigator.goBack()
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