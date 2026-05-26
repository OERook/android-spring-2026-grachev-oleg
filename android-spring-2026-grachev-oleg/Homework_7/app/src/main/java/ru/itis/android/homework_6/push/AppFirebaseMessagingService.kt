package ru.itis.android.homework_6.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.itis.android.homework_6.MainActivity
import ru.itis.android.homework_6.R
import kotlin.random.Random


class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "FCM token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        if (data.isEmpty()) return

        val kind = data["kind"].orEmpty()
        val title = data["title"] ?: getString(R.string.app_name)
        val body = data["message"].orEmpty()

        Log.d(TAG, "onMessageReceived: kind=$kind, title=$title, message=$body, fullData=$data")

        showNotification(kind = kind, title = title, body = body)
    }

    private fun showNotification(kind: String, title: String, body: String) {
        ensureChannel()

        val (icon, priority, prefix) = when (kind) {
            "promo" -> Triple(
                android.R.drawable.ic_menu_send,
                NotificationCompat.PRIORITY_HIGH,
                "🎁 "
            )
            "auth" -> Triple(
                android.R.drawable.ic_lock_lock,
                NotificationCompat.PRIORITY_MAX,
                "🔐 "
            )
            else -> Triple(
                android.R.drawable.ic_dialog_info,
                NotificationCompat.PRIORITY_DEFAULT,
                ""
            )
        }

        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pi = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(prefix + title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(priority)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()

        val manager = NotificationManagerCompat.from(this)
        try {
            manager.notify(Random.nextInt(), notif)
        } catch (se: SecurityException) {
            Log.w(TAG, "POST_NOTIFICATIONS permission denied", se)
        }
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (mgr.getNotificationChannel(CHANNEL_ID) == null) {
                mgr.createNotificationChannel(
                    NotificationChannel(
                        CHANNEL_ID,
                        getString(R.string.default_notification_channel_name),
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )
            }
        }
    }

    companion object {
        private const val TAG = "AppFCM"
        private const val CHANNEL_ID = "default_channel"
    }
}
