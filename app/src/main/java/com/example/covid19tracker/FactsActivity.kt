package com.example.covid19tracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_facts.*

class FactsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facts)
        supportActionBar?.hide()
        symp_tv.setOnClickListener {
            startActivity(Intent(this,SymptomsActivity::class.java))
        }
        preven_tv.setOnClickListener {
            startActivity(Intent(this,PreventionActivity::class.java))
        }
        back.setOnClickListener {
            finish()
        }
    }
}