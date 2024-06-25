package com.example.tpg.ui.recyclerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpg.R
import com.example.tpg.classes.Maintenance
import com.squareup.picasso.Picasso

class MaintenanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val userEmail = itemView.findViewById<TextView>(R.id.txtUserEmail)
    private val description = itemView.findViewById<TextView>(R.id.txtMaintenanceDescription)
    private val maintenanceImage = itemView.findViewById<ImageView>(R.id.maintenanceImage)
    private val machineModel = itemView.findViewById<TextView>(R.id.txtMachineModel)
    private val machineBrand = itemView.findViewById<TextView>(R.id.txtMachineBrand)
    private val machineId = itemView.findViewById<TextView>(R.id.txtMachineId)

    fun bind(maintenance: Maintenance) {
        userEmail.text = maintenance.profile.email
        description.text = maintenance.description
        machineModel.text = maintenance.machine.model
        machineBrand.text = maintenance.machine.brand
        machineId.text = maintenance.machine.serial_number
        if (!maintenance.image_link.isNullOrEmpty()) {
            Picasso.get().load(maintenance.image_link).into(maintenanceImage)
        } else {
            maintenanceImage.setImageResource(R.drawable.no_image)
        }
    }
}