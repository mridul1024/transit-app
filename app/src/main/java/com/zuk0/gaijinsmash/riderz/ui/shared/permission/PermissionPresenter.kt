package com.zuk0.gaijinsmash.riderz.ui.shared.permission

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.zuk0.gaijinsmash.riderz.databinding.PermissionLayoutBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.home.HomeViewModel
import com.zuk0.gaijinsmash.riderz.data.local.manager.LocationManager

class PermissionPresenter(var activity: Activity?, val vm: HomeViewModel) {

    private val inflater: LayoutInflater
    private var binding: PermissionLayoutBinding

    init {
        Log.d(TAG, "init $TAG")
        inflater = LayoutInflater.from(activity)
        binding = PermissionLayoutBinding.inflate(inflater)
    }

    fun showDialog() {
        binding.homePermissionContainer.visibility = View.VISIBLE
        binding.enablePermissionButton.setOnClickListener { v ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity?.packageName))
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity?.startActivityForResult(intent, LocationManager.LOCATION_PERMISSION_REQUEST_CODE)
        }
        binding.disablePermissionButton.setOnClickListener {
            binding.homePermissionContainer.visibility = View.GONE
        }
    }

    fun closeDialog() {
        //todo hide visibility
        binding.homePermissionContainer.visibility = View.GONE
        activity = null
    }

    companion object {
        private const val TAG = "PermissionPresenter"
    }
}
