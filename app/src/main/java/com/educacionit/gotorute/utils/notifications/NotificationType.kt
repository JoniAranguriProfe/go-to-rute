package com.educacionit.gotorute.utils.notifications

import android.app.NotificationManager

sealed class NotificationType(
    val channelName: String,
    val channelId: String,
    val importance: Int,
    val notificationId: Int
)

data object NotificationOutOfRoute : NotificationType(
    channelName = "Notifications out of route",
    channelId = "1",
    importance = NotificationManager.IMPORTANCE_HIGH,
    notificationId = 1

)

data object NotificationBatteryLow : NotificationType(
    channelName = "Notifications battery low",
    channelId = "2",
    importance = NotificationManager.IMPORTANCE_HIGH,
    notificationId = 2
)

data object NotificationAlarmContinue : NotificationType(
    channelName = "Notifications alarm continue with route",
    channelId = "3",
    importance = NotificationManager.IMPORTANCE_HIGH,
    notificationId = 3
)

