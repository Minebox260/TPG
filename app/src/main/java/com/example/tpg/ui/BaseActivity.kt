package com.example.tpg.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.tpg.R

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_base)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val maintenances: ImageView = findViewById(R.id.nav_maintenances)
        maintenances.setOnClickListener {
            val myIntent = Intent(this, ScannerActivity::class.java)
            startActivity(myIntent)
        }
        val scan: ImageView = findViewById(R.id.nav_scan)
        scan.setOnClickListener {
            val myIntent = Intent(this, ScannerActivity::class.java)
            startActivity(myIntent)
        }
        val history: ImageView = findViewById(R.id.nav_history)
        history.setOnClickListener {
            val myIntent = Intent(this, ScannerActivity::class.java)
            startActivity(myIntent)
        }
        val profile: ImageView = findViewById(R.id.nav_profile)
        profile.setOnClickListener {
            val myIntent = Intent(this, ScannerActivity::class.java)
            startActivity(myIntent)
        }
    }

}