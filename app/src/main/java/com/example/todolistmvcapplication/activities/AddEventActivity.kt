package com.example.todolistmvcapplication.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import com.example.todolistmvcapplication.R
import com.example.todolistmvcapplication.models.Event
import com.example.todolistmvcapplication.utils.DateFormatter
import com.example.todolistmvcapplication.utils.Utils
import java.util.*
import android.widget.TimePicker
import android.app.TimePickerDialog
import java.text.ParseException


class AddEventActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    companion object {
        private const val REQ_ADD_EVENT = 34
        const val EVENT_OBJ = "event_object"

        fun beginActivity(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, AddEventActivity::class.java))
        }

        fun beginActivityForResult(activity: AppCompatActivity) {
            activity.startActivityForResult(Intent(activity, AddEventActivity::class.java), REQ_ADD_EVENT)
        }
    }

    private var mSelectedDate: Date? = null

    private lateinit var edtEventName: EditText
    private lateinit var txtEventTime: TextView
    private lateinit var btnSelectTime: Button
    private lateinit var edtEventDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        actionBar?.title = getString(R.string.add_event)
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
        btnSelectTime.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val dialog = DatePickerDialog(
            this, this,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.setTitle("Select Date")
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
        mSelectedDate = calendar.time
        showTimePicker()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(
            this, this, calendar.get(Calendar.HOUR_OF_DAY)
            , calendar.get(Calendar.MINUTE), true
        )
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.time = mSelectedDate
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        mSelectedDate = calendar.time

        txtEventTime.text = DateFormatter.getStringFromDate(mSelectedDate!!,DateFormatter.dd_MM_yyyy_HH_mm)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_event, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun prepareEventModel(): Event? {
        //Check for empty event name
        if (edtEventName.text.toString().trim().isEmpty()) {
            Utils.showToast(getString(R.string.plz_enter_event_name))
            return null
        }

        //Check for proper event time format
        try {
            DateFormatter.getDateFromString(DateFormatter.dd_MM_yyyy_HH_mm, txtEventTime.text.toString().trim())
        } catch (ex: ParseException) {
            Utils.showToast(getString(R.string.plz_select_proper_event_time))
            return null
        }

        //Check for empty event description
        if (edtEventDescription.text.toString().trim().isEmpty()) {
            Utils.showToast(getString(R.string.plz_enter_event_description))
            return null
        }

        //Prepare and return event object
        return Event(
            0,
            edtEventName.text.toString().trim(),
            DateFormatter.getDateFromString(
                DateFormatter.dd_MM_yyyy_HH_mm
                , txtEventTime.text.toString()
            ),
            edtEventDescription.text.toString().trim()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_done) {
            val event = prepareEventModel()
            if (event != null) {
                intent.putExtra(EVENT_OBJ, event)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
