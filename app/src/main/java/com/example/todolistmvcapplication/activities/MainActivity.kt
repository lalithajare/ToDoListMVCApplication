package com.example.todolistmvcapplication.activities

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.todolistmvcapplication.R
import com.example.todolistmvcapplication.adapters.EventAdapter
import com.example.todolistmvcapplication.models.Event
import com.example.todolistmvcapplication.utils.MyApplication
import com.example.todolistmvcapplication.utils.Utils
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var mEventAdapter: EventAdapter
    private val mEventList = ArrayList<Event>()

    private lateinit var recyclerTodo: RecyclerView
    private lateinit var txtNoData: TextView
    private lateinit var btnAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.title = getString(R.string.to_do_list)
        initViews()
        setViews()
        setAdapter()
        setData()
    }


    private fun initViews() {
        recyclerTodo = findViewById(R.id.recycler_todo)
        btnAdd = findViewById(R.id.btn_add)
        txtNoData = findViewById(R.id.txt_no_data)
    }

    private fun setViews() {
        btnAdd.setOnClickListener(this)
    }

    private fun setAdapter() {
        mEventAdapter = EventAdapter(mEventList, this)
        recyclerTodo.adapter = mEventAdapter
        recyclerTodo.layoutManager = LinearLayoutManager(this)
    }


    private fun setData() {
        GetEventsTask(this).execute()
    }

    /**
     * This task is used to add the event in DB
     * Its used immediately after filled 'Event' object is returned by 'AddEventActivity' in 'onActivityResult()'
     */
    private class InsertEventTask(var context: MainActivity, var event: Event) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            MyApplication.getInstance().eventDB!!.eventDao().insertEvent(event)
            return true
        }

        override fun onPostExecute(bool: Boolean?) {
            if (bool!!) {
                Utils.showToast(context.getString(R.string.event_added_to_db))
            }
        }
    }

    /**
     * This task is used to update the event in DB
     * Its used immediately after filled 'Event' object is returned by 'EditEventActivity' in 'onActivityResult()'
     */
    private class UpdateEventTask(var context: MainActivity, var event: Event) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            MyApplication.getInstance().eventDB!!.eventDao().updateEvent(event)
            return true
        }

        override fun onPostExecute(bool: Boolean?) {
            if (bool!!) {
                Utils.showToast(context.getString(R.string.event_updated_in_db))
            }
        }
    }

    /**
     * This task is used to fill the events list at beginning
     */
    private class GetEventsTask(var context: MainActivity) : AsyncTask<Void, Void, List<Event>>() {
        override fun doInBackground(vararg params: Void?): List<Event> {
            return MyApplication.getInstance().eventDB!!.eventDao().getEvents()
        }

        override fun onPostExecute(eventList: List<Event>?) {
            if (eventList.isNullOrEmpty()) {
                context.txtNoData.visibility = View.VISIBLE
            } else {
                context.txtNoData.visibility = View.GONE
                context.mEventList.addAll(eventList)
                context.mEventAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card_layout -> {
                if (v.tag != null && v.tag is Event)
                    EditEventActivity.beginActivityForResult(this, v.tag as Event)
            }

            R.id.btn_add -> {
                AddEventActivity.beginActivityForResult(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && data != null && data.hasExtra(AddEventActivity.EVENT_OBJ)
            && data.getSerializableExtra(AddEventActivity.EVENT_OBJ) != null
        ) {
            if (requestCode == AddEventActivity.REQ_ADD_EVENT) {
                val event = data.getSerializableExtra(AddEventActivity.EVENT_OBJ) as Event
                mEventList.add(event)
                mEventAdapter.notifyItemInserted(mEventList.lastIndex)
                txtNoData.visibility = View.GONE
                InsertEventTask(this, event).execute()
            } else if (requestCode == EditEventActivity.REQ_EDIT_EVENT) {
                val event = data.getSerializableExtra(AddEventActivity.EVENT_OBJ) as Event

                for (i in 0 until mEventList.size) {
                    if (mEventList[i].id == event.id) {
                        mEventList.removeAt(i)
                        mEventList.add(i, event)
                        mEventAdapter.notifyItemChanged(i)
                        UpdateEventTask(this, event).execute()
                        break
                    }
                }

            }

        }
    }


}
