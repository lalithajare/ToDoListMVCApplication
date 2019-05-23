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
import com.example.todolistmvcapplication.database.EventDatabase
import com.example.todolistmvcapplication.models.Event
import com.example.todolistmvcapplication.utils.MyApplication
import com.example.todolistmvcapplication.utils.Prefs
import com.example.todolistmvcapplication.utils.Utils
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

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
        btnAdd.setOnClickListener {
            AddEventActivity.beginActivityForResult(this)
        }
    }

    private fun setAdapter() {
        mEventAdapter = EventAdapter(mEventList)
        recyclerTodo.adapter = mEventAdapter
        recyclerTodo.layoutManager = LinearLayoutManager(this)
    }


    private fun setData() {
        GetEventsFromDb(this).execute()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && data != null && data.hasExtra(AddEventActivity.EVENT_OBJ)
            && data.getSerializableExtra(AddEventActivity.EVENT_OBJ) != null
        ) {
            val event = data.getSerializableExtra(AddEventActivity.EVENT_OBJ) as Event
            mEventList.add(event)
            mEventAdapter.notifyItemInserted(mEventList.lastIndex)
            txtNoData.visibility = View.GONE

            InsertTask(this, event).execute()

        }
    }

    private class InsertTask(var context: MainActivity, var event: Event) : AsyncTask<Void, Void, Boolean>() {
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

    private class GetEventsFromDb(var context: MainActivity) : AsyncTask<Void, Void, List<Event>>() {
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
            /* if (Prefs.eventList.isNullOrEmpty()) {
                 txtNoData.visibility = View.VISIBLE
             } else {
                 txtNoData.visibility = View.GONE
                 mEventList.addAll(Prefs.eventList!!)
                 mEventAdapter.notifyDataSetChanged()
             }*/
        }
    }

}
