package com.educacionit.gotorute.utils.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.educacionit.gotorute.R

object NotificationUtils {
    private var notificationManager: NotificationManager? = null

    fun showNotification(
        context: Context?,
        title: String,
        contentMessage: String,
        notificationType: NotificationType
    ) {
        notificationManager = notificationManager
            ?: context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        context?.let { safeContext ->
            val notification =
                buildNotification(safeContext, title, contentMessage, notificationType)
            notificationManager?.notify(notificationType.notificationId, notification)
        }
    }

    private fun buildNotification(
        context: Context,
        title: String,
        contentMessage: String,
        notificationType: NotificationType
    ): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationType)
        }

        return NotificationCompat.Builder(context, notificationType.channelId)
            .setContentTitle(title)
            .setContentText(contentMessage)
            .setSmallIcon(R.drawable.map_arrow_square)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.educacionit_logo
                )
            )
            .setAutoCancel(true)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationType: NotificationType) {
        val channel = NotificationChannel(
            notificationType.channelId,
            notificationType.channelName,
            notificationType.importance,
        )
        notificationManager?.createNotificationChannel(channel)
    }
}