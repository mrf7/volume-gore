package com.example.volumegore

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.volumegore.ui.theme.volumeGoreTheme
import kotlin.math.roundToInt

val redToGreen = keyframes<Color> {
    durationMillis = 6_000
    Color.Green at 0 with FastOutLinearInEasing
    Color.Yellow at 4_000 with FastOutLinearInEasing
    Color.Red at 6_000 with FastOutLinearInEasing
}

/**
 * Compose implementation of [This idea](https://www.reddit.com/r/ProgrammerHumor/comments/6f8ory/launch_a_90db_volume_slider_over_300_metres/)
 * WIP havent figured out how to draw the ball on screen in the proper spot relative to the progress bar
 */
@Composable
fun CatapultVolume(volume: Int, volumeChanged: VolumeChanged) {
    ConstraintLayout(
        Modifier
            .padding(vertical = 100.dp)
            .fillMaxWidth()
    ) {
        val (image, slider) = createRefs()
        val charging = remember { mutableStateOf(false) }
        // infiniteRepeatable will continue to repeat until we recompose with a different animation, so switch to a tween
        // when the button is released
        val rotate by animateFloatAsState(
            targetValue = if (charging.value) 45f else 0f,
            if (charging.value) infiniteRepeatable(tween(6_000, easing =FastOutLinearInEasing), RepeatMode.Reverse) else tween(6_000)
        )
        val color by animateColorAsState(
            targetValue = if (charging.value) Color.Red else Color.Black,
            if (charging.value) infiniteRepeatable(redToGreen, RepeatMode.Reverse) else snap()
        )
        Icon(
            Icons.Default.VolumeUp, "", tint = color,
            modifier = Modifier
                .size(80.dp)
                .rotate(-rotate)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        charging.value = true
                        awaitRelease()
                        charging.value = false
                        volumeChanged((rotate / 45f * 100f).toInt())
                    })
                }
                .constrainAs(image) {
                    centerVerticallyTo(parent)
                    start.linkTo(parent.start, 5.dp)
                }
        )
        Slider(
            value = volume.toFloat(),
            enabled = false,
            valueRange = 0f..100f,
            steps = 100,
            onValueChange = { volumeChanged(it.toInt()) },
            modifier = Modifier.constrainAs(slider) {
                start.linkTo(image.absoluteRight, 10.dp)
                centerVerticallyTo(image)
                end.linkTo(parent.end, 10.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Preview
@Composable
fun CatapultVolumePreview() {
    volumeGoreTheme {
        Surface(color = MaterialTheme.colors.background) {
            CatapultVolume(0) {}
        }
    }
}