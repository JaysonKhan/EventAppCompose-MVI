package uz.gita.eventappcompose.utils.navigation

interface AppNavigator {
    suspend fun backUntilRoot()
    suspend fun backAll()
    suspend fun navigateTo(screen: MyScreen)
    suspend fun replace(screen: MyScreen)
    suspend fun replaceAll(screen: MyScreen)
    suspend fun back()
}