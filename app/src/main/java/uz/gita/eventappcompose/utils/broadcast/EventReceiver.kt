package uz.gita.eventappcompose.utils.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import uz.gita.eventappcompose.R
import uz.gita.eventappcompose.data.local.dao.MyDao
import javax.inject.Inject

@AndroidEntryPoint
class EventReceiver : BroadcastReceiver() {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    @Inject
    lateinit var eventDao: MyDao

    override fun onReceive(context: Context?, intent: Intent?) {
        scope.launch {
            val events = eventDao.getAllEnableEvents()

            for (i in events.indices) {
                when (intent?.action) {
                    events[i].events -> {
                        var soundResId = getSoundResourceId(events[i].events)
                        if (soundResId==R.raw.airplane_on&&intent.action==Intent.ACTION_AIRPLANE_MODE_CHANGED){
                            val isAirplaneModeOn = intent.getBooleanExtra("state", false)
                            if (!isAirplaneModeOn){
                                soundResId=R.raw.airplane_off
                            }
                        }
                        playNotificationSound(context, soundResId)
                    }
                }
            }
        }
    }

    private fun getSoundResourceId(event: String): Int {
        return when (event) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> R.raw.airplane_on
            Intent.ACTION_SCREEN_ON -> R.raw.screen_on
            Intent.ACTION_SCREEN_OFF -> R.raw.screen_off
            Intent.ACTION_POWER_CONNECTED -> R.raw.charging_on
            Intent.ACTION_POWER_DISCONNECTED -> R.raw.charging_off
            Intent.ACTION_DEVICE_STORAGE_LOW -> R.raw.storage_low
            Intent.ACTION_TIMEZONE_CHANGED -> R.raw.time_zone_changed
            Intent.ACTION_TIME_CHANGED -> R.raw.time_changed
            Intent.ACTION_BATTERY_OKAY -> R.raw.batery_full
            Intent.ACTION_SHUTDOWN -> R.raw.power_off
            Intent.ACTION_DATE_CHANGED -> R.raw.date_changed
            else -> R.raw.sound
        }
    }

    private fun playNotificationSound(context: Context?, soundResId: Int) {
        val notificationSound = Uri.parse("android.resource://${context?.packageName}/$soundResId")
        val ringtone = RingtoneManager.getRingtone(context, notificationSound)

        ringtone.play()

        // MediaPlayer ishini to'xtatish uchun bitta sekund tantanali to'xtatuvchi Handler
        Handler(Looper.getMainLooper()).postDelayed({
            ringtone.stop()
        }, 7000)
    }

    fun clearReceiver() {
        scope.cancel()
    }
}
