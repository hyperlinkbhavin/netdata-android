package cloud.netdata.android.ui.base

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import cloud.netdata.android.R
import cloud.netdata.android.core.AppPreferences
import cloud.netdata.android.core.Session
import cloud.netdata.android.data.pojo.enumclass.ThemeMode
import cloud.netdata.android.di.HasComponent
import cloud.netdata.android.di.component.ActivityComponent
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.di.module.FragmentModule
import cloud.netdata.android.exception.ApplicationException
import cloud.netdata.android.exception.AuthenticationException
import cloud.netdata.android.exception.ServerException
import cloud.netdata.android.ui.manager.Navigator
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.Validator
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

abstract class BaseFragment<T : ViewBinding> : Fragment(), HasComponent<FragmentComponent> {

    protected lateinit var toolbar: HasToolbar

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    open lateinit var validator: Validator

    @Inject
    open lateinit var session: Session

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val component: FragmentComponent
        get() {
            return getComponent(ActivityComponent::class.java).plus(FragmentModule(this))
        }

    private var _binding: T? = null

    protected val binding: T
        get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = createViewBinding(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window: Window = requireActivity().window

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)



        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                appPreferences.putString(Constant.APP_PREF_DAY_NIGHT_MODE, ThemeMode.Night.name)
                window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.colorBlack2B)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                appPreferences.putString(Constant.APP_PREF_DAY_NIGHT_MODE, ThemeMode.Day.name)
                window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.colorWhiteF2)
            }
        }

        if(session.userSession.isNotEmpty()){
            Constant.TOKEN = session.userSession
        }

        bindData()
    }

    protected fun <C> getComponent(componentType: Class<C>): C {
        return componentType.cast((activity as HasComponent<C>).component)
    }

    override fun onAttach(context: Context) {
        inject(component)
        super.onAttach(context)

        if (activity is HasToolbar)
            toolbar = activity as HasToolbar


    }

    fun showLoader() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showLoader()
        }
    }

    fun hideLoader() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).hideLoader()
        }
    }

    fun hideKeyBoard() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).hideKeyboard()
        }
    }

    fun showKeyBoard() {
        if (activity is BaseActivity) {
            (activity as BaseActivity).showKeyboard()
        }
    }


    fun <T : BaseFragment<*>> getParentFragment(targetFragment: Class<T>): T? {
        if (parentFragment == null) return null
        try {
            return targetFragment.cast(parentFragment)
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
        return null
    }


    open fun onShow() {

    }

    fun showMessage(message: String) {
        showSnackBar(message)

    }

    fun showToast(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showMessage(@StringRes stringId: Int) {
        showSnackBar(getString(stringId))
    }

    fun showMessage(applicationException: ApplicationException) {

        showSnackBar(applicationException.message)
    }

    private fun showSnackBar(message: String) {
        hideKeyBoard()
        if (view != null) {
            val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
            snackbar.duration = 2000
            snackbar.setActionTextColor(Color.BLACK)
//            snackbar.setAction("OK", { snackbar.dismiss() })
            val snackView = snackbar.view
            val params = snackView.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            params.setMargins(0,20,0,0)
            snackView.layoutParams = params
            val textView =
                snackView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            textView.maxLines = 4

            snackView.background = ResourcesCompat.getDrawable(resources, R.color.colorPrimary, null)
            /*snackView.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.colorPrimary,
                    null
                )
            )*/
            snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
            snackbar.show()
        }
    }

    private fun showSnackBar(message: String, viewSet: View) {
        hideKeyBoard()
        if (viewSet != null) {
            val snackbar = Snackbar.make(viewSet, message, Snackbar.LENGTH_LONG)
            snackbar.duration = 3000
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.setAction("OK", View.OnClickListener { snackbar.dismiss() })
            val snackView = snackbar.getView()
            val textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.maxLines = 4

            snackView.setBackgroundColor(requireActivity().getResources().getColor(R.color.colorPrimary))
            snackbar.show()
        }
    }


    open fun onBackActionPerform(): Boolean {
        return true
    }

    open fun onViewClick(view: View) {

    }

    public fun onError(throwable: Throwable) {
        try {
            when (throwable) {
                is ServerException -> showMessage(throwable.message.toString())
                is ConnectException -> showMessage(R.string.connection_exception)
                is AuthenticationException -> {
                    logout()
                }
                is ApplicationException -> {
                    showMessage(throwable.toString())
                }
                is SocketTimeoutException -> showMessage(R.string.socket_time_out_exception)
                else -> showMessage(getString(R.string.other_exception) + throwable.message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun logout() {
        // Write logout code
    }

    protected abstract fun inject(fragmentComponent: FragmentComponent)
    /**
     * This method is used for binding view with your binding
     */
    protected abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?,attachToRoot: Boolean): T

    protected abstract fun bindData()
}
