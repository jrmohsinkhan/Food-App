package com.example.foodapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.apiService.NotificationApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.remoteMessage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

const val CHANNEL_ID = "notification_channel"
const val CHANNEL_NAME = "GeneralNotification"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val serviceScope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO)

    // generate the notification
    // attach the notificaiton
    // show the notificaiton

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token1:", token)

        // Send token to server in background
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        // Use a proper CoroutineScope
        serviceScope.launch {
            try {
                val response = NotificationApi.sendFcmToken(token)
                if (response.success) {
                    Log.d("FCM", "Token sent successfully")
                } else {
                    Log.e("FCM", "Failed to send token: ${response.message}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("FCM", "Error sending token: ${e.message}")
            }
        }
    }


    override fun onMessageReceived(message: RemoteMessage) {
        if (message.getNotification() != null){
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }

    fun generateNotification( title: String, message: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.congrats)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }

    private fun getRemoteView(title: String, message: String): RemoteViews? {
        val remoteView = RemoteViews("com.example.foodapp", R.layout.push_notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.noti_logo,R.drawable.congrats)

        return remoteView
    }

}