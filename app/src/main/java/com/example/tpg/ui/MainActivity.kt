package com.example.tpg.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tpg.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnScannerQRCode: Button = findViewById(R.id.btnScannerQRCode)
        val btnChercherMachine: Button = findViewById(R.id.btnChercherMachine)
        val btnAppelerExpert: Button = findViewById(R.id.btnAppelerExpert)

        btnScannerQRCode.setOnClickListener {
            val myIntent = Intent(this, ScannerActivity::class.java)
            startActivity(myIntent)
        }

        btnChercherMachine.setOnClickListener {
            val myIntent = Intent(this, MachineSearchActivity::class.java)
            startActivity(myIntent)
        }

        btnAppelerExpert.setOnClickListener {
            val myIntent = Intent(this, ExpertCallActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val myIntent = Intent(this, SettingsActivity::class.java)
        startActivity(myIntent)

        return super.onOptionsItemSelected(item)
    }
}