package cloud.netdata.android.ui.home.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.*
import cloud.netdata.android.databinding.HomeDetailsFragmentBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class HomeDetailsFragment: BaseFragment<HomeDetailsFragmentBinding>() {

    /*private val timeValuesAdapter by lazy {
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
    }*/

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): HomeDetailsFragmentBinding {
        return HomeDetailsFragmentBinding.inflate(inflater,container,attachToRoot)
    }

    override fun bindData() {
        toolbar()
        manageClick()
        loadWebview()
    }

    private fun toolbar() = with(binding){
        includeToolbar.imageViewBack.setOnClickListener { navigator.goBack() }
        includeToolbar.textViewSpace.visible()
        includeToolbar.textViewSpace.text = appPreferences.getString(Constant.APP_PREF_SPACE_NAME)
    }

    private fun manageClick() = with(binding){

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebview() = with(binding){
        showLoader()
        /*val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webview, true)*/

        val sessionId = "s_i=${Constant.COOKIE_SI}"
        val token = "s_v_${Constant.COOKIE_SI}=${Constant.COOKIE_SV}"
        val cookieValue = "$sessionId;$token"
        /*val domain = "https://app.netdata.cloud/"

        cookieManager.setCookie(domain, cookieValue)
        CookieManager.getInstance().flush()*/

        webview.settings.javaScriptEnabled = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.useWideViewPort = true
        webview.settings.builtInZoomControls = true
//        webview.webChromeClient = WebChromeClient()
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true

        // Set a WebViewClient to handle events inside the WebView
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Page has finished loading
                hideLoader()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Page has started loading
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // Handle URL loading within WebView, return true to indicate it's handled here
                view?.loadUrl(url!!)
                return true
            }
        }

        val forwardedUrl = arguments?.getString(Constant.BUNDLE_URL)!!

        val client = OkHttpClient()
        val myURL = URL(forwardedUrl)
        val request = Request.Builder()
            .url(myURL)
            .addHeader("Cookie", cookieValue)
            .build()

        GlobalScope.launch (Dispatchers.IO) {
                try {
                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        launch(Dispatchers.Main) {
                            webview.loadDataWithBaseURL(myURL.toString(), responseData!!, "text/html", "UTF-8", null)
                        }
                    } else {
                        // Handle unsuccessful response
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        // Load a URL into the WebView
        /*Log.e("url", arguments?.getString(Constant.BUNDLE_URL)!!)
        webview.loadUrl(arguments?.getString(Constant.BUNDLE_URL)!!)*/
    }

   /* @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() = with(binding){
        recyclerViewTimeValues.adapter = timeValuesAdapter
        recyclerViewAlertInfo.adapter = alertInfoAdapter
        recyclerViewConfiguration.adapter = configurationAdapter
        recyclerViewSeverityConditions.adapter = severityConditionsAdapter
        recyclerViewInstanceValues.adapter = instanceValuesAdapter

        addData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addData(){
        timeValuesAdapter.list.clear()
        timeValuesAdapter.list.add(TimeValuesList("Warning", "65.65 mb/s • 32.1 %", "Sat Nov 10 2021, 12:33:50 EET"))
        timeValuesAdapter.list.add(TimeValuesList("Warning", "65.65 mb/s • 32.1 %", "Sat Nov 10 2021, 12:33:50 EET"))
        timeValuesAdapter.notifyDataSetChanged()

        alertInfoAdapter.list.add(AlertInfoList(R.drawable.ic_alert_info_node, "Node:", "node.name12thisisanode"))
        alertInfoAdapter.list.add(AlertInfoList(R.drawable.ic_alert_info_chart_id, "Chart ID:", "net.eth0"))
        alertInfoAdapter.list.add(AlertInfoList(R.drawable.ic_alert_info_type, "Type:", "System"))
        alertInfoAdapter.list.add(AlertInfoList(R.drawable.ic_alert_info_hostname, "Hostname:", "pi3"))
        alertInfoAdapter.list.add(AlertInfoList(R.drawable.ic_alert_info_component, "Component:", "Network"))
        alertInfoAdapter.list.add(AlertInfoList(R.drawable.ic_alert_info_family, "Family:", "Unknown"))
        alertInfoAdapter.list.add(AlertInfoList(R.drawable.ic_alert_info_class, "Class:", "Workload"))
        alertInfoAdapter.list.add(AlertInfoList(R.drawable.ic_alert_info_event_id, "Event ID:", "asdasd908mnk987653o"))
        alertInfoAdapter.notifyDataSetChanged()

        configurationAdapter.list.add(ConfigurationList("DB LOOKUP", "Average of all values of dimension received, of chart net.eth0, starting 1 min ago and up to now, with options absolute unaligned"))
        configurationAdapter.list.add(ConfigurationList("CALCULATION", "(\$interface_speed > 0) ? (\$this*100 / (\$interface_speed)) : (nan)"))
        configurationAdapter.notifyDataSetChanged()

        severityConditionsAdapter.list.add(SeverityConditionsList(R.color.colorYellowFF, "Warning when:", "\$this > ((\$status >= \$WARNING) ? (85) : (90))", "More <b>warning</b> details about <b>\$this</b> command"))
        severityConditionsAdapter.list.add(SeverityConditionsList(R.color.colorRedFF, "Critical when:", "\$this > ((\$status >= \$WARNING) ? (85) : (90))", "More critical details about \$this command"))
        severityConditionsAdapter.notifyDataSetChanged()

        instanceValuesAdapter.list.add(InstanceValuesList("parentB/rpi2b-1", "Warning", "35.5%", "Sat 10, November 2021 • 12:33:09"))
        instanceValuesAdapter.list.add(InstanceValuesList("parentA/rpi2b-1", "Warning", "34.0%", "Sat 10, November 2021 • 12:00:04"))
        instanceValuesAdapter.list.add(InstanceValuesList("rpi2b-1", "Warning", "43.2%", "Sat 10, November 2021 • 11:53:02"))
        instanceValuesAdapter.notifyDataSetChanged()
    }*/

    /*private fun setLineChartData() = with(binding){
        val xValue = ArrayList<String>()
        xValue.add("1")
        xValue.add("2")
        xValue.add("3")
        xValue.add("4")

        val lineEntry = ArrayList<Entry>()

        lineEntry.add(Entry(15F, 0))
        lineEntry.add(Entry(30F, 1))
        lineEntry.add(Entry(15F, 2))
        lineEntry.add(Entry(25F, 3))

        val lineDataSet = LineDataSet(lineEntry, "")

        lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.colorGreen68)
        lineDataSet.circleRadius = 0F
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ContextCompat.getColor(requireContext(), R.color.colorGreen68)
        lineDataSet.fillAlpha = 50

        val data = LineData(xValue, lineDataSet)
        lineChart.data = data
        lineChart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBlack1C))
        lineChart.animateXY(1000,1000)

    }*/
}