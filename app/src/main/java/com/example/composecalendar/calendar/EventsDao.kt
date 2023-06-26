package com.example.composecalendar.calendar

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Vladimir Stebakov on 11.06.2023
 */

@Dao
interface EventsDao {

    @Query("SELECT * FROM events WHERE `date` LIKE :date")
    suspend fun fetchEventsByData(date: String): List<Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEvent(event: Event)
}