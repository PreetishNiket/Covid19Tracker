package com.example.covid19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class VaccineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine)
        supportActionBar?.hide()
    }
}