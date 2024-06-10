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

class MachineSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_machine_search)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val edtMachineId: EditText = findViewById(R.id.edtMachineId)
        val btnOkMachineSearch: Button = findViewById(R.id.btnOkMachineSearch)

        btnOkMachineSearch.setOnClickListener {
            val machineId = edtMachineId.text.toString()
            if (machineId.isNotEmpty()) {
                val intent = Intent(this, MachineActivity::class.java)
                intent.putExtra("machineId", machineId)
                startActivity(intent)
            }
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