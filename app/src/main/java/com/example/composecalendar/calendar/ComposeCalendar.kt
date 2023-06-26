package com.example.composecalendar.calendar

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composecalendar.ui.theme.Blue
import kotlinx.coroutines.launch

/**
 * Created by Vladimir Stebakov on 06.06.2023
 */

@Preview
@Composable
private fun ComposeCalendarPreview() {
    ComposeCalendar()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ComposeCalendar() {
    val db = EventsDB.getInstance(context = LocalContext.current)
    val viewModel = ComposeCalendarViewModel(db)
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
    val scaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val pagerState = rememberPagerState(initialPage = viewModel.fetchCurrentMonth())
    val currentDay = remember { mutableStateOf(-1) }
    val showSheetContent = remember { mutableStateOf(false) }
//    val flowOfCalendar = rememberFlow(flow = viewModel.flowOfCalendarState)
//    val stateOfCalendar = flowOfCalendar.collectAsState(initial = CalendarState.Progress)
    val stateOfCalendar = viewModel.flowOfCalendarState.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        when (stateOfCalendar.value) {
            is CalendarState.Progress -> {
                CircularProgressIndicator(modifier = Modifier
                    .width(40.dp)
                    .height(40.dp))
            }

            is CalendarState.Success -> {

//                BottomSheetEvents(state = scaffoldState, viewModel = viewModel, day = currentDay)
                BottomSheetScaffold(
                    sheetContainerColor = Blue,
                    sheetContent = {
                        if (showSheetContent.value){
                            LazyColumn(modifier = Modifier.fillMaxWidth().height(300.dp), content = {
                                item {
                                    ElevatedButton(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        onClick = {
                                            viewModel.addNewEvent(currentDay.value)
                                            Log.d("TES12412421","on click $currentDay")
                                        },
                                    ) {
                                        Text(text = "Add new event")
                                    }
                                }
                                items(viewModel.fetchEventsForCurrentDay(currentDay.value-1, stateOfCalendar)){ event->
                                    Text(text = "$")
                                }
                            })
                        }
                    },
                    scaffoldState = scaffoldState,
                ) {
                    ComposeCalendarView(
                        viewModel = viewModel,
                        onDayClick = { day->
                            coroutineScope.launch {
                                currentDay.value = day
                                showSheetContent.value = true
                                if(bottomSheetState.hasExpandedState){
                                    bottomSheetState.hide()
                                } else {
                                    bottomSheetState.expand()
                                }
//                                scaffoldState.bottomSheetState.collapseOrExpand()
                            }
                        },
                        pagerState = pagerState,
                        listOfDays = (stateOfCalendar.value as CalendarState.Success).days,
                        onNextClick = {
                            coroutineScope.launch {
                                showSheetContent.value = false
                                pagerState.scrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        onPreviousClick = {
                            coroutineScope.launch {
                                showSheetContent.value = false
                                pagerState.scrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    )
                }
            }

            is CalendarState.Fail -> {

            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ComposeCalendarView(
    viewModel: ComposeCalendarViewModel,
    onDayClick: (Number: Int) -> Unit,
    pagerState: PagerState,
    listOfDays: List<CalendarDay>,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit
) {
    Column {
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            pageCount = 12,
            userScrollEnabled = false
        ) { page ->
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(24.dp),
                    text = viewModel.fetchCurrentMonthName(page),
                    color = Color.Blue,
                    textAlign = TextAlign.Center,
                )
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(6),
                    verticalArrangement = Arrangement.SpaceAround,
                    contentPadding = PaddingValues(4.dp)
                ) {
                    viewModel.fetchDaysForMonth(month = page)
                    items(listOfDays) { day ->
                        when (day) {
                            is CalendarDay.Free -> {
                                ClickableText(
                                    text = AnnotatedString("${day.number}"),
                                    style = TextStyle(textAlign = TextAlign.Center),
                                    onClick = {
                                        onDayClick(day.number)
                                    }
                                )
                            }

                            is CalendarDay.WithEvents -> {
                                ClickableText(
                                    text = AnnotatedString("${day.number}"),
                                    style = TextStyle(textAlign = TextAlign.Center, color = Color.Red),
                                    onClick = {
                                        onDayClick(day.number)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        Row {
            Button(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(0.5f)
                    .width(60.dp),
                onClick = {
                    onPreviousClick()
                }) {
                Text(text = "prev page")
            }
            Button(modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.5f)
                .width(60.dp),
                onClick = {
                    onNextClick()
                }) {
                Text(text = "next page")
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetEvents(
    state: BottomSheetScaffoldState,
    viewModel: ComposeCalendarViewModel,
    day: Int
) {
    BottomSheetScaffold(
        sheetContainerColor = Blue,
        sheetContent = {
            LazyColumn(content = {
                item {
                    ElevatedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = {
                            viewModel.addNewEvent(day)
                            Log.d("TES12412421","on click $day")
                        },
                    ) {
                        Text(text = "Add new event")
                    }
                }
            })
        },
        scaffoldState = state,
    ) {}
}