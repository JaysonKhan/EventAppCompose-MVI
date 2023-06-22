package uz.gita.eventappcompose.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.gita.eventappcompose.data.local.entity.EventEntity
import uz.gita.eventappcompose.data.model.EventsData

interface UseCase {
    fun getAllDisableEvents(): Flow<List<EventsData>>

    fun getAllEnableEvents(): Flow<List<EventsData>>

    suspend fun getAllEvents(): Flow<List<EventsData>>

    fun updateEventStateToDisable(eventId: Int): Flow<Unit>

    fun updateEventStateToEnable(eventId: Int): Flow<Unit>
}