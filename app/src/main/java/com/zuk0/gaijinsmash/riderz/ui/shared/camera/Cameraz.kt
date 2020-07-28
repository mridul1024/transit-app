package com.zuk0.gaijinsmash.riderz.ui.shared.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.icu.text.SimpleDateFormat
import android.media.Image
import android.util.Log
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.nio.ByteBuffer
import java.nio.file.Files.createFile
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Cameraz(var previewView: PreviewView?, lifecycleOwner: LifecycleOwner) : LifecycleOwner, LifecycleObserver {

    private lateinit var imageCapture: ImageCapture
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(lifecycleOwner)
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var imagePreview: Preview

    private lateinit var cameraControl: CameraControl
    private lateinit var imageAnalysis: ImageAnalysis

    private lateinit var cameraInfo: CameraInfo
    private val executor = Executors.newSingleThreadExecutor()

    init {
        Log.d(TAG, "init")
    }

    fun startCamera(context: Context?) {

        imagePreview = Preview.Builder().apply {
            setTargetAspectRatio(AspectRatio.RATIO_16_9)
            previewView?.display?.rotation?.let { setTargetRotation(it) }
        }.build()

        imageAnalysis = ImageAnalysis.Builder().apply {
            setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        }.build()
        imageAnalysis.setAnalyzer(executor, LuminosityAnalyzer())

        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            val camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    imagePreview,
                    imageAnalysis,
                    imageCapture)
            cameraControl = camera.cameraControl
            cameraInfo = camera.cameraInfo

            previewView?.preferredImplementationMode = PreviewView.ImplementationMode.TEXTURE_VIEW
            imagePreview.setSurfaceProvider(previewView?.createSurfaceProvider())
        }, ContextCompat.getMainExecutor(previewView?.context))
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d(TAG, "onCreate")

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        previewView?.context?.let {
            cameraProviderFuture = ProcessCameraProvider.getInstance(it)

            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            cameraProviderFuture.addListener(Runnable {
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.bindToLifecycle(this, cameraSelector)
            }, ContextCompat.getMainExecutor(it))

            if (isAllPermissionsGranted()) {
                Log.d(TAG, "permissions granted")

                previewView?.post { startCamera(it) }
            } else {
                Log.d(TAG, "permissions denied")
                /*requestPermissions(
                        REQUIRED_PERMISSIONS,
                        REQUEST_CODE_PERMISSIONS
                )*/
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.d(TAG, "onResume")
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {

        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun captureImage() {

    }

    fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
     /*   if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                previewView.post { startCamera() }
            } else {
                finish()
            }
        }*/
    }

    fun isAllPermissionsGranted() : Boolean {
        //todo return false
        previewView?.context?.let {
            return ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    private fun toggleTorch() {
        if (cameraInfo.torchState.value == TorchState.ON) {
            cameraControl.enableTorch(false)
        } else {
            cameraControl.enableTorch(true)
        }
    }

    private fun takePicture() {
        imageCapture.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
            @SuppressLint("UnsafeExperimentalUsageError")
            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                imageProxy.image?.let {
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    val bitmap = it.toBitmap(rotationDegrees)
                    // ...
                    super.onCaptureSuccess(imageProxy)
                }
            }
            override fun onError(exception: ImageCaptureException) {
                val msg = "Photo capture failed: ${exception.message}"
                previewView?.post {
                    Toast.makeText(previewView?.context, msg, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
/*

    private fun takePicture() {
        val file = createFile(
                outputDirectory,
                FILENAME,
                PHOTO_EXTENSION
        )
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(outputFileOptions, executor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val msg = "Photo capture succeeded: ${file.absolutePath}"
                previewView.post {
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                val msg = "Photo capture failed: ${exception.message}"
                previewView.post {
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
*/

    private fun Image.toBitmap(rotationDegrees: Int): Bitmap {
        val buffer = planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
        val matrix = Matrix()
        matrix.postRotate(rotationDegrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }


    companion object {
        private const val TAG = "Cameraz"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, "Cameraz").apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())mediaDir else appContext.filesDir
        }

        fun createFile(baseFolder: File, format: String, extension: String) =
                File(baseFolder, java.text.SimpleDateFormat(format, Locale.US)
                        .format(System.currentTimeMillis()) + extension)
    }

    private class LuminosityAnalyzer : ImageAnalysis.Analyzer {
        private var lastAnalyzedTimestamp = 0L

        /**
         * Helper extension function used to extract a byte array from an
         * image plane buffer
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {
            val currentTimestamp = System.currentTimeMillis()
            // Calculate the average luma no more often than every second
            if (currentTimestamp - lastAnalyzedTimestamp >=
                    TimeUnit.SECONDS.toMillis(1)) {
                // Since format in ImageAnalysis is YUV, image.planes[0]
                // contains the Y (luminance) plane
                val buffer = image.planes[0].buffer
                // Extract image data from callback object
                val data = buffer.toByteArray()
                // Convert the data into an array of pixel values
                val pixels = data.map { it.toInt() and 0xFF }
                // Compute average luminance for the image
                val luma = pixels.average()
                // Log the new luma value
                Log.d("CameraXApp", "Average luminosity: $luma")
                // Update timestamp of last analyzed frame
                lastAnalyzedTimestamp = currentTimestamp
            }
            image.close()
        }
    }
}