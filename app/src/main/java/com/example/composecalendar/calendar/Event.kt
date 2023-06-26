package com.example.composecalendar.calendar

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Vladimir Stebakov on 11.06.2023
 */

/**
 * [key] - Calendar.Day + Calendar.Month + Calendar.Year
 */

@Entity(tableName = "events")
data class Event(
    @PrimaryKey val key: Int,
    val date: String,
    val time: String,
    val task: String
)