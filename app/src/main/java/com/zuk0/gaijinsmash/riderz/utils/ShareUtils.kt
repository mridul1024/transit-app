package com.zuk0.gaijinsmash.riderz.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

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
    fun shareTrip(context: Context?, bitmap: Bitmap?) {
        if(context == null || bitmap == null)
            return

        try {
            //todo create file provider
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.jpeg")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            stream.close()
            val uri = FileProvider.getUriForFile(context, "com.riderz.provider", file)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            context.startActivity(Intent.createChooser(intent, "Share Trip"))
            Logger.i("share intent : $intent")
        } catch(e: Exception) {
            CrashLogUtil.logException(e)
        }
    }

    /**
     * Create an implicit intent and share trip
     * @param context
     * @param view
     */
    fun shareTrip(context: Context?, view: View?) {
        if(context == null || view == null)
            return

        try {
            //todo change scope
            val bitmap =  convertViewToBitmap(view)
            val file = createTempFile()
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            stream.close()
            val uri = Uri.fromFile(file)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/jpeg"
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            context.startActivity(Intent.createChooser(intent, "Share Trip"))
            Logger.i("share intent : $intent")
        } catch(e: Exception) {
            CrashLogUtil.logException(e)
        }
    }


    /**
     * Capture a layout/view and return
     * a bitmap version of it
     * @param view
     */
    fun convertViewToBitmap(view: View) : Bitmap {
/*
        val dm = view.context.resources.displayMetrics
        view.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
*/

        val b = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        val drawable = view.background
        drawable.draw(c)
        view.layout(view.left, view.top, view.bottom, view.right)
        view.draw(c)
        return b
    }
}