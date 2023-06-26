package com.example.composecalendar.calendar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Vladimir Stebakov on 11.06.2023
 */

@Database(entities = [Event::class], version = 1)
abstract class EventsDB : RoomDatabase() {
    abstract fun questionsDao(): EventsDao

    companion object {
        fun getInstance(context: Context): EventsDB =
            Room.databaseBuilder(context, EventsDB::class.java, "database").build()
    }
}