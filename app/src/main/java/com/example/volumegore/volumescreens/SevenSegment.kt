package com.example.volumegore.volumescreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            val digits = volume.toString().padStart(3, '0').toCharArray().map { it.digitToInt() }
            val states: List<MutableList<Boolean>> = digits.map {
                remember {
                    it.toSegmentList().toMutableStateList()
                }
            }
            for (state in states) {
                Column {
                    SevenSegmentDigit(
                        state,
                        { index, updateState ->
                            state[index] = updateState
                        },
                        Modifier.padding(horizontal = 10.dp)
                    )
                    val digit = segmentsToNums[state.map { it }]?.toString() ?: "ERROR"
                    Text(digit, Modifier.align(Alignment.CenterHorizontally))
                }
            }

            val newVolDigits = states.mapNotNull { state -> segmentsToNums[state.map { it }] }
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
private val numsToSegments = (0..9).associateWith { it.toSegmentList() }

@ExperimentalStdlibApi
private val segmentsToNums = numsToSegments.entries.associate { (key, value) -> value to key }

@ExperimentalStdlibApi
private fun Int.toSegmentList(): List<Boolean> {
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
            SevenSegmentDigit(digit.toSegmentList(), { _, _ -> })
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
