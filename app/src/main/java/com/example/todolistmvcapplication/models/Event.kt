package com.example.todolistmvcapplication.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.android.databinding.library.baseAdapters.BR
import com.example.todolistmvcapplication.utils.DataConverter
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@Entity(tableName = "events")
data class Event(@PrimaryKey(autoGenerate = true) @SerializedName("id") var id: Int) : Serializable, Observable {
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.add(callback)
    }

    @Ignore
    @Transient
    private var propertyChangeRegistry : PropertyChangeRegistry = PropertyChangeRegistry()


    @ColumnInfo(name = "event_name")
    @SerializedName("eventName")
    var eventName: String? = "XXXXX"
    @Bindable get
    set(value) {
        field = value
        propertyChangeRegistry.notifyChange(this, BR.eventName )
    }

    @ColumnInfo(name = "event_time")
    @SerializedName("eventTime")
    var eventTime: Date? = null
        @Bindable get
        set(value) {
            field = value
            propertyChangeRegistry.notifyChange(this, BR.eventTime )
        }

    @ColumnInfo(name = "event_description")
    @SerializedName("eventDescription")
    var eventDescription: String? = "XXXXX"
        @Bindable get
        set(value) {
            field = value
            propertyChangeRegistry.notifyChange(this, BR.eventDescription )
        }
}