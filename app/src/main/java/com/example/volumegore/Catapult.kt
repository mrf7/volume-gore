package com.example.volumegore

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.RingVolume
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.volumegore.VolumeChanged
import com.example.volumegore.ui.theme.volumeGoreTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

val redToGreen = keyframes<Color> {
    durationMillis = 6_000
    Color.Green at 0 with FastOutSlowInEasing
    Color.Yellow at 4_000 with FastOutSlowInEasing
    Color.Red at 6_000 with FastOutSlowInEasing
}

@Composable
fun CatapultVolume(volume: Int, volumeChanged: VolumeChanged) {
    ConstraintLayout(
        Modifier
            .padding(vertical = 100.dp)
            .fillMaxWidth()
    ) {
        val (image, slider, angle, ball) = createRefs()
        val charging = remember { mutableStateOf(false) }
        val rotate by animateFloatAsState(targetValue = if (charging.value) 45f else 0f, tween(6_000))
        val color by animateColorAsState(
            targetValue = if (charging.value) Color.Red else Color.Black,
            if (charging.value) redToGreen else snap()
        )
        Text(rotate.roundToInt().toString(), Modifier.constrainAs(angle) {
            centerHorizontallyTo(image)
            top.linkTo(image.bottom, 5.dp)
        })
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