package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.phone_lines

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.databinding.FragmentPhoneLinesBinding
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment
import javax.inject.Inject

class PhoneLinesFragment : BaseFragment() {

    private lateinit var binding: FragmentPhoneLinesBinding

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var vm: PhoneLinesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this, vmFactory).get(PhoneLinesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPhoneLinesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initButtons()
        super.collapseAppBar(activity)
        super.setTitle(activity, getString(R.string.phoneLines_title))
    }

    private fun initButtons() {
        binding.bartPoliceButton.setOnClickListener {
            val number = getString(R.string.phoneLines_police_number)
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${vm.formatTelephoneNumber(number)}"))
            startActivity(intent)
        }
        binding.bartComplaintsButton.setOnClickListener {
            val number = getString(R.string.phoneLines_bartComplaints_number)
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${vm.formatTelephoneNumber(number)}"))
            startActivity(intent)
        }
        binding.lostAndFoundButton.setOnClickListener {
            val number = getString(R.string.phoneLines_bartLostFound_number)
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${vm.formatTelephoneNumber(number)}"))
            startActivity(intent)
        }
    }
}