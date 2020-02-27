package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.bottom_sheet

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.data.local.entity.event.EventToggleMap
import com.zuk0.gaijinsmash.riderz.databinding.BottomSheetLayoutBinding
import org.greenrobot.eventbus.EventBus


class ActionBottomDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: BottomSheetLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BottomSheetLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun initButtons() {
        binding.cameraBtn.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_actionBottomDialogFragment_to_cameraFragment)
            dismiss()
        }
        binding.mapBtn.setOnClickListener {
            toggleMapFab(it) //todo use event bus?
            dismiss()
        }
        binding.reportBtn.setOnClickListener {
           NavHostFragment.findNavController(this).navigate(R.id.action_actionBottomDialogFragment_to_reportFragment)
            dismiss()

        }
    }

    override fun onClick(v: View?) {
        dismiss()
    }

    private fun toggleMapFab(view: View) {
        EventBus.getDefault().post(EventToggleMap(TAG))
    }

    companion object {
        const val TAG = "ActionBottomSheet"
    }
}