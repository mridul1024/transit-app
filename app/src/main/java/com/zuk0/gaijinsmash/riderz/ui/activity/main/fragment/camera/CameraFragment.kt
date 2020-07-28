package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.camera

import android.Manifest
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.zuk0.gaijinsmash.riderz.databinding.FragmentCameraBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class CameraFragment : BaseFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var vm: CameraViewModel
    private lateinit var binding: FragmentCameraBinding
    private lateinit var broadcastManager: LocalBroadcastManager

    var imageCapture: ImageCapture? = null
    var imageAnalyzer: ImageAnalysis? = null
    lateinit var cameraSelector: CameraSelector

    private lateinit var preview: Preview
    private var camera: Camera? = null

    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    /**
     * We need a display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override config change in manifest or for 180-degree
     * orientation changes.
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) = view?.let { view ->
            if (displayId == vm.displayId) {
                Log.d(TAG, "Rotation changed: ${view.display.rotation}")
                imageCapture?.targetRotation = view.display.rotation
                imageAnalyzer?.targetRotation = view.display.rotation
            }
        } ?: Unit
    }

    /** Volume down button receiver used to trigger shutter */
    private val volumeDownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(KEY_EVENT_EXTRA, KeyEvent.KEYCODE_UNKNOWN)) {
                // When the volume down button is pressed, simulate a shutter button click
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    takePicture()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this, vmFactory).get(CameraViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCameraBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize our background executor
        vm.cameraExecutor = Executors.newSingleThreadExecutor()
        broadcastManager = LocalBroadcastManager.getInstance(view.context)
        // Set up the intent filter that will receive events from our main activity
        val filter = IntentFilter().apply { addAction(KEY_EVENT_ACTION) }
        broadcastManager.registerReceiver(volumeDownReceiver, filter)

        // Every time the orientation of device changes, update rotation for use cases
        displayManager.registerDisplayListener(displayListener, null)

        // Wait for the views to be properly laid out
        binding.previewView.post {

            // Keep track of the display in which this view is attached
            vm.displayId = binding.previewView.display.displayId

            // Build UI controls
            updateCameraUi()

            // Set up the camera and its use cases
            setupCamera()
        }
    }

    override fun onStart() {
        super.onStart()
        startCamera()
    }

    override fun onResume() {
        super.onResume()
        super.collapseAppBar(activity)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
          // Unregister the broadcast receivers and listeners
        broadcastManager.unregisterReceiver(volumeDownReceiver)
        displayManager.unregisterDisplayListener(displayListener)
        super.onDestroyView()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSIONS) {
            if (vm.isCameraPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(requireContext(),
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show()
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    fun updateCameraUi() {

        if(vm.hasBackCamera()) {
            binding.cameraSwitchBtnFront.visibility = View.VISIBLE
            binding.cameraSwitchBtnBack.visibility = View.GONE

        } else if(vm.hasFrontCamera()) {
            binding.cameraSwitchBtnFront.visibility = View.GONE
            binding.cameraSwitchBtnBack.visibility = View.VISIBLE
        }
    }

    fun setupCamera() {
        binding.cameraLibrary.setOnClickListener {
            //todo
        }
        binding.cameraShutterBtn.setOnClickListener {
            takePicture()
        }
        binding.cameraSwitchBtnBack.setOnClickListener {
            updateCameraUi()
        }
        binding.cameraSwitchBtnFront.setOnClickListener {
            updateCameraUi()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = context?.let { ProcessCameraProvider.getInstance(it) }

        cameraProviderFuture?.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            vm.cameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder()
                    .build()
            // ImageCapture
            imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    // We request aspect ratio but no resolution to match preview config, but letting
                    // CameraX optimize for whatever specific resolution best fits our use cases
                    //.setTargetAspectRatio(screenAspectRatio)
                    // Set initial target rotation, we will have to call this again if rotation changes
                    // during the lifecycle of this use case
                    //.setTargetRotation(rotation)
                    .build()

            // Select back camera
            cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                // Unbind use cases before rebinding
                vm.cameraProvider?.unbindAll()

                // Bind use cases to camera
                camera = vm.cameraProvider?.bindToLifecycle(
                        this, cameraSelector, preview, imageCapture)
                preview.setSurfaceProvider(binding.previewView.createSurfaceProvider())
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePicture() {

        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create timestamped output file to hold the image
        val photoFile = vm.createImageFile()

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Setup image capture listener which is triggered after photo has
        // been taken
        imageCapture.takePicture(
                outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $savedUri"
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)
            }
        })
    }

    companion object {
        private const val TAG = "CameraFragment"
        private const val REQUEST_CODE_CAMERA_PERMISSIONS = 5111
        const val KEY_EVENT_EXTRA = "KEY_EVENT_EXTRA"
        const val KEY_EVENT_ACTION = "KEY_EVENT_ACTION"
    }
}