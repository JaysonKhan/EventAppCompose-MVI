package uz.gita.eventappcompose.domain.usecase

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.eventappcompose.data.local.entity.EventEntity
import uz.gita.eventappcompose.data.model.EventsData
import uz.gita.eventappcompose.domain.repository.AppRepository
import javax.inject.Inject

class UseCaseImpl
@Inject constructor(
    private val repository: AppRepository
) : UseCase {
    private var list: List<EventsData> = ArrayList()

    override fun getAllDisableEvents() = flow<List<EventsData>> {
        val result = repository.getAllDisableEvents().map {
            it.toData()
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override fun getAllEnableEvents() = flow<List<EventsData>> {
        val result = repository.getAllEnableEvents().map {
            it.toData()
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllEvents() =  flow<List<EventsData>> {
        val result = repository.getAllEvents().map {
            it.toData()

        }
        Log.d("TTT", "USECASE")
        emit(result)
    }.flowOn(Dispatchers.IO)


    override fun updateEventStateToDisable(eventId: Int) = flow<Unit> {
        repository.updateEventStateToDisable(eventId)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    override fun updateEventStateToEnable(eventId: Int) = flow<Unit> {
        repository.updateEventStateToEnable(eventId)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

}
