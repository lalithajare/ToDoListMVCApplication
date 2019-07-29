package com.example.todolistmvcapplication

import com.example.todolistmvcapplication.daos.EventDao
import com.example.todolistmvcapplication.models.Event
import com.example.todolistmvcapplication.utils.MyApplication
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.mockito.Mock;
import java.util.*

class EventDaoTest {
    //Non-zero Long return value, to test whether values are inserting
    @Test
    fun insertEventTest() {
        val event = Event(0)
        event.eventName = "TestEvent"
        event.eventTime = Date()
        event.eventDescription = "xyz event is awesome"
        assertNotEquals(
            MyApplication.getInstance().eventDB!!.eventDao().insertEvent(
                event
            ), 0
        )
    }

    //Non-zero Int return value, to test whether values are updated
    @Test
    fun updateEventTest() {
        val event = Event(0)
        event.eventName = "TestEvent"
        event.eventTime = Date()
        event.eventDescription = "xyz event is awesome"
        assertNotEquals(
            MyApplication.getInstance().eventDB!!.eventDao().updateEvent(
                event
            ), 0
        )
    }

    //Whether the events in DB not empty
    @Test
    fun getEventsTest() {
        System.out.println("Number of events in DB : "+MyApplication.getInstance().eventDB!!.eventDao().getEvents().size)
        assertNotEquals(
            MyApplication.getInstance().eventDB!!.eventDao().getEvents().size, 0
        )
    }
}