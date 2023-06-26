package com.example.composecalendar.calendar

/**
 * Created by Vladimir Stebakov on 06.06.2023
 */

sealed interface CalendarDay {
    data class Free(val number: Int) : CalendarDay
    data class WithEvents(val number: Int, val events: List<Event>) : CalendarDay
}

sealed class CalendarState {
    object Progress : CalendarState()
    data class Success(val days: List<CalendarDay>) : CalendarState()
    data class Fail(val message: String) : CalendarState()
}