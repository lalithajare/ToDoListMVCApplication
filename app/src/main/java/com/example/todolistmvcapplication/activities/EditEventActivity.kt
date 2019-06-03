package com.example.todolistmvcapplication.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.example.todolistmvcapplication.R
import com.example.todolistmvcapplication.databinding.ActivityAddEventBinding
import com.example.todolistmvcapplication.databinding.ActivityEditEventBinding
import com.example.todolistmvcapplication.models.Event
import com.example.todolistmvcapplication.utils.DateFormatter
import com.example.todolistmvcapplication.utils.DateTimePickerHandler
import com.example.todolistmvcapplication.utils.EventTimeListener
import com.example.todolistmvcapplication.utils.Utils
import java.text.ParseException
import java.util.*

class EditEventActivity : AppCompatActivity(), EventTimeListener {

    companion object {
        const val REQ_EDIT_EVENT = 35
        const val EVENT_OBJ = "event_object"

        fun beginActivity(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, EditEventActivity::class.java))
        }

        fun beginActivityForResult(
            activity: AppCompatActivity,
            event: Event
        ) {
            val intent = Intent(activity, EditEventActivity::class.java)
            intent.putExtra(EVENT_OBJ, event)
            activity.startActivityForResult(intent, REQ_EDIT_EVENT)
        }
    }

    private lateinit var mEvent: Event

    private lateinit var edtEventName: EditText
    private lateinit var txtEventTime: TextView
    private lateinit var btnSelectTime: Button
    private lateinit var edtEventDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val handler = DateTimePickerHandler(this)
        mEvent = intent.getSerializableExtra(EVENT_OBJ) as Event
        val activityBinding = DataBindingUtil.setContentView<ActivityEditEventBinding>(this, R.layout.activity_edit_event)
        activityBinding.event = mEvent
        activityBinding.handler = handler

        actionBar?.title = getString(R.string.edit_event)
        initViews()
        setViews()
    }

    private fun initViews() {
        edtEventName = findViewById(R.id.edt_event_name)
        txtEventTime = findViewById(R.id.txt_event_time)
        btnSelectTime = findViewById(R.id.btn_select_time)
        edtEventDescription = findViewById(R.id.edt_event_description)
    }

    private fun setViews() {
        if (mEvent.eventName!!.isNotEmpty()) {
            edtEventName.setText(mEvent.eventName)
        }
        if (mEvent.eventDescription!!.isNotEmpty()) {
            edtEventDescription.setText(mEvent.eventDescription)
        }
        if (mEvent.eventTime != null) {
            txtEventTime.text = DateFormatter.getStringFromDate(mEvent.eventTime!!, DateFormatter.dd_MM_yyyy_HH_mm)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_event, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun prepareEventModel(): Boolean {
        //Check for empty event name
        if (mEvent.eventName.isNullOrEmpty()) {
            Utils.showToast(getString(R.string.plz_enter_event_name))
            return false
        }

        //Check for proper event time format
        if (mEvent.eventTime == null) {
            Utils.showToast(getString(R.string.plz_select_proper_event_time))
            return false
        }

        //Check for empty event description
        if (mEvent.eventDescription.isNullOrEmpty()) {
            Utils.showToast(getString(R.string.plz_enter_event_description))
            return false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_done) {
            if(prepareEventModel()) {
                intent.putExtra(EVENT_OBJ, mEvent)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTimeSet(event: Event) {
//        mEvent = event
    }
}
