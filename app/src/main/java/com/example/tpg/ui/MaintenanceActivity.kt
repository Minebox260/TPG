package com.example.tpg.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tpg.R
import com.example.tpg.classes.Maintenance
import com.example.tpg.data.DataProvider
import com.example.tpg.ui.recyclerview.MaintenanceAdapter
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MaintenanceActivity : BaseActivity() {
    private val adapter = MaintenanceAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_maintenance, findViewById(R.id.content_frame), true)

        setActionBarTitle(getString(R.string.maintenance))
        val refItems = findViewById<RecyclerView>(R.id.recyclerViewMaintenances)
        refItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        refItems.adapter = adapter

        val userId = intent.getStringExtra("userId")
        val machineId = intent.getStringExtra("machineId")

        getMaintenances(userId, machineId)
    }

    private fun getMaintenances(userId: String?, machineId: String?) {

        lifecycleScope.launch {
            try{
                val columns = Columns.raw("""
                    *,
                    machines(*),
                    profiles(*)
                """.trimIndent())
                val maintenances: List<Maintenance>
                if (!userId.isNullOrEmpty()) {
                    maintenances = DataProvider.supabase.from("maintenances").select(
                        columns = columns
                    ) {
                        filter {
                            eq("user_id", userId)
                        }
                        order(column = "created_at", order = Order.DESCENDING)
                    }.decodeList<Maintenance>()
                } else if (!machineId.isNullOrEmpty()) {
                    maintenances = DataProvider.supabase.from("maintenances").select(
                        columns = columns
                    ) {
                        filter {
                            eq("machine_id", machineId)
                        }
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
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MaintenanceActivity, getString(R.string.erreur), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}