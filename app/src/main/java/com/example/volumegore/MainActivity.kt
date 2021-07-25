package com.example.volumegore

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.volumegore.ui.theme.volumeGoreTheme
import com.example.volumegore.RadioVolume
import com.example.volumegore.SevenSegment

typealias VolumeChanged = (volume: Int) -> Unit

class MainActivity : ComponentActivity() {
    @ExperimentalStdlibApi
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            volumeGoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val viewModel: VolumeViewModel by viewModels()
                    val volume by viewModel.volume.observeAsState(viewModel.getInitialVolume())
                    RadioVolume(volume, viewModel::updateVolume)
                }
            }
        }
    }
}