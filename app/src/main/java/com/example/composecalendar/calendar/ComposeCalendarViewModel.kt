package com.example.composecalendar.calendar

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

/**
 * Created by Vladimir Stebakov on 06.06.2023
 */
class ComposeCalendarViewModel(private val roomDatabase: EventsDB) : ViewModel() {

    private val calendar: Calendar = Calendar.getInstance()

    private val listOfMonth = CalendarMonth.generateListOfMonthEnglish()

    val flowOfCalendarState = MutableStateFlow<CalendarState>(CalendarState.Success(emptyList()))
//
//    fun fetchDaysForMonth(month: Int): List<CalendarDay> {
//        calendar.set(Calendar.MONTH, month)
//        return (1..calendar.getActualMaximum(Calendar.DATE)).map { CalendarDay.Free(number = it) }
//    }

    fun fetchCurrentMonth() = calendar.get(Calendar.MONTH)

    fun fetchCurrentMonthName(index: Int) = listOfMonth[index].name

    fun fetchDaysForMonth(month: Int = calendar.get(Calendar.MONTH)) {
        calendar.set(Calendar.MONTH, month)
        viewModelScope.launch {
            val listOfCalendarDays = (1..calendar.getActualMaximum(Calendar.DATE)).map {
                val listOfEvents = roomDatabase.questionsDao().fetchEventsByData(
                    date = it.toString() + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + calendar.get(Calendar.YEAR)
                )
                if (listOfEvents.isNotEmpty()) {
                    CalendarDay.WithEvents(number = it, events = listOfEvents)
                } else {
                    CalendarDay.Free(number = it)
                }
            }
            if (listOfCalendarDays.isNotEmpty()) {
                flowOfCalendarState.tryEmit(CalendarState.Success(days = listOfCalendarDays))
            } else {
                flowOfCalendarState.tryEmit(CalendarState.Fail(message = "Something went wrong"))
            }
        }
    }

    fun addNewEvent(day: Int) {
        viewModelScope.launch {
            roomDatabase.questionsDao().addEvent(
                Event(
                    key = 13 + day + calendar.get(Calendar.MONTH) + calendar.get(Calendar.YEAR),
                    date = day.toString() + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + calendar.get(Calendar.YEAR),
                    time = "13",
                    task = ""
                )
            )
            fetchDaysForMonth()
        }
    }

    fun fetchEventsForCurrentDay(day: Int, state: State<CalendarState>): List<Event> {
        return if (state.value is CalendarState.Success && day != -1) {
            val days = (state.value as CalendarState.Success).days
            if (days.isNotEmpty()) {
                val currentDay = days[day]
                return if (currentDay is CalendarDay.WithEvents) {
                    currentDay.events
                } else {
                    emptyList()
                }
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun subscribeOnState(onNewState: (CalendarState) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            flowOfCalendarState.collect {
                onNewState(it)
            }
        }
    }
}