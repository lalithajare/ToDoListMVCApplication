package com.example.todolistmvcapplication.viewholders

import android.view.View
import android.widget.TextView
import com.example.todolistmvcapplication.R
import com.example.todolistmvcapplication.models.Event
import com.example.todolistmvcapplication.utils.DateFormatter

class EventViewHolder(itemView: View) : ViewHolder(itemView) {

    private var mEvent: Event? = null

    private val txtEventName: TextView = itemView.findViewById(R.id.txt_event_name)
    private val txtEventTime: TextView = itemView.findViewById(R.id.txt_event_time)
    private val txtEventDescription: TextView = itemView.findViewById(R.id.txt_event_description)

    override fun onBindView(model: Any) {
        mEvent = model as Event?

        if (!mEvent?.eventName.isNullOrEmpty())
            txtEventName.text = mEvent?.eventName

        if (mEvent?.eventTime != null)
            txtEventTime.text = DateFormatter.getStringFromDate(mEvent?.eventTime!!, DateFormatter.dd_MM_yyyy_HH_mm)

        if (!mEvent?.eventDescription.isNullOrEmpty())
            txtEventDescription.text = mEvent?.eventDescription
    }
}