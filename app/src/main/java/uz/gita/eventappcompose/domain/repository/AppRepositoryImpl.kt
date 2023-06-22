package uz.gita.eventappcompose.domain.repository

import android.content.Intent
import uz.gita.eventappcompose.R
import uz.gita.eventappcompose.data.local.dao.MyDao
import uz.gita.eventappcompose.data.local.entity.EventEntity
import javax.inject.Inject

class AppRepositoryImpl
@Inject constructor(
    private val eventsDao: MyDao
) : AppRepository {
    override suspend fun getAllDisableEvents(): List<EventEntity> {
        return eventsDao.getAllDisableEvents()
    }

    override suspend fun getAllEnableEvents(): List<EventEntity> {
        return eventsDao.getAllEnableEvents()
    }

    override suspend fun updateEventStateToDisable(eventId: Int) {
        eventsDao.updateEventStateToDisable(eventId)
    }

    override suspend fun updateEventStateToEnable(eventId: Int) {
        eventsDao.updateEventStateToEnable(eventId)
    }

    override suspend fun getAllEvents(): List<EventEntity> {
        if (eventsDao.getAllEvents().isNullOrEmpty()){
            val list = listOf(
                EventEntity(
                    id = 1,
                    eventIcon = R.drawable.ic_screen_on,
                    eventName = R.string.screen_on,
                    events = Intent.ACTION_SCREEN_ON
                ),

                EventEntity(
                    id = 2,
                    eventIcon = R.drawable.ic_screen_off,
                    eventName = R.string.screen_off,
                    events = Intent.ACTION_SCREEN_OFF
                ),

                EventEntity(
                    id = 3,
                    eventIcon = R.drawable.ic_connected,
                    eventName = R.string.battery_charging_on,
                    events = Intent.ACTION_POWER_CONNECTED
                ),

                EventEntity(
                    id = 4,
                    eventIcon = R.drawable.ic_disconnected,
                    eventName = R.string.battery_charging_off,
                    events = Intent.ACTION_POWER_DISCONNECTED
                ),

                EventEntity(
                    id = 6,
                    eventIcon = R.drawable.ic_airplane,
                    eventName = R.string.text_airplane,
                    events = Intent.ACTION_AIRPLANE_MODE_CHANGED
                ),
                EventEntity(
                    id = 7,
                    eventIcon = R.drawable.ic_battery_ok,
                    eventName = R.string.text_battery_ok,
                    events = Intent.ACTION_BATTERY_OKAY
                ),
                EventEntity(
                    id = 8,
                    eventIcon = R.drawable.ic_battery_low,
                    eventName = R.string.text_battery_low,
                    events = Intent.ACTION_BATTERY_LOW
                ),
                EventEntity(
                    id = 8,
                    eventIcon = R.drawable.ic_shutdown,
                    eventName = R.string.text_shut_down,
                    events = Intent.ACTION_SHUTDOWN
                ),
                EventEntity(
                    id = 9,
                    eventIcon = R.drawable.time_zone,
                    eventName = R.string.text_time_zone_changed,
                    events = Intent.ACTION_TIMEZONE_CHANGED
                ),
                EventEntity(
                    id = 10,
                    eventIcon = R.drawable.ic_time_changed,
                    eventName = R.string.text_time_changed,
                    events = Intent.ACTION_TIME_CHANGED
                ),
                EventEntity(
                    id = 11,
                    eventIcon = R.drawable.date_changed,
                    eventName = R.string.text_date_changed,
                    events = Intent.ACTION_DATE_CHANGED
                ),

                )
            list.forEach {
                eventsDao.addEvent(it)
            }
        }
        return eventsDao.getAllEvents()

    }
}