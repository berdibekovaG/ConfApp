package kz.kolesateam.confapp.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.event_details.presentation.EventDetailsRouter
import kz.kolesateam.confapp.events.data.models.EventApiData

object NotificationHelper {

    private lateinit var application: Application
    private var notificationCounter: Int = 0

    fun init(application: Application) {
        this.application = application
        initChannel()
    }

    fun sendNotification(
            title: String,
            content: String,
            eventId: Int
    ) {
        val notification: Notification = getNotification(
                title = title,
                content = content,
                eventId = eventId
        )
        NotificationManagerCompat.from(application).notify(
                notificationCounter++,
                notification
        )
    }

    private fun getNotification(
            title: String,
            content: String,
            eventId: Int
    ): Notification = NotificationCompat.Builder(
            application, "favorite_notification_channel"
    ).setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_favorite_filled_imageview)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntent(eventId = eventId))
            .setAutoCancel(true)
            .build()

    private fun getPendingIntent(
        eventId: Int
    ): PendingIntent{
        val upcomingEventsIntent = EventDetailsRouter().createIntentForNotification(
            context = application,
            eventId = eventId
        ).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(application,
            0,
            upcomingEventsIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
    }

    private fun initChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channelId: String = "favorite_notification_channel"
        val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
                channelId, channelId, importance
        )
        val notificationManager: NotificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}