package uz.gita.eventappcompose.utils.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
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
class EventReciver():BroadcastReceiver() {
    private lateinit var mediaPlayer: MediaPlayer
    private val scope = CoroutineScope(Dispatchers.IO +  SupervisorJob())

    @Inject
    lateinit var eventDao: MyDao

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TTT", "onReceive() -> ${intent?.action}")
        mediaPlayer = MediaPlayer.create(context, R.raw.sound)
        scope.launch {
            val events = eventDao.getAllEnableEvents()

            for (i in events.indices){
                when(intent?.action){
                    events[i].events -> {
                        mediaPlayer.start()
                    }
                }
            }
        }
    }
    fun clearReciver(){
        scope.cancel()
    }
}