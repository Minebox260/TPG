package com.example.tpg.ui.recyclerview

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpg.R
import com.example.tpg.classes.Maintenance
import com.example.tpg.ui.ProfileActivity
import com.squareup.picasso.Picasso

class MaintenanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val userInfos = itemView.findViewById<LinearLayout>(R.id.userInfos)
    private val userEmail = itemView.findViewById<TextView>(R.id.txtUserEmail)
    private val userImg = itemView.findViewById<ImageView>(R.id.imgUserIcon)
    private val description = itemView.findViewById<TextView>(R.id.txtMaintenanceDescription)
    private val maintenanceImage = itemView.findViewById<ImageView>(R.id.maintenanceImage)
    private val machineModel = itemView.findViewById<TextView>(R.id.txtMachineModel)
    private val machineBrand = itemView.findViewById<TextView>(R.id.txtMachineBrand)
    private val machineId = itemView.findViewById<TextView>(R.id.txtMachineId)

    fun bind(maintenance: Maintenance) {
        userEmail.text = maintenance.profile.email
        if (!maintenance.profile.profile_icon_url.isNullOrEmpty()) {
            Picasso.get().load(maintenance.profile.profile_icon_url).into(userImg)
        } else {
            userImg.setImageResource(R.drawable.default_user)
        }
        description.text = maintenance.description
        machineModel.text = maintenance.machine.model
        machineBrand.text = maintenance.machine.brand
        machineId.text = maintenance.machine.serial_number
        if (!maintenance.image_link.isNullOrEmpty()) {
            Picasso.get().load(maintenance.image_link).into(maintenanceImage)
        } else {
            maintenanceImage.setImageResource(R.drawable.no_image)
        }

        userInfos.setOnClickListener {
            val myIntent = Intent(itemView.context, ProfileActivity::class.java)
            myIntent.putExtra("userId", maintenance.profile.user_id)
            itemView.context.startActivity(myIntent)
        }
    }
}