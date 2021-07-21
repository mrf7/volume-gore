package com.example.voulumegore

import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VolumeBarDisplay(volume: Int, modifier: Modifier = Modifier) {
    // TODO Make a bar
    LinearProgressIndicator(volume.toFloat() / 100, modifier.height(45.dp))
}