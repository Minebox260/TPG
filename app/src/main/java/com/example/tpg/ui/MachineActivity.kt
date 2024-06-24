package com.example.tpg.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.tpg.R
import com.example.tpg.classes.Machine
import com.example.tpg.data.DataProvider
import com.squareup.picasso.Picasso
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class MachineActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_machine, findViewById(R.id.content_frame), true)

        val txtID: TextView = findViewById(R.id.txtID)
        val txtMarque: TextView = findViewById(R.id.txtMarque)
        val txtModele: TextView = findViewById(R.id.txtModele)
        val txtHorodatage: TextView = findViewById(R.id.txtHorodatage)
        val txtType: TextView = findViewById(R.id.txtType)
        val txtProvenance: TextView = findViewById(R.id.txtProvenance)
        val txtNbMaintenances: TextView = findViewById(R.id.txtNbMaintenances)
        val txtDateMiseEnService: TextView = findViewById(R.id.txtDateMiseEnService)
        val imageMachine: ImageView = findViewById(R.id.machineImage)

        val machineId = intent.getStringExtra("machineId")
        val maintenancesBtn: Button = findViewById(R.id.maintenances_button)
        val ajouterMaintenanceBtn: Button = findViewById(R.id.nouvelle_maintenance_button)

        if (machineId != null) {
            txtID.text = machineId
            getMachine(
                machineId,
                txtMarque,
                txtModele,
                txtHorodatage,
                txtType,
                txtProvenance,
                txtNbMaintenances,
                txtDateMiseEnService,
                imageMachine)
        }

        maintenancesBtn.setOnClickListener {
            val intent = Intent(this, MaintenanceActivity::class.java)
            intent.putExtra("machineId", machineId)
            startActivity(intent)
        }

        ajouterMaintenanceBtn.setOnClickListener {
            val intent = Intent(this, CreateMaintenanceActivity::class.java)
            intent.putExtra("machineId", machineId)
            startActivity(intent)
        }
    }

    private fun getMachine(
        serialNumber: String,
        txtMarque: TextView,
        txtModele: TextView,
        txtHorodatage: TextView,
        txtType: TextView,
        txtProvenance: TextView,
        txtNbMaintenances: TextView,
        txtDateMiseEnService: TextView,
        imageMachine: ImageView) {

        lifecycleScope.launch {
            val machine = DataProvider.supabase.from("machines").select {
                filter {
                    Machine::serial_number eq serialNumber
                }
            }.decodeSingleOrNull<Machine>()

            if (machine != null) {
                txtMarque.text = machine.brand
                txtModele.text = machine.model
                txtHorodatage.text = machine.hours_used.toString()
                txtType.text = machine.type
                txtProvenance.text = machine.origin
                txtNbMaintenances.text = machine.interventions_count.toString()
                txtDateMiseEnService.text = machine.service_start_date
                Picasso.get().load(machine.photo_link).into(imageMachine)
            } else {
                Toast.makeText(this@MachineActivity, "Impossible de trouver la machine", Toast.LENGTH_SHORT).show()
                finish()
                val intent = Intent(this@MachineActivity, ScannerActivity::class.java)
                startActivity(intent)

            }
        }
    }
}