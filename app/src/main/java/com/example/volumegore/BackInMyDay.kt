package com.example.volumegore

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.volumegore.ui.theme.volumeGoreTheme

@Composable
fun WindowCrankVolume(volume: Int, volumeChanged: VolumeChanged) {
    Column {
        val rotation = remember {
            mutableStateOf(0f)
        }
        VolumeBarDisplay(volume = volume)
        CustomCrank(
            Modifier
                .size(width = 500.dp, height = 1000.dp)
                .rotate(rotation.value)
                .pointerInput(Unit) {
                    this.detectTransformGestures()
                })
    }
}

@Composable
fun CustomCrank(modifier: Modifier = Modifier, color: Color = Color.Gray) {
    Canvas(modifier = modifier) {
        val (canvasWidth, canvasHeight) = size
        val centerSize = size.minDimension / 9
        val endSize = size.minDimension / 7
        drawCircle(color = color, radius = centerSize)
        val endKnobCenter = Offset(canvasWidth / 2, size.minDimension / 7)
        drawCircle(
            color = color,
            radius = size.minDimension / 7,
            center = endKnobCenter
        )
        drawLine(
            color = color,
            start = Offset(canvasWidth / 2 - centerSize + 4, canvasHeight / 2),
            end = Offset(
                canvasWidth / 2 - endSize,
                endSize + 4,
            ),
            strokeWidth = 15f
        )
        drawLine(
            color = color,
            start = Offset(canvasWidth / 2 + centerSize + 4, canvasHeight / 2),
            end = Offset(
                canvasWidth / 2 + endSize,
                endSize + 4,
            ),
            strokeWidth = 15f
        )
    }
}

@Preview
@Composable
fun CrankPreview() {
    volumeGoreTheme {
        Surface(color = MaterialTheme.colors.background) {
            WindowCrankVolume(volume = 10, volumeChanged = {})
        }
    }
}