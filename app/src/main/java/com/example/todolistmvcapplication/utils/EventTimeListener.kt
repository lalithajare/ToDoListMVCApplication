package com.example.todolistmvcapplication.utils

import com.example.todolistmvcapplication.models.Event

interface EventTimeListener {
    fun onTimeSet(event: Event)
}