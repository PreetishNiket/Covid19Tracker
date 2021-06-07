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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var stateListAdapter: StateListAdapter
    var isOpen =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val fabOpen = AnimationUtils.loadAnimation(
            this,
            R.anim.fab_open
        )
        val fabClose = AnimationUtils.loadAnimation(
            this,
            R.anim.fab_close
        )
        val fabRClockwise = AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_clockwise
        )
        val fabRAntiClockwise = AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_anticlockwise
        )
        Tracker_fab.setOnClickListener {
            if(isOpen){
                About_fab.startAnimation(fabClose)
                Symptoms_fab.startAnimation(fabClose)
                Essentials_fab.startAnimation(fabClose)
                Prevention_fab.startAnimation(fabClose)
                Vaccine_fab.startAnimation(fabClose)

                //visibility//
                About_fab.visibility = View.INVISIBLE
                Symptoms_fab.visibility = View.INVISIBLE
                Essentials_fab.visibility = View.INVISIBLE
                Prevention_fab.visibility = View.INVISIBLE
                Vaccine_fab.visibility=View.INVISIBLE
                //clickable

                About_fab.isClickable = false
                Symptoms_fab.isClickable = false
                Essentials_fab.isClickable = false
                Prevention_fab.isClickable = false
                Vaccine_fab.isClickable=false
                //disabling when fab close

                About_fab.isEnabled = false
                Symptoms_fab.isEnabled = false
                Essentials_fab.isEnabled = false
                Prevention_fab.isEnabled = false
                Vaccine_fab.isEnabled=false

                Tracker_fab.startAnimation(fabRClockwise)
                isOpen = false

            }else{
                About_fab.startAnimation(fabOpen)
                Symptoms_fab.startAnimation(fabOpen)
                Essentials_fab.startAnimation(fabOpen)
                Prevention_fab.startAnimation(fabOpen)
                Vaccine_fab.startAnimation(fabOpen)
                Tracker_fab.startAnimation(fabRAntiClockwise)
                ////

                About_fab.visibility = View.VISIBLE
                Symptoms_fab.visibility = View.VISIBLE
                Essentials_fab.visibility = View.VISIBLE
                Prevention_fab.visibility = View.VISIBLE
                Vaccine_fab.visibility = View.VISIBLE
                //

                About_fab.isClickable = true
                Symptoms_fab.isClickable = true
                Essentials_fab.isClickable = true
                Prevention_fab.isClickable = true
                Vaccine_fab.isClickable=true

                ///////////
                About_fab.isEnabled = true
                Symptoms_fab.isEnabled = true
                Essentials_fab.isEnabled = true
                Prevention_fab.isEnabled = true
                Vaccine_fab.isEnabled=true
                isOpen = true
            }
            About_fab.setOnClickListener {
//                val intent = Intent(this, AboutActivity::class.java)
//                startActivity(intent)
            }
            Symptoms_fab.setOnClickListener {
//                val intent = Intent(this, SymptomActivity::class.java)
//                startActivity(intent)
            }
            Essentials_fab.setOnClickListener {
//                val intent = Intent(this, EssentialActivity::class.java)
//                startActivity(intent)
            }
            Prevention_fab.setOnClickListener {
//                val intent = Intent(this, PreventionActivity::class.java)
//                startActivity(intent)
            }
            Vaccine_fab.setOnClickListener {
                startActivity(Intent(this,VaccineActivity::class.java))
            }

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
            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {

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
        confirmedTv.text = data.confirmed
        activeTv.text = data.active
        recoveredTv.text = data.recovered
        deceasedTv.text = data.deaths

        val lastUpdatedTime = data.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastUpdatedTv.text = "Last Updated ${getTimeAgo(simpleDateFormat.parse(lastUpdatedTime))}"

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