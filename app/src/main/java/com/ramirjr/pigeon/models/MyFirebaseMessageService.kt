package com.ramirjr.pigeon.models

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ramirjr.pigeon.R
import com.ramirjr.pigeon.messages.LatestMessagesActivity
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            notificatioReceived(applicationContext, it.title, it.body)
            if (it.body?.contains("pix", true) == true) {
                val localBroadcast = LocalBroadcastManager.getInstance(applicationContext)
                localBroadcast.sendBroadcast(Intent("PIX_RECEIVED"))
            }
        }
    }

    fun notificatioReceived(context: Context, title: String?, message: String?) {

        val notificationChannel: NotificationChannel
        val builder: NotificationCompat.Builder
        val channelId = TXT_CHANNEL
        val notificationManager: NotificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = Random.nextInt(1, 100)
        val intent = Intent(this, LatestMessagesActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, message, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = NotificationCompat.Builder(this, channelId)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(message)
                        .setSummaryText(TXT_MESSAGE)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
        } else {
            builder = NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(id, builder.build())
    }

    companion object {
        private const val TXT_CHANNEL = "Pix"
        private const val TXT_MESSAGE = "Mensagem Teste"
    }

}