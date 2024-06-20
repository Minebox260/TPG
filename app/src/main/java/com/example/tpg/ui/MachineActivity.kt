package com.example.tpg.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tpg.R
import com.example.tpg.classes.Machine
import com.example.tpg.data.DataProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class MachineActivity : AppCompatActivity() {
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_machine)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtMachineId: TextView = findViewById(R.id.txtMachineId)
        val txtRawOutput: TextView = findViewById(R.id.txtRawOutput)
        val txtMachineName: TextView = findViewById(R.id.txtMachineName)
        val txtMachineType: TextView = findViewById(R.id.txtMachineType)
        val txtMachineModel: TextView = findViewById(R.id.txtMachineModel)
        val txtMachineBrand: TextView = findViewById(R.id.txtMachineBrand)
        val txtMachineHoursUsed: TextView = findViewById(R.id.txtMachineHoursUsed)
        val txtMachineInterventionsCounts: TextView = findViewById(R.id.txtMachineInterventionsCounts)
        val txtMachineServiceStartDate: TextView = findViewById(R.id.txtMachineServiceStartDate)

        val machineId = intent.getStringExtra("machineId")

        if (machineId != null) {
            txtMachineId.text = machineId
            getMachine(machineId, txtRawOutput, txtMachineName, txtMachineType, txtMachineModel, txtMachineBrand, txtMachineHoursUsed, txtMachineInterventionsCounts, txtMachineServiceStartDate)
        }
    }

    private fun getMachine(serialNumber: String, output: TextView, machineName: TextView, machineType: TextView, machineModel: TextView, machineBrand: TextView, machineHoursUsed: TextView, machineInterventionsCounts: TextView, machineServiceStartDate: TextView) {
        lifecycleScope.launch {
            /*val responseString = DataProvider.retrofitService.getMachine(serial_number= "eq.$serialNumber")
            Log.d("PMR", responseString)
            output.text = responseString

            val listType = object : TypeToken<List<Machine>>() {}.type
            val machine = Gson().fromJson<List<Machine>>(responseString, listType).first()
            machineName.text = machine.name
            machineType.text = machine.type
            machineModel.text = machine.model
            machineBrand.text = machine.brand
            machineHoursUsed.text = machine.hours_used.toString()
            machineInterventionsCounts.text = machine.interventions_counts.toString()
            machineServiceStartDate.text = machine.service_start_date
            */

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