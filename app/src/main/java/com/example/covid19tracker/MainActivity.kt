package com.example.covid19tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AbsListView

import com.example.covid19tracker.dataClass.Response
import com.example.covid19tracker.dataClass.StatewiseItem
import com.example.covid19tracker.listView.StateListAdapter
import com.example.covid19tracker.networking.Client
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var stateListAdapter: StateListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        vaccine_btn.setOnClickListener {
            startActivity(Intent(this,VaccineActivity::class.java))
        }
        facts.setOnClickListener {
//            startActivity(Intent(this,FactsActivity::class.java))
        }
        list.addHeaderView(
            LayoutInflater.from(this)
                .inflate(R.layout.list_header, list, false)
        )
        //fetching
        fetchResponse()
        swipeToRefresh.setOnRefreshListener {
            fetchResponse()
        }
        list.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

                if (list.getChildAt(0) != null) {
                    swipeToRefresh.isEnabled =
                       list.firstVisiblePosition == 0 && list.getChildAt(0).top == 0
                }
            }

        })

    }

    private fun fetchResponse() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.clone().execute() }
            if (response.isSuccessful) {
                swipeToRefresh.isRefreshing = false
                val data = Gson().fromJson(response.body?.string(), Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedData(data.statewise[0])
                    bindStateWiseData(data.statewise.subList(0, data.statewise.size))
                }
            }
        }
    }

    private fun bindStateWiseData(subList: List<StatewiseItem>) {
        stateListAdapter = StateListAdapter(subList)
        list.adapter = stateListAdapter
    }

    private fun bindCombinedData(data: StatewiseItem) {
        confirmedTv?.text = data.confirmed
        activeTv?.text = data.active
       recoveredTv?.text = data.recovered
        deceasedTv?.text = data.deaths

        val lastUpdatedTime = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastUpdatedTv?.text = "Last Updated ${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"

    }

    private fun getTimeAgo(past: Date): String {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

        return when {
            seconds < 60 -> {
                "Few seconds ago"
            }
            minutes < 60 -> {
                "$minutes minutes ago"
            }
            hours < 24 -> {
                "$hours hour ${minutes % 60} min ago"
            }
            else -> {
                SimpleDateFormat("dd/MM/yy, hh:mm a").format(past).toString()
            }
        }
    }
}