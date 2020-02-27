package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.camera

import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Camera
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.camera.core.*
import androidx.camera.core.impl.PreviewConfig
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.FragmentCameraBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.MainActivity
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.permissions.PermissionFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CameraFragment : BaseFragment() {

    private lateinit var mainExecutor: Executor
    private lateinit var analysisExecutor: Executor

    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var displayManager: DisplayManager
    private lateinit var binding: FragmentCameraBinding
    private lateinit var vm: CameraViewModel

    private var camera: Camera? = null

    // Volume down button receiver used to trigger shutter
    private val volumeDownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(KEY_EVENT_EXTRA, KeyEvent.KEYCODE_UNKNOWN)) {
                // When the volume down button is pressed, simulate a shutter button click
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    val shutter = binding.cameraShutterBtn
                    shutter.performClick()
                }
            }
        }
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
                vm.imageCapture?.setTargetRotation(view.display.rotation)
                vm.imageAnalyzer?.setTargetRotation(view.display.rotation)
            }
        } ?: Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainExecutor = ContextCompat.getMainExecutor(requireContext())
        analysisExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCameraBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        broadcastManager = LocalBroadcastManager.getInstance(view.context)

        // Set up the intent filter that will receive events from our main activity
        val filter = IntentFilter().apply { addAction(KEY_EVENT_ACTION) }
        broadcastManager.registerReceiver(volumeDownReceiver, filter)

        // Every time the orientation of device changes, recompute layout
        displayManager = binding.viewFinder.context
                .getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        displayManager.registerDisplayListener(displayListener, null)

        // Determine the output directory
       //todo vm.outputDirectory = activity.getOutputDirectory(requireContext())

        // Wait for the views to be properly laid out
        binding.viewFinder.post {

            // Keep track of the display in which this view is attached
            vm.displayId = binding.viewFinder.display.displayId

            // Build UI controls
            updateCameraUi()

            // Bind use cases
            bindCameraUseCases()

            // In the background, load latest photo taken (if any) for gallery thumbnail
            lifecycleScope.launch(Dispatchers.IO) {
                vm.outputDirectory.listFiles { file ->
                    EXTENSION_WHITELIST.contains(file.extension.toUpperCase(Locale.ROOT))
                }?.max()?.let {
                    setGalleryThumbnail(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    fun startCamera() {
     /*   // Create configuration object for the viewfinder use case
        val previewConfig = PreviewConfig.Builder().apply {
            setTargetResolution(Size(640, 480))
        }.build()


        // Build the viewfinder use case
        val preview = Preview(previewConfig)

        // Every time the viewfinder is updated, recompute layout
        preview.setOnPreviewOutputUpdateListener {

            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = binding.viewFinder.parent as ViewGroup
            parent.removeView(binding.viewFinder)
            parent.addView(binding.viewFinder, 0)

            binding.viewFinder.surfaceTexture = it.surfaceTexture
            updateTransform()
        }

        // Bind use cases to lifecycle
        // If Android Studio complains about "this" being not a LifecycleOwner
        // try rebuilding the project or updating the appcompat dependency to
        // version 1.1.0 or higher.
        CameraX.bindToLifecycle(this, preview)*/
    }

    fun updateTransform() {
//
    }

    fun updateCameraUi() {

    }

    fun bindCameraUseCases() {

    }

    fun setGalleryThumbnail(file: File) {

    }

    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
     /*   if (!PermissionFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(
                    //CameraFragmentDirections.actionCameraToPermissions()
            )
        }*/
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        // Unregister the broadcast receivers and listeners
        broadcastManager.unregisterReceiver(volumeDownReceiver)
        displayManager.unregisterDisplayListener(displayListener)
        super.onDestroyView()
    }

    private fun initCamera() {

    }

    private fun takePicture() {

    }

    private fun savePicture() {

    }

    companion object {
        const val TAG = "CameraFragment"
        const val KEY_EVENT_EXTRA = "KEY_EVENT_EXTRA"
        const val KEY_EVENT_ACTION = "KEY_EVENT_ACTION"
        // This is an arbitrary number we are using to keep track of the permission
        // request. Where an app has multiple context for requesting permission,
        // this can help differentiate the different contexts.
        private const val REQUEST_CODE_PERMISSIONS = 10

        // This is an array of all the permission specified in the manifest.
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    }
}