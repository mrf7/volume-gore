package com.example.volumegore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * This one updates the list on click but since the state handling framework sees the same reference
 * it doesnt know that the state is updated, so no recompose
 */
@Composable
fun StateOfMutableList() {
    // Tip: If we can cast to non mutable state we're doing it wrong. Need to call [MutableState].setValue to recompose
    val state: State<MutableList<Int>> = remember { mutableStateOf(listOf(1, 2, 3, 4).toMutableList()) }
    Column {
        state.value.forEachIndexed { index, num ->
            Text(num.toString(),
                Modifier
                    .clickable { state.value[index] += 1 }
                    .padding(15.dp)
                    .fillMaxWidth())
        }
    }
}

/**
 * This works because we're calling set on [MutableState.value] not  [MutableList.set], so it knows to recompose
 */
@Composable
fun ListOfMutableState() {
    val state: List<MutableState<Int>> = remember { listOf(1, 2, 3, 4).map { mutableStateOf(it) } }
    Column {
        state.forEachIndexed { index, num ->
            Text(num.value.toString(),
                Modifier
                    .clickable { state[index].value += 1 }
                    .padding(15.dp)
                    .fillMaxWidth())
        }
    }
}


/**
 * Think this is the proper way to handle. A subclass of mutable list that handles  state updates properly is built in
 * Gets proper recomposition and can treat the object as a mutable list so you dont have to think about it
 */
@Composable
fun MutableStateList() {
    val state: SnapshotStateList<Int> = remember {
        mutableStateListOf(1, 2, 3, 4)
    }
    // Can upcast to use as mutable list
    val nums = state as MutableList<Int>
    Column {
        nums.forEachIndexed { index, num ->
            Text(num.toString(),
                Modifier
                    .clickable { state[index] += 1 }
                    .padding(15.dp)
                    .fillMaxWidth())
        }
    }
}
