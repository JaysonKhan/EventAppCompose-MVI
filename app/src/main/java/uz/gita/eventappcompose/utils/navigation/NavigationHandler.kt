package uz.gita.eventappcompose.utils.navigation

import kotlinx.coroutines.flow.SharedFlow
import uz.gita.eventappcompose.utils.navigation.NavigationArg

interface NavigationHandler {
    val navigatorBuffer:SharedFlow<NavigationArg>
}