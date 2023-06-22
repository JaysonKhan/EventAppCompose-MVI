package uz.gita.eventappcompose.presentation.screen.home

import org.orbitmvi.orbit.ContainerHost
import uz.gita.eventappcompose.data.model.EventsData

interface HomeContract {
    interface Model:ContainerHost<UiState, SideEffect>{
        fun eventDispatcher(intent:Intent)
    }
     interface Intent{
        object LoadEvents:Intent
        data class DisableEvent(val eventsData: EventsData):Intent
        data class EnableEvent(val eventsData: EventsData):Intent
    }
    sealed interface UiState{
        object Loading : UiState
        data class EventList(val list:List<EventsData>):UiState

    }
    sealed interface SideEffect{
        data class EventTurnedOn(val name:String):SideEffect
        data class EventTurnedOff(val name:String):SideEffect
    }
}