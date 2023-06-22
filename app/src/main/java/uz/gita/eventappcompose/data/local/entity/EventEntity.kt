package uz.gita.eventappcompose.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.gita.eventappcompose.data.model.EventsData

@Entity(tableName = "events")
data class EventEntity(
   @PrimaryKey(autoGenerate = true)
   val id:Int,
   val eventIcon: Int,
   val events: String,
   val eventName: Int,
   var eventState: Int = 0
){
   fun toData(): EventsData = EventsData(
      id = id,
      eventIcon = eventIcon,
      events = events,
      eventName = eventName,
      eventState = eventState
   )

}
