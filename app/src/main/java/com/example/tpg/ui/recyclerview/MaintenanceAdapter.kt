package com.example.tpg.ui.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tpg.classes.Maintenance
import com.example.tpg.R

class MaintenanceAdapter: RecyclerView.Adapter<MaintenanceViewHolder>() {
    private var dataSource = listOf<Maintenance>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        return MaintenanceViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.maintenance, parent, false)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun show(maintenanceList: List<Maintenance>) {
        dataSource = maintenanceList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.maintenance
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        holder.bind(dataSource[position])
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }
}