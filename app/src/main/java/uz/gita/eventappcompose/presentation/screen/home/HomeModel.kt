package uz.gita.eventappcompose.presentation.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import uz.gita.eventappcompose.domain.usecase.UseCase
import javax.inject.Inject

@HiltViewModel
class HomeModel @Inject constructor(
    private val useCase: UseCase
):HomeContract.Model, ViewModel() {
    override val container = container<HomeContract.UiState, HomeContract.SideEffect>(HomeContract.UiState.Loading)

    override fun eventDispatcher(intent: HomeContract.Intent) {
       when(intent){
          is HomeContract.Intent.LoadEvents -> {
               viewModelScope.launch{
                   useCase.getAllEvents().onEach {
                       intent{
                           reduce {
                               Log.d("TTT", "LoadEvent")
                               HomeContract.UiState.EventList(it)
                           }
                       }
                   }.launchIn(viewModelScope)
               }
           }
           is HomeContract.Intent.EnableEvent -> {
                   useCase.updateEventStateToEnable(intent.eventsData.id).launchIn(viewModelScope)
               intent{
                   postSideEffect(HomeContract.SideEffect.EventTurnedOn("${intent.eventsData.eventName.toString()} turned ON"))
               }
           }
           is HomeContract.Intent.DisableEvent -> {
                   useCase.updateEventStateToDisable(intent.eventsData.id).launchIn(viewModelScope)
               intent{
                   postSideEffect(HomeContract.SideEffect.EventTurnedOn("${intent.eventsData.eventName.toString()} turned OFF"))
               }
           }
       }
    }
/*
    private val getAllEnableEventsObserver = Observer<List<EventsData>> { list ->
        Log.d("CCC", "getAllEnableEventsObserver: $list")
        val arrayList = ArrayList<String>()

        for (i in list.indices) {
            arrayList.add(list[i].events)
        }
        val intent = Intent(requireContext(), EventService::class.java)
        intent.putStringArrayListExtra("enabledActions", arrayList)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(intent)
        } else {
            requireActivity().startService(intent)
        }
        adapter.submitList(list)
        binding.recyclerScreen.adapter = adapter
        binding.recyclerScreen.layoutManager = LinearLayoutManager(requireContext())*/
    }
