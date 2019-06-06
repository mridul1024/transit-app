package com.zuk0.gaijinsmash.riderz.ui.activity.main.fragment.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.zuk0.gaijinsmash.riderz.R

class NewsFragment : Fragment() {

    companion object {
        const val TAG = "NewsFragment"
        const val URL = "https://www.bart.gov/news/articles/2018/news20181129-0"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWebView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val appBarLayout: AppBarLayout? = activity?.findViewById(R.id.main_app_bar_layout)
        appBarLayout?.setExpanded(false)
    }

    private fun initWebView() {
        val webView : WebView? = activity?.findViewById(R.id.home_news_webView)
        webView?.loadUrl(URL)
        webView?.webViewClient = WebViewClient()
    }

    private fun getBundle() {

    }
}