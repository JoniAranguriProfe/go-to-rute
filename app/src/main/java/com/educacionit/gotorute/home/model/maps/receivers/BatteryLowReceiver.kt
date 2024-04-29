package com.educacionit.gotorute.home.model.maps.receivers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BATTERY_LOW
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.educacionit.gotorute.R
import com.educacionit.gotorute.home.model.maps.services.RouteCheckService

class BatteryLowReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManager? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        println("RECEIVER: ${context.toString()}, ${intent.toString()}")
        val contentMessage = if (intent?.action == ACTION_BATTERY_LOW) {
            "Tu dispositivo se está quedando sin bateria, asegurate de llegar a tu destino antes que se apague"
        } else {
            "Llegó otro tipo de action: ${intent?.action}"
        }

        notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        showNotification(contentMessage, context)
    }

    private fun showNotification(contentMessage: String, context: Context?) {
        context?.let { safeContext ->
            val notification = buildNotification(contentMessage, safeContext)
            notificationManager?.notify(RouteCheckService.NOTIFICATION_ID, notification)
        }
    }

    private fun buildNotification(contentMessage: String, context: Context): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        return NotificationCompat.Builder(context, RouteCheckService.CHANNEL_ID)
            .setContentTitle("Batery low!")
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
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            RouteCheckService.CHANNEL_ID,
            "Notifications out of route",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager?.createNotificationChannel(channel)
    }
}