package com.example.tpg.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpg.R
import com.example.tpg.classes.Machine
import com.example.tpg.classes.Maintenance
import com.example.tpg.data.DataProvider
import com.example.tpg.ui.recyclerview.MaintenanceAdapter
import com.squareup.picasso.Picasso
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class MaintenanceActivity : BaseActivity() {
    private val adapter = MaintenanceAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_maintenance, findViewById(R.id.content_frame), true)

        val refItems = findViewById<RecyclerView>(R.id.recyclerViewMaintenances)
        refItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        refItems.adapter = adapter

        val userEmail = intent.getStringExtra("email")
        val machineId = intent.getStringExtra("machineId")

        getMaintenances(userEmail, machineId)
    }

    private fun getMaintenances(userEmail: String?, machineId: String?) {

        lifecycleScope.launch {
            val columns = Columns.raw("""
                id,
                created_at,
                description,
                image_link,
                machines!inner(*),
                profiles!inner(*)
            """.trimIndent())
            var maintenances: List<Maintenance>
            if (!userEmail.isNullOrEmpty()) {
                maintenances = DataProvider.supabase.from("maintenances").select(
                    columns = columns
                ) {
                    filter {
                        eq("profiles.email", userEmail)
                    };
                    order(column = "created_at", order = Order.DESCENDING)
                }.decodeList<Maintenance>()
            } else if (!machineId.isNullOrEmpty()) {
                maintenances = DataProvider.supabase.from("maintenances").select(
                    columns = columns
                ) {
                    filter {
                        eq("machines.serial_number", machineId)
                    };
                    order(column = "created_at", order = Order.DESCENDING)
                }.decodeList<Maintenance>()
            } else {
                maintenances = DataProvider.supabase.from("maintenances").select(
                    columns = columns
                ) {
                    order(column = "created_at", order = Order.DESCENDING)
                }.decodeList<Maintenance>()
            }

            if (maintenances.isNotEmpty()) {
                adapter.show(maintenances)
            } else {
                Toast.makeText(this@MaintenanceActivity, "Aucune maintenance trouvée", Toast.LENGTH_SHORT).show()
            }
        }
    }
}