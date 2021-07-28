package com.example.volumegore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.volumegore.ui.theme.volumeGoreTheme

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
//                    StateOfMutableList()
//                    ListOfMutableState()
                    MutableStateList()
                }
            }
        }
    }
}

enum class VolumeControl {
    PiVolume, RadioVolume, ScrollingList, SevenSegment, Catapult
}