package com.example.covid19tracker

import android.app.DatePickerDialog
import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.example.covid19tracker.dataClass.SlotModal
import com.example.covid19tracker.listView.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_vaccine.*
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class VaccineActivity : AppCompatActivity() {
    lateinit var centerList: List<SlotModal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine)
        supportActionBar?.hide()
        centerList = ArrayList()

        idBtnSearch.setOnClickListener {
            val pinCode = idEdtPinCode.text.toString()
            if (pinCode.length != 6) {
                Toast.makeText(this, "Please Enter A Valid PinCode", Toast.LENGTH_SHORT).show()
            } else {
                (centerList as ArrayList).clear()
                val c = Calendar.getInstance()
                // on below line we are getting our current year, month and day.
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(
                    this,
                    { _, year, monthOfYear, dayOfMonth ->
//                    loadingPB.setVisibility(View.VISIBLE)

                        // on below line we are creating a date string for our date
                        val dateStr= """$dayOfMonth - ${monthOfYear + 1} - $year"""

                        // on below line we are calling a method to get
                        // the appointment info for vaccination centers
                        // and we are passing our pin code to it.
                        getAppointments(pinCode, dateStr)
                    },
                    year,
                    month,
                    day)
                dpd.show()
            }
        }
    }

    private fun getAppointments(pinCode: String, date: String) {
        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pinCode + "&date=" + date
        val queue = Volley.newRequestQueue(this)

        // on below line we are creating a request
        // variable for making our json object request.
        val request =
            // as we are getting json object response and we are making a get request.
            JsonObjectRequest(Request.Method.GET, url, null, { response ->

                // this method is called when we get successful response from API.
                Log.e("TAG", "SUCCESS RESPONSE IS $response")
                // we are setting the visibility of progress bar as gone.
//                loadingPB.setVisibility(View.GONE)
                // on below line we are adding a try catch block.


                try {
                    // in try block we are creating a variable for center
                    // array and getting our array from our object.
                    val centerArray = response.getJSONArray("centers")

                    // on below line we are checking if the length of the array is 0.
                    // the zero length indicates that there is no data for the given pincode.
                    if (centerArray.length() == 0) {
                        Toast.makeText(this, "No Center Found", Toast.LENGTH_SHORT).show()
                    }
                    for (i in 0 until centerArray.length()) {

                        // on below line we are creating a variable for our center object.
                        val centerObj = centerArray.getJSONObject(i)

                        // on below line we are getting data from our session
                        // object and we are storing that in a different variable.
                        val centerName: String = centerObj.getString("name")
                        val centerAddress: String = centerObj.getString("address")
                        val centerFromTime: String = centerObj.getString("from")
                        val centerToTime: String = centerObj.getString("to")
                        val fee_type: String = centerObj.getString("fee_type")

                        // on below line we are creating a variable for our session object
                        val sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                        val ageLimit: Int = sessionObj.getInt("min_age_limit")
                        val vaccineName: String = sessionObj.getString("vaccine")
                        val avaliableCapacity: Int = sessionObj.getInt("available_capacity")

                        // after extracting all the data we are passing this
                        // data to our modal class we have created
                        // a variable for it as center.
                        val center = SlotModal(
                            centerName,
                            centerAddress,
                            centerFromTime,
                            centerToTime,
                            fee_type,
                            ageLimit,
                            vaccineName,
                            avaliableCapacity
                        )
                        // after that we are passing this modal to our list on the below line.
                        centerList = centerList + center
                    }

                    // on the below line we are setting layout manager to our recycler view.
                    centersRV.layoutManager = LinearLayoutManager(this)

                    // on the below line we are setting an adapter to our recycler view.
                    centersRV.adapter = RecyclerViewAdapter(centerList)

                    // on the below line we are notifying our adapter as the data is updated.
                    centersRV.adapter!!.notifyDataSetChanged()

                } catch (e: JSONException) {
                    // below line is for handling json exception.
                    e.printStackTrace();
                }
            },
                { error ->
                    // this method is called when we get any
                    // error while fetching data from our API
                    Log.e("TAG", "RESPONSE IS $error")
                    // in this case we are simply displaying a toast message.
                    Toast.makeText(this, "Fail to get response", Toast.LENGTH_SHORT).show()
                })
        // at last we are adding
        // our request to our queue.
        queue.add(request)
    }
}