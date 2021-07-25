package com.example.volumegore

import android.app.Application
import android.content.Context
import android.media.AudioDeviceInfo
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import android.media.AudioManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.ceil

const val UPDATE_SYS = true

class VolumeViewModel(app: Application) : AndroidViewModel(app) {
    val audioManager = getApplication<Application>()
        .getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _volume = MutableLiveData<Int>()
    val volume: LiveData<Int>
        get() = _volume

    fun getInitialVolume(): Int {
        val current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        return (current.toFloat() / max * 100).toInt()
    }

    fun updateVolume(newVol: Int) {
        _volume.value = newVol.also { if (UPDATE_SYS) changeSystemVolume(it) }
    }

    private fun changeSystemVolume(newVol: Int) {
        if(newVol !in 0..100) return
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val updateVolume = ceil(newVol.toDouble() / 100 * max).toInt()
        if (updateVolume != audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, updateVolume, AudioManager.FLAG_SHOW_UI)
        }
    }

}