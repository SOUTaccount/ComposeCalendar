package com.example.composecalendar.calendar

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Created by Vladimir Stebakov on 07.06.2023
 */

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetLayout() {
    val coroutineScope = rememberCoroutineScope()
    val state =
        rememberBottomSheetScaffoldState(bottomSheetState = SheetState(skipPartiallyExpanded = true))

    BottomSheetScaffold(
        sheetContainerColor = Color.Green,
        sheetContentColor = Color.Red,
        sheetContent = {
            Text(text = "this bottom sheet")
        },
        scaffoldState = state
    ) {
        Text(text = "this content over bottom sheet")
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Button(modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .align(Alignment.Center), onClick = {
            coroutineScope.launch {
                if (state.bottomSheetState.isVisible) {
                    state.bottomSheetState.hide()
                } else {
                    state.bottomSheetState.show()
                }
            }
        }) {

        }
    }
}