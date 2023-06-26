package com.example.composecalendar.calendar

import java.util.Calendar

/**
 * Created by Vladimir Stebakov on 06.06.2023
 */
data class CalendarMonth(
    val name: String,
    val monthId: Int
) {
    companion object {
        fun generateListOfMonthEnglish() = listOf(
            CalendarMonth(
                name = "JANUARY",
                monthId = Calendar.JANUARY
            ),
            CalendarMonth(
                name = "FEBRUARY",
                monthId = Calendar.FEBRUARY
            ),
            CalendarMonth(
                name = "MARCH",
                monthId = Calendar.MARCH
            ),
            CalendarMonth(
                name = "APRIL",
                monthId = Calendar.APRIL
            ),
            CalendarMonth(
                name = "MAY",
                monthId = Calendar.MAY
            ),
            CalendarMonth(
                name = "JUNE",
                monthId = Calendar.JUNE
            ),
            CalendarMonth(
                name = "JULY",
                monthId = Calendar.JULY
            ),
            CalendarMonth(
                name = "AUGUST",
                monthId = Calendar.AUGUST
            ),
            CalendarMonth(
                name = "SEPTEMBER",
                monthId = Calendar.SEPTEMBER
            ),
            CalendarMonth(
                name = "OCTOBER",
                monthId = Calendar.OCTOBER
            ),
            CalendarMonth(
                name = "NOVEMBER",
                monthId = Calendar.NOVEMBER
            ),
            CalendarMonth(
                name = "DECEMBER",
                monthId = Calendar.DECEMBER
            )
        )
    }
}