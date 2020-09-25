package com.example.covid19tracker.listView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.covid19tracker.R
import com.example.covid19tracker.SpannableDelta
import com.example.covid19tracker.dataClass.StatewiseItem
import kotlinx.android.synthetic.main.list_item.view.*

class StateListAdapter(private val list: List<StatewiseItem>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        val item = list[position]

        view.confirmedTv.text = SpannableDelta(
            "${item.confirmed}\n ↑ ${item.deltaconfirmed ?: "0"}",
            "#D32F2F",
            item.confirmed?.length ?: 0
        )

        view.activeTv.text = SpannableDelta(
            "${item.active}\n ↑ ${item.deltaactive ?: "0"}",
            "#1976D2",
            item.confirmed?.length ?: 0
        )
        view.recoveredTv.text = SpannableDelta(
            "${item.recovered}\n ↑ ${item.deltarecovered ?: "0"}",
            "#388E3C",
            item.recovered?.length ?: 0
        )
        view.deceasedTv.text = SpannableDelta(
            "${item.deaths}\n ↑ ${item.deltadeaths ?: "0"}",
            "#FBC02D",
            item.deaths?.length ?: 0
        )
        view.stateTv.text = item.state
        return view
    }

    override fun getItem(position: Int) = list[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount(): Int = list.size

}