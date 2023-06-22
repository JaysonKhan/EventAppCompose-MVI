package uz.gita.eventappcompose.domain.repository

import uz.gita.eventappcompose.data.local.entity.EventEntity

interface AppRepository {
    suspend fun getAllDisableEvents(): List<EventEntity>

    suspend fun getAllEnableEvents(): List<EventEntity>

    suspend fun getAllEvents(): List<EventEntity>

    suspend fun updateEventStateToDisable(eventId: Int)

    suspend fun updateEventStateToEnable(eventId: Int)
}