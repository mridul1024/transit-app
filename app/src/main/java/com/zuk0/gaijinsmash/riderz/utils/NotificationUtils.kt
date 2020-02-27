package com.zuk0.gaijinsmash.riderz.utils

import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.zuk0.gaijinsmash.riderz.R

object NotificationUtils {

    private const val HEADS_UP_CHANNEL = "HEADS_UP_CHANNEL"

    fun createNotificationChannel(context: Context, title: String, content: String) {
        val builder = NotificationCompat.Builder(context, HEADS_UP_CHANNEL)
                .setSmallIcon(R.drawable.ic_train_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_MAX)
    }

    fun notifyTrainUpdate() {

    }

    fun notifyAdvisory() {

    }
}