package com.example.todolistmvcapplication.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.provider.ContactsContract.CommonDataKinds.Note
import com.example.todolistmvcapplication.models.Event
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Query


@Dao
interface EventDao {
    @Insert
    fun insertEvent(event: Event): Long?

    @Query("SELECT * FROM events")
    fun getEvents(): List<Event>
}