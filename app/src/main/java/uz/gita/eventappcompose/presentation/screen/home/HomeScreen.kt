package uz.gita.eventappcompose.presentation.screen.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.eventappcompose.presentation.components.EventItem
import uz.gita.eventappcompose.presentation.screen.home.HomeContract.SideEffect.EventTurnedOn
import uz.gita.eventappcompose.ui.theme.EventAppComposeTheme

class HomeScreen:AndroidScreen() {
    @Composable
    override fun Content() {
        val viewmodel = getViewModel<HomeModel>()
        val uiState = viewmodel.collectAsState().value
        val context = LocalContext.current

        viewmodel.collectSideEffect{ sideEffect->
            when(sideEffect){
                is EventTurnedOn -> {
                    Toast.makeText(context, sideEffect.name, Toast.LENGTH_SHORT).show()
                }
                is HomeContract.SideEffect.EventTurnedOff -> {
                    Toast.makeText(context, sideEffect.name, Toast.LENGTH_SHORT).show()
                }
            }
        }

        EventAppComposeTheme {
            HomeContent(uiState, viewmodel::eventDispatcher)
        }
    }
    @Composable
    fun HomeContent(
        uiState: HomeContract.UiState,
        eventDipatcher: (HomeContract.Intent) -> Unit,
    ){
      Surface {
          Column {
              Text(text = "Events List", modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 8.dp),
                  textAlign = TextAlign.Center,
                  fontSize = MaterialTheme.typography.titleLarge.fontSize
              )
              when(uiState){
                  is HomeContract.UiState.Loading -> {
                      eventDipatcher.invoke(HomeContract.Intent.LoadEvents)
                  }
                  is HomeContract.UiState.EventList -> {
                      LazyColumn{
                         for (i in uiState.list.indices){
                             item {
                                 EventItem(event = uiState.list[i]) { checked->
                                     if (checked){
                                         eventDipatcher.invoke(HomeContract.Intent.EnableEvent(uiState.list[i]))
                                     }else{
                                         eventDipatcher.invoke(HomeContract.Intent.DisableEvent(uiState.list[i]))
                                     }
                                 }
                             }
                         }
                      }
                  }
              }
          }
      }
    }
    @Preview(showSystemUi = true)
    @Composable
    fun PrevHome(){
        EventAppComposeTheme {
            HomeContent(uiState = HomeContract.UiState.Loading){

            }
        }
    }

}
