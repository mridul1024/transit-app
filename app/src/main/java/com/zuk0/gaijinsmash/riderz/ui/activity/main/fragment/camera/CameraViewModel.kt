package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.camera

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.lifecycle.ViewModel
import java.io.File

class CameraViewModel : ViewModel() {

    lateinit var outputDirectory: File
    var displayId: Int = -1
    var lensFacing: Int = -1//CameraSelector.LENS_FACING_BACK
    var preview: Preview? = null
    var imageCapture: ImageCapture? = null
    var imageAnalyzer: ImageAnalysis? = null

    companion object {
        private const val TAG = "CameraViewModel"
    }
}