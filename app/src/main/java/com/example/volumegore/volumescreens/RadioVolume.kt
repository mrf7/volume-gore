package com.example.volumegore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import com.example.volumegore.VolumeBarDisplay
import com.example.volumegore.VolumeChanged
import com.example.volumegore.ui.theme.volumeGoreTheme

/**
 * Compose implementation of [This idea](https://www.reddit.com/r/ProgrammerHumor/comments/6f2c4v/advanced_volume_control/)
 */
@ExperimentalFoundationApi
@Composable
fun RadioVolume(currentVolume: Int, onVolumeSelected: VolumeChanged) {
    Column {
        VolumeBarDisplay(volume = currentVolume,
            Modifier
                .padding(horizontal = 10.dp, vertical = 20.dp)
                .fillMaxWidth())
        LazyVerticalGrid(
            cells = GridCells.Adaptive(60.dp),
            Modifier
                .fillMaxSize()
        ) {
            // This should be remembered to avoid shuffling the numbers every recomposition, but being
            // horrible is the goal so leaving it like this
            val numbers = List(101) { it }.shuffled()
            items(numbers) {
                Row(Modifier.clickable { onVolumeSelected(it) }) {
                    RadioButton(
                        selected = currentVolume == it,
                        onClick = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
//                    Text(
//                        it.toString(),
//                        Modifier
//                            .padding(vertical = 10.dp)
//                            .align(Alignment.CenterVertically)
//                    )
                    VolumeBarDisplay(
                        volume = it,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun RadioVolumePreview() {
    volumeGoreTheme {
        Surface(color = MaterialTheme.colors.background) {
            RadioVolume(currentVolume = 0) {}
        }
    }
}
