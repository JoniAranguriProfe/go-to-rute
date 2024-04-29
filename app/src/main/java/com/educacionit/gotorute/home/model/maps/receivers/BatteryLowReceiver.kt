package com.educacionit.gotorute.home.model.maps.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BATTERY_LOW
import com.educacionit.gotorute.utils.notifications.NotificationBatteryLow
import com.educacionit.gotorute.utils.notifications.NotificationUtils

class BatteryLowReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val contentMessage = if (intent?.action == ACTION_BATTERY_LOW) {
            "Tu dispositivo se está quedando sin bateria, asegurate de llegar a tu destino antes que se apague"
        } else {
            "Llegó otro tipo de action: ${intent?.action}"
        }

        NotificationUtils.showNotification(
            context,
            "Battery low!",
            contentMessage,
            NotificationBatteryLow
        )
    }
}