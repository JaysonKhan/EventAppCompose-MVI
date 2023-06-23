package uz.gita.eventappcompose.utils.service

import android.app.Notification.Action
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import uz.gita.eventappcompose.MainActivity
import uz.gita.eventappcompose.R
import uz.gita.eventappcompose.utils.broadcast.EventReceiver

class EventService:Service() {
    private val CHANNEL_ID = "KHAN347"
    private val receiver = EventReceiver()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("TTT", "Service at onCreate()")
        createChanel()
        startServis()
        registerAllIntents()
    }

    private fun registerAllIntents() {
        registerReceiver(receiver, IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
            addAction(Intent.ACTION_TIME_CHANGED)
            addAction(Intent.ACTION_SHUTDOWN)
            addAction(Intent.ACTION_DATE_CHANGED)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getStringExtra("STOP") == "STOP") {
            stopSelf() // Servisni to'xtatish
        }
        return START_NOT_STICKY
    }

    private fun startServis() {
        val notifIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent
            .getActivity(
                this,
                0,
                notifIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val notification = NotificationCompat
            .Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Event App")
            .setContentIntent(pendingIntent)
            .setOngoing(false)
            .setCustomContentView(createNotificationLayout())
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationLayout(): RemoteViews {
        val view = RemoteViews(packageName, R.layout.remote_view)
        // Yopish tugmasi bosilganda notificationni yopish uchun PendingIntent yaratish
        val closeIntent = Intent(this, EventService::class.java).apply {
            putExtra("STOP", "STOP")
        }
        val closePendingIntent = PendingIntent.getService(
            this,
            1,
            closeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    /*    view.setInt(
            R.id.notificationLayout,
            "setBackgroundResource",
            R.drawable.logo
        )*/
        view.setOnClickPendingIntent(R.id.manageState, createPendingIntent())
        view.setOnClickPendingIntent(R.id.closeButton, closePendingIntent)
        return view
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, EventService::class.java)
        return PendingIntent
            .getService(
                this,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

    }

    private fun createChanel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val chanel = NotificationChannel(CHANNEL_ID, "XXX", NotificationManager.IMPORTANCE_DEFAULT)
            chanel.setSound(null, null)
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(chanel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        receiver.clearReceiver()
        unregisterReceiver(receiver)
    }
}