package com.zuko.searchviewz.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.SearchView

class SearchViewz(context: Context) : SearchView(context) {

    // search view bar
    // behavior

    lateinit var closeIcon: Drawable // defaults
    lateinit var searchIcon: Drawable // defaults

    lateinit var searchView: SearchView


    init {

    }


    fun initOnTouchEventListeners() {

    }

    fun initIconListeners() {

    }

    /**
     * Force the search view to open and gain focus
     */
    fun open() {

    }

    /**
     * Force the search view to close
     */
    fun close() {

    }

    fun hide() {

    }

    fun setTransition() {

    }

    fun searchSuggestions() {

    }

    fun voiceSearch() {

    }
    companion object {
        const val TAG = "SearchViewz"
    }
}