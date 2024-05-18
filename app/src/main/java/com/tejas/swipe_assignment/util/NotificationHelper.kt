package com.tejas.swipe_assignment.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tejas.swipe_assignment.R
import com.tejas.swipe_assignment.activities.MainActivity

object NotificationHelper {


    const val NOTIFICATION_CHANNEL_ID = "product_notification_channel_id"
    const val NOTIFICATION_CHANNEL_NAME = "product_notification_channel_name"
}