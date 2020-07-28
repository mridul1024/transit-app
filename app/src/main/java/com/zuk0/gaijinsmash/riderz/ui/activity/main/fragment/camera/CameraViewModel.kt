package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.camera

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class CameraViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    var cameraProvider: ProcessCameraProvider? = null

    lateinit var outputDirectory: File
    var displayId: Int = -1
    var lensFacing: Int = CameraSelector.LENS_FACING_BACK

    /** Blocking camera operations are performed using this executor */
    lateinit var cameraExecutor: ExecutorService

    fun isCameraPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun checkCameraHardware(context: Context?): Boolean {
        return context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA) == true
    }

    fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    fun createImageFile(): File {
        return File(
                createTempDir(),
                SimpleDateFormat(FILENAME_FORMAT, Locale.US
                ).format(System.currentTimeMillis()) + ".jpg")
    }

/*    fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }*/


    fun savePicture() {

    }


    override fun onCleared() {
         // Shut down our background executor
        cameraExecutor.shutdown()
        super.onCleared()
    }

    companion object {
        private const val TAG = "CameraViewModel"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}