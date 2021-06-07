package com.example.covid19tracker.listView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19tracker.R
import com.example.covid19tracker.dataClass.SlotModal

class RecyclerViewAdapter(private val list:List<SlotModal>):RecyclerView.Adapter<RecyclerViewAdapter.DetailsViewHolder>(){

    inner class DetailsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val centerNameTV: TextView = itemView.findViewById(R.id.idTVCenterName)
        val centerAddressTV: TextView = itemView.findViewById(R.id.idTVCenterAddress)
        val centerTimings: TextView = itemView.findViewById(R.id.idTVCenterTimings)
        val vaccineNameTV: TextView = itemView.findViewById(R.id.idTVVaccineName)
        val centerAgeLimitTV: TextView = itemView.findViewById(R.id.idTVAgeLimit)
        val centerFeeTypeTV: TextView = itemView.findViewById(R.id.idTVFeeType)
        val avalabilityTV: TextView = itemView.findViewById(R.id.idTVAvaliablity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerViewAdapter.DetailsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_rv,parent,false)
        return DetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val currentItem=list[position]
        holder.apply {
            centerNameTV.text=currentItem.centerName
            holder.centerAddressTV.text = currentItem.centerAddress
            holder.centerTimings.text = ("From : " + currentItem.centerFromTime + " To : " + currentItem.centerToTime)
            holder.vaccineNameTV.text = currentItem.vaccineName
            holder.centerAgeLimitTV.text = "Age Limit : " + currentItem.ageLimit.toString()
            holder.centerFeeTypeTV.text = currentItem.fee_type
            holder.avalabilityTV.text = "Availability : " + currentItem.availableCapacity.toString()
        }
    }

    override fun getItemCount():Int=list.size

}