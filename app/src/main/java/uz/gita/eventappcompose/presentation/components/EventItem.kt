package uz.gita.eventappcompose.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.gita.eventappcompose.data.model.EventsData

@Composable
fun EventItem(
    event: EventsData,
    listener:(Boolean)->Unit
) {
    var b = event.eventState==1
    var checkedState by remember {
        mutableStateOf(b)
    }
    Surface {
        Card(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Image(modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(16.dp), painter = painterResource(id = event.eventIcon), contentDescription = null, contentScale = ContentScale.Crop)
                Text(text = stringResource(id = event.eventName), fontSize = MaterialTheme.typography.titleLarge.fontSize, modifier = Modifier.padding(horizontal = 16.dp))
                Switch(checked = checkedState, onCheckedChange ={
                    checkedState = !checkedState
                    listener.invoke(checkedState)
                }, modifier = Modifier.padding(end = 16.dp) )
            }

        }
    }
}
