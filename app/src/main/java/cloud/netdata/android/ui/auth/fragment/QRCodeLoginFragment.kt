package cloud.netdata.android.ui.auth.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import cloud.netdata.android.data.pojo.request.APIRequest
import cloud.netdata.android.databinding.AuthFragmentQrCodeLoginBinding
import cloud.netdata.android.di.component.FragmentComponent
import cloud.netdata.android.ui.base.BaseFragment
import cloud.netdata.android.ui.home.fragment.ChooseSpaceFragment
import cloud.netdata.android.utils.Constant
import cloud.netdata.android.utils.customapi.ApiViewModel
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback

class QRCodeLoginFragment : BaseFragment<AuthFragmentQrCodeLoginBinding>() {

    private val apiViewModel by lazy {
        ViewModelProvider(this)[ApiViewModel::class.java]
    }

    private lateinit var codeScanner: CodeScanner
    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ): AuthFragmentQrCodeLoginBinding {
        return AuthFragmentQrCodeLoginBinding.inflate(inflater, container, attachToRoot)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLinkDevice()
    }

    override fun bindData() {
        codeScanner = CodeScanner(requireActivity(), binding.preViewQRCode)
        checkPermission()
        binding.imageViewClose.setOnClickListener {
            navigator.goBack()
        }
    }

    private fun checkPermission () {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Camera permission is already granted, proceed with camera operations
            startScan()
        } else {
            // Request camera permission
            requestCameraPermission()
        }
    }
    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, proceed with camera operations
                startScan()
            } else {
                // Camera permission denied, handle accordingly (e.g., show a message or disable camera functionality)
            }
        }
    }

    private fun startScan(){
        codeScanner.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                session.userSession = it.text
                Constant.TOKEN = session.userSession
                session.getFirebaseDeviceId { deviceId ->
                    session.deviceId = deviceId
                    callLinkDevice()
                }
            }
        }
        codeScanner.startPreview()
    }

    private fun callLinkDevice(){
        showLoader()
        apiViewModel.callLinkDevice(APIRequest( token = session.deviceId))
    }

    private fun observeLinkDevice(){
        apiViewModel.linkDeviceLiveData.observe(this){
            hideLoader()
            if(!it.isError){
                if(it.responseCode == 200){
                    appPreferences.putBoolean(Constant.APP_PREF_IS_LOGIN, true)
                    navigator.load(ChooseSpaceFragment::class.java).replace(false)
                } else {
                    showMessage("Invalid QR code")
                    startScan()
                }
            } else {
                showMessage("Something wrong! Try again")
                startScan()
            }
        }
    }

    /*private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.preViewQRCode.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (e: Exception) {
                // Handle camera initialization error
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }*/

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}