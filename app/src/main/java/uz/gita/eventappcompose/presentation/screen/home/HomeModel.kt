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
) : HomeContract.Model, ViewModel() {
    override val container =
        container<HomeContract.UiState, HomeContract.SideEffect>(HomeContract.UiState.Loading)

    override fun eventDispatcher(intent: HomeContract.Intent) {
        when (intent) {
            is HomeContract.Intent.LoadEvents -> {
                viewModelScope.launch {
                    useCase.getAllEvents().onEach {
                        intent {
                            reduce {
                                HomeContract.UiState.EventList(it)
                            }
                        }
                    }.launchIn(viewModelScope)
                }
            }

            is HomeContract.Intent.EnableEvent -> {
                viewModelScope.launch {
                    useCase.updateEventStateToEnable(intent.eventsData.id).launchIn(viewModelScope)
                    useCase.getAllEvents().onEach {
                        intent {
                            postSideEffect(HomeContract.SideEffect.EventTurnedOn("${intent.eventsData.eventName} turned ON"))
                            reduce {
                                HomeContract.UiState.EventList(it)
                            }
                        }
                    }.launchIn(viewModelScope)
                }
            }

            is HomeContract.Intent.DisableEvent -> {
                viewModelScope.launch {
                    useCase.updateEventStateToDisable(intent.eventsData.id).launchIn(viewModelScope)
                    useCase.getAllEvents().onEach {
                        intent {
                            postSideEffect(HomeContract.SideEffect.EventTurnedOn("${intent.eventsData.eventName} turned OFF"))
                            reduce {
                                HomeContract.UiState.EventList(it)
                            }
                        }
                    }.launchIn(viewModelScope)
                }
            }
        }
    }
}
