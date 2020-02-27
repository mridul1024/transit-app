package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.phone_lines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zuk0.gaijinsmash.riderz.R

class PhoneLinesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_phone_lines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {}
}