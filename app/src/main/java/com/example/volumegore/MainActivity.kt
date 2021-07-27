package com.example.volumegore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.volumegore.ui.theme.volumeGoreTheme
import com.example.volumegore.volumescreens.SevenSegment

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
                    var volumeControl by remember {
                        mutableStateOf(VolumeControl.PiVolume)
                    }

                    Scaffold(topBar = {
                        var expanded by remember { mutableStateOf(false) }
                        TopAppBar(
                            title = {
                                Text(volumeControl.name)
                            },
                            actions = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(Icons.Default.MoreVert, "")
                                }
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    VolumeControl.values().toList().minus(volumeControl).forEach {
                                        DropdownMenuItem(onClick = {
                                            expanded = false
                                            volumeControl = it
                                        }) {
                                            Text(it.name)
                                        }
                                    }
                                }
                            }
                        )
                    }){
                        when (volumeControl) {
                            VolumeControl.PiVolume -> PiVolume(volume, viewModel::updateVolume)
                            VolumeControl.RadioVolume -> RadioVolume(volume, viewModel::updateVolume)
                            VolumeControl.ScrollingList -> ListVolume(volume, viewModel::updateVolume)
                            VolumeControl.SevenSegment -> SevenSegment(volume, viewModel::updateVolume)
                            VolumeControl.Catapult -> CatapultVolume(volume, viewModel::updateVolume)
                        }
                    }
                }
            }
        }
    }
}

enum class VolumeControl {
    PiVolume, RadioVolume, ScrollingList, SevenSegment, Catapult
}