@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.volumegore

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.volumegore.ui.theme.volumeGoreTheme
import kotlinx.coroutines.delay

val redToGreen = keyframes<Color> {
    durationMillis = 6_000
    Color.Green at 0 with FastOutLinearInEasing
    Color.Yellow at 4_000 with FastOutLinearInEasing
    Color.Red at 6_000 with FastOutLinearInEasing
}


@Composable
fun CatapultVolume(volume: Int, volumeChanged: VolumeChanged) {
    Row {
        var charging by remember { mutableStateOf(false) }
        // infiniteRepeatable will continue to repeat until we recompose with a different animation, so switch to a tween
        // when the button is released
        val rotate by animateFloatAsState(
            targetValue = if (charging) 45f else 0f,
            if (charging) infiniteRepeatable(
                tween(6_000, easing = FastOutLinearInEasing),
                RepeatMode.Reverse
            ) else tween(6_000)
        )
        val color by animateColorAsState(
            targetValue = if (charging) Color.Red else Color.Black,
            if (charging) infiniteRepeatable(redToGreen, RepeatMode.Reverse) else snap()
        )

        var launchVol by remember { mutableStateOf<Float?>(null) }
        Icon(
            Icons.Default.VolumeUp, "", tint = color,
            modifier = Modifier
                .size(40.dp)
                .rotate(-rotate)
                .align(Alignment.Bottom)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        charging = true
                        awaitRelease()
                        charging = false
                        val newVol = rotate / 45f * 100f
                        launchVol = newVol/ 100f
                        delay(1500)
                        launchVol = null
                        volumeChanged(newVol.toInt())
                    })
                }
        )
        BoxWithConstraints(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Physics(
                finalValue = launchVol, modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
            Slider(
                value = volume.toFloat(),
                enabled = false,
                valueRange = 0f..100f,
                steps = 100,
                onValueChange = { volumeChanged(it.toInt()) },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .height(40.dp)
            )
        }
    }
}

@Composable
internal fun Physics(finalValue: Float?,modifier: Modifier) {
    if (finalValue == null) return
    val duration = 1500
    val transition = rememberInfiniteTransition()
    val x by transition.animateFloat(
        initialValue = 0f,
        targetValue = finalValue,
        animationSpec = infiniteRepeatable(tween(duration, easing = LinearEasing))
    )
    val y by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = duration
                1f at 0 with FastOutSlowInEasing
                .5f at 500 with FastOutSlowInEasing
                1f at 1000 with LinearEasing
            }
        )
    )
    Canvas(modifier = modifier) {
        val (width, height) = this.size
        drawCircle(Color.Red, radius = 10f, Offset(x * width, y * height))
    }
}

@Preview
@Composable
fun PhysicsPreview() {
    volumeGoreTheme {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.size(200.dp)) {
            Physics(.9f, Modifier.fillMaxSize())
        }
    }
}

/**
 * Compose implementation of [This idea](https://www.reddit.com/r/ProgrammerHumor/comments/6f8ory/launch_a_90db_volume_slider_over_300_metres/)
 * WIP havent figured out how to draw the ball on screen in the proper spot relative to the progress bar
 */

@Preview
@Composable
fun CatapultVolumePreview() {
    volumeGoreTheme {
        Surface(color = MaterialTheme.colors.background) {
            var volume by remember {
                mutableStateOf(0)
            }
            CatapultVolume(volume) { volume = it }
        }
    }
}