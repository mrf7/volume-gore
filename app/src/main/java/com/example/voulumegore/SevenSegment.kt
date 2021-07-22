package com.example.voulumegore

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.volumegore.VolumeChanged
import com.example.volumegore.ui.theme.volumeGoreTheme

@ExperimentalStdlibApi
@Composable
fun SevenSegment(volume: Int, volumeChanged: VolumeChanged) {
    Column {
        Row {
            val digits = volume.toString().toCharArray().map { it.digitToInt() }
            val digitsToDisplay = digits.takeIf { it.size > 1 } ?: listOf(0) + digits
            val states = digitsToDisplay.map {
                remember {
                    it.toSevenSegmentStateList()
                        .toMutableList()
                        .map { mutableStateOf(it) }
                }
            }
            for (state in states) {
                Column {
                    SevenSegmentDigit(
                        state.map { it.value },
                        { index, updateState ->
                            state[index].value = updateState
                        },
                        Modifier.padding(horizontal = 15.dp)
                    )
                    val digit = segmentsToNums[state.map { it.value }]?.toString() ?: "ERROR"
                    Text(digit, Modifier.align(Alignment.CenterHorizontally))
                }
            }

            val newVolDigits = states.mapNotNull { state -> segmentsToNums[state.map { it.value }] }
            if (newVolDigits.size == states.size) {
                var newVol = 0
                for (digit in newVolDigits) {
                    newVol = newVol * 10 + digit
                }
                volumeChanged(newVol)
            }
        }
    }
}

@ExperimentalStdlibApi
@Composable
private fun SevenSegmentDigit(
    segmentStates: List<Boolean>,
    onSegmentClick: (index: Int, updateState: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        HorizontalSegment(segmentStates[0],
            Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onSegmentClick(0, !segmentStates[0]) })
        Row {
            VerticalSegment(segmentStates[1], Modifier.clickable { onSegmentClick(1, !segmentStates[1]) })
            Box(modifier = Modifier.width(80.dp))
            VerticalSegment(segmentStates[2], Modifier.clickable { onSegmentClick(2, !segmentStates[2]) })
        }
        HorizontalSegment(segmentStates[3],
            Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onSegmentClick(3, !segmentStates[3]) })
        Row {
            VerticalSegment(segmentStates[4], Modifier.clickable { onSegmentClick(4, !segmentStates[4]) })
            Box(modifier = Modifier.width(80.dp))
            VerticalSegment(segmentStates[5], Modifier.clickable { onSegmentClick(5, !segmentStates[5]) })
        }
        HorizontalSegment(segmentStates[6],
            Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onSegmentClick(6, !segmentStates[6]) })
    }
}

@Composable
private fun VerticalSegment(active: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CutCornerShape(5.dp))
            .size(height = 100.dp, width = 15.dp)
            .background(if (active) Color.Black else Color.Gray)
    )
}

@Composable
private fun HorizontalSegment(active: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CutCornerShape(5.dp))
            .size(height = 15.dp, width = 100.dp)
            .background(if (active) Color.Black else Color.Gray)
    )
}

@ExperimentalStdlibApi
private val numsToSegments = (0..9).associateWith { it.toSevenSegmentStateList() }

@ExperimentalStdlibApi
private val segmentsToNums = numsToSegments.entries.associate { (key, value) -> value to key }

@ExperimentalStdlibApi
private fun Int.toSevenSegmentStateList(): List<Boolean> {
    return when (this) {
        0 -> {
            buildList {
                add(true)
                addAll(listOf(true, true))
                add(false)
                addAll(listOf(true, true))
                add(true)
            }
        }
        1 -> {
            buildList {
                add(false)
                addAll(listOf(false, true))
                add(false)
                addAll(listOf(false, true))
                add(false)
            }
        }
        2 -> {
            buildList {
                add(true)
                addAll(listOf(false, true))
                add(true)
                addAll(listOf(true, false))
                add(true)
            }
        }
        3 -> {
            buildList {
                add(true)
                addAll(listOf(false, true))
                add(true)
                addAll(listOf(false, true))
                add(true)
            }
        }
        4 -> {
            buildList {
                add(false)
                addAll(listOf(true, true))
                add(true)
                addAll(listOf(false, true))
                add(false)
            }
        }
        5 -> {
            buildList {
                add(true)
                addAll(listOf(true, false))
                add(true)
                addAll(listOf(false, true))
                add(true)
            }
        }
        6 -> {
            buildList {
                add(true)
                addAll(listOf(true, false))
                add(true)
                addAll(listOf(true, true))
                add(true)
            }
        }
        7 -> {
            buildList {
                add(true)
                addAll(listOf(false, true))
                add(false)
                addAll(listOf(false, true))
                add(false)
            }
        }
        8 -> List(7) { true }
        9 -> {
            buildList {
                add(true)
                addAll(listOf(true, true))
                add(true)
                addAll(listOf(false, true))
                add(true)
            }
        }
        else -> error("Number too big")
    }
}


@ExperimentalStdlibApi
//@Preview
@Composable
fun DigitPreview(@PreviewParameter(DigitParamProvider::class, 1) digit: Int) {
    volumeGoreTheme {
        Surface(color = MaterialTheme.colors.background) {
            SevenSegmentDigit(digit.toSevenSegmentStateList(), { _, _ -> })
        }
    }
}

@ExperimentalStdlibApi
//@Preview
@Composable
fun SevenSegmentPreview() {
    volumeGoreTheme {
        Surface(color = MaterialTheme.colors.background) {
            SevenSegment(49) {}
        }
    }
}

class DigitParamProvider : PreviewParameterProvider<Int> {
    override val values: Sequence<Int>
        get() = (0..10).toList().asSequence()
}
