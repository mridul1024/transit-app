package com.zuk0.gaijinsmash.riderz.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.orhanobut.logger.Logger

object ShareUtils {

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        type = "text/plain"
    }

    fun shareText(text: String, context: Context) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text.trim())
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        context.startActivity(shareIntent)
        Logger.i("share intent : $shareIntent")
    }

    /**
     * Create an implicit intent and share trip
     * @param context
     * @param view
     */
    fun shareTrip(context: Context, view: View) {
        //capture trip view - print
        // create jpg
        // send
        val bitmap = convertViewToBitmap(view)
        val intent = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, bitmap)
            type = "image/jpeg"

            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, "Example")
        context.startActivity(intent)
        Logger.i("share intent : $intent")
    }


    /**
     * Capture a layout/view and return
     * a bitmap version of it
     * @param view
     */
    fun convertViewToBitmap(view: View) : Bitmap {
        val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(measureSpec, measureSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val b = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight,
        Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        c.translate(((-view.scrollX).toFloat()), (-view.scrollY).toFloat())
        view.draw(c)
        return b
    }
}