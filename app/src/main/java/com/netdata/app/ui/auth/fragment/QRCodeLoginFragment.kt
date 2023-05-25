package com.netdata.app.ui.auth.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.netdata.app.databinding.AuthFragmentLoginBinding
import com.netdata.app.databinding.AuthFragmentQrCodeLoginBinding
import com.netdata.app.di.component.FragmentComponent
import com.netdata.app.ui.auth.viewmodel.LoginViewModel
import com.netdata.app.ui.base.BaseFragment
import com.netdata.app.utils.Validator
import javax.inject.Inject

class QRCodeLoginFragment : BaseFragment<AuthFragmentQrCodeLoginBinding>() {

    private val viewModel by lazy {
        ViewModelProvider(this,
            viewModelFactory)[LoginViewModel::class.java]
    }
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

    }

    override fun bindData() {
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
            startCamera()
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
                startCamera()
            } else {
                // Camera permission denied, handle accordingly (e.g., show a message or disable camera functionality)
            }
        }
    }

    private fun startCamera() {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}