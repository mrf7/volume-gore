package com.example.voulumegore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.voulumegore.ui.theme.VoulumeGoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VoulumeGoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val viewModel: VolumeViewModel by viewModels()
                    val volume by viewModel.volume.observeAsState(viewModel.getInitialVolume())
                    ListVolume(volume) {volume, locked ->
                        if (locked) {
                            // Do nothing
                        } else {
                            viewModel.updateVolume(volume)
                        }
                    }
                }
            }
        }
    }
}