package com.example.tpg.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.tpg.R
import com.example.tpg.classes.InsertMaintenance
import com.example.tpg.classes.MaintenanceId
import com.example.tpg.data.DataProvider
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class CreateMaintenanceActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_create_maintenance, findViewById(R.id.content_frame), true)

        setActionBarTitle(getString(R.string.ajout_de_maintenance))

        val operateurEmailTextView = findViewById<TextView>(R.id.txtOperateurEmail)
        val machineIdTextView = findViewById<TextView>(R.id.txtMachineId)
        val btnCreerMaintenance = findViewById<Button>(R.id.creer_maintenance_button)
        val edtDescription = findViewById<EditText>(R.id.edtDescription)

        val machineId = intent.getStringExtra("machineId")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val email = sharedPreferences.getString("email", "")

        operateurEmailTextView.text = email
        machineIdTextView.text = machineId

        btnCreerMaintenance.setOnClickListener {
            createMaintenance(email, machineId, edtDescription.text.toString())
        }

    }

    private fun createMaintenance(email: String?, machineId: String?, description: String) {
        if (email != null && machineId != null) {
            lifecycleScope.launch {
                val userId = DataProvider.supabase.auth.retrieveUserForCurrentSession().id

                val MaintenanceId = DataProvider.supabase.from("maintenances").select(columns = Columns.list("id")) {
                    order(column = "id", order = Order.DESCENDING)
                }.decodeSingle<MaintenanceId>().id + 1

                val maintenance = InsertMaintenance(MaintenanceId, description, userId, machineId)

                DataProvider.supabase.from("maintenances").insert(maintenance)

                val intent = Intent(this@CreateMaintenanceActivity, MaintenanceActivity::class.java)
                intent.putExtra("machineId", machineId)
                startActivity(intent)
            }
        }
    }
}