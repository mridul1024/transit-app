package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.help

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.zuk0.gaijinsmash.riderz.R
import com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.BaseFragment

class HelpFragment : BaseFragment() {
    private var mInflatedView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mInflatedView = inflater.inflate(R.layout.fragment_help, container, false)
        return mInflatedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mButton = mInflatedView?.findViewById<Button>(R.id.help_report_button)
        mButton?.setOnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("message/rfc822")
                    .putExtra(Intent.EXTRA_EMAIL, arrayOf("zuk0.hack@gmail.com"))
                    .putExtra(Intent.EXTRA_SUBJECT, "***RIDERZ BUG REPORT***")
                    .putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.help_intent_text))
            startActivity(Intent.createChooser(intent, resources.getString(R.string.help_intent_title)))
        }
        super.collapseAppBar(activity)
        super.setTitle(activity, getString(R.string.help_title))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
    }
}