package com.example.volumegore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.volumegore.ui.theme.volumeGoreTheme

/**
 * Compose implementation of [This idea](https://www.reddit.com/r/ProgrammerHumor/comments/8zibwm/new_mobile_phone_volume_control/)
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListVolume(currentVolume: Int, onVolumeSelected: VolumeChanged) {
    val listState = rememberLazyListState(currentVolume)
    val showPaywall = remember { mutableStateOf(false) }
    if (showPaywall.value) {
        PaywallDialog { showPaywall.value = false }
    }
    VolumeList(listState, currentVolume) { volume, locked ->
        if (locked) {
            showPaywall.value = true
        } else {
            onVolumeSelected(volume)
        }
    }
}

@Composable
fun PaywallDialog(onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Unlock More Volume!") }, buttons = {
        Button(onClick = onDismiss, Modifier.padding(10.dp).fillMaxWidth()) {
            Text(text = "Unlock mutliples of 2 for only $1.99")
        }
        Button(onClick = onDismiss, Modifier.padding(10.dp).fillMaxWidth()) {
            Text(text = "Unlock mutliples of 5 for only $1.99")
        }
        Button(onClick = onDismiss, Modifier.padding(10.dp).fillMaxWidth()) {
            Text(text = "Or unlock everything for only $5.99")
        }
    })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun VolumeList(
    listState: LazyListState,
    currentVolume: Int,
    onVolumeSelected: (volume: Int, locked: Boolean) -> Unit
) {
    LazyColumn(state = listState) {
        stickyHeader {
            VolumeBarDisplay(
                volume = currentVolume,
                Modifier
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )
        }
        items(101) {
            val locked = it % 2 == 0 || it % 5 == 0
            Box(modifier = Modifier
                .clickable { onVolumeSelected(it, locked) }
                .height(50.dp)
            ) {
                LinearProgressIndicator(
                    progress = it.toFloat() / 100,
                    color = Color.Green,
                    backgroundColor = MaterialTheme.colors.background,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
                Text(text = "$it%", fontSize = 30.sp, modifier = Modifier.align(Alignment.CenterEnd))
                if (locked) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Gray.copy(alpha = .8f))
                    ) {
                        Image(
                            Icons.Filled.Lock, "",
                            Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
            Divider()
        }
    }
}

@Preview
@Composable
fun ListVolumePreview() {
    volumeGoreTheme {
        Surface(color = MaterialTheme.colors.background) {
            ListVolume(currentVolume = 0) {}
        }
    }
}
