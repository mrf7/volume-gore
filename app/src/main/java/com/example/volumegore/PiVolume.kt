package com.example.volumegore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.volumegore.ui.theme.volumeGoreTheme

@Composable
fun PiVolume(volume: Int, volumeChanged: VolumeChanged) {
    Column {
        VolumeBarDisplay(volume = volume, Modifier.fillMaxWidth())
        var piText by remember {
            mutableStateOf("")
        }
        Row {
            Text("Enter Pi for volume", Modifier.align(Alignment.CenterVertically))
            TextField(value = piText, onValueChange = { piText = it },)
        }
        val pi = Math.PI.toString()
        val piMatch = pi.indexOf(piText)
        if (piMatch == -1 || piMatch != 0) {
            Text(text = "WRONG TRY AGAIN")
        } else {
            volumeChanged(piText.length)
        }
    }
}

@Preview
@Composable
fun PiVolumePreview(){
    volumeGoreTheme {
        Surface(color = MaterialTheme.colors.background) {
            PiVolume(5,{})
        }
    }
}