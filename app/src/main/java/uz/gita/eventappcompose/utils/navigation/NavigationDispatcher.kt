package uz.gita.eventappcompose.utils.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationDispatcher @Inject constructor() : AppNavigator, NavigationHandler {
    override val navigatorBuffer = MutableSharedFlow<NavigationArg>()

    private suspend fun navigate(arg: NavigationArg) {
        navigatorBuffer.emit(arg)
    }

    override suspend fun backUntilRoot() = navigate { popUntilRoot() }

    override suspend fun backAll() = navigate { popAll() }

    override suspend fun navigateTo(screen: MyScreen) = navigate { push(screen) }

    override suspend fun replace(screen: MyScreen) = navigate { replace(screen) }
    override suspend fun replaceAll(screen: MyScreen) = navigate { replaceAll(screen) }
    override suspend fun back() = navigate { pop() }

}