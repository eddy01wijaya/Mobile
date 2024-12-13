package com.eddy01wijaya.bcafhive.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.eddy01wijaya.bcafhive.R
import com.eddy01wijaya.bcafhive.dao.RecruitmentDao
import com.eddy01wijaya.bcafhive.model.Recruitment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Adapter
class RecruitmentAdapter(private val recruitmentList: List<Recruitment>,
                         private val onUpdateApproval: (Recruitment, String) -> Unit
) :
    RecyclerView.Adapter<RecruitmentAdapter.RecruitmentViewHolder>() {

    inner class RecruitmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtRecruitmentName: TextView = view.findViewById(R.id.txtRecruitmentName)
        val txtOpenDate: TextView = view.findViewById(R.id.txtOpenDate)
        val txtCloseDate: TextView = view.findViewById(R.id.txtClosedDate)
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
        val btnReject: Button = view.findViewById(R.id.btnReject)
        val txtCurrentStatus: TextView = view.findViewById(R.id.txtCurrentStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecruitmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recruitment, parent, false)
        return RecruitmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecruitmentViewHolder, position: Int) {
        val recruitment = recruitmentList[position]
        holder.txtRecruitmentName.text = recruitment.recruitmentName
        // holder.txtRequirements.text = recruitment.requirements // Uncomment if needed
        holder.txtOpenDate.text = recruitment.openDate
        holder.txtCloseDate.text = recruitment.closeDate
        holder.txtCurrentStatus.visibility = View.GONE


        // Update the status based on recruitment.approval
        holder.txtStatus.text = when (recruitment.approval) {
            "P" -> "Pending"
            "A" -> "Approved"
            "R" -> "Rejected"
            else -> "Unknown Status"
        }

        // Hide or show buttons based on approval status
        if (recruitment.approval == "A" || recruitment.approval == "R") {
            // If approved or rejected, hide the buttons
            holder.btnApprove.visibility = View.GONE
            holder.btnReject.visibility = View.GONE
            if (recruitment.approval == "A") {
                holder.txtCurrentStatus.text = "✔️"
            } else {
                holder.txtStatus.setTextColor(Color.RED)
                holder.txtCurrentStatus.text = "❌"
            }
            holder.txtCurrentStatus.visibility = View.VISIBLE
        } else {
            // If pending, show both buttons
            holder.btnApprove.visibility = View.VISIBLE
            holder.btnReject.visibility = View.VISIBLE
        }

        // Approve button
        holder.btnApprove.setOnClickListener {
            val context = holder.itemView.context // Context berasal dari ViewHolder
            AlertDialog.Builder(context).apply {
                setTitle("Confirmation")
                setMessage("Are you sure you want to approve this recruitment?")
                setPositiveButton("Yes") { _, _ ->
                    // Handle approval logic
                    onUpdateApproval(recruitment, "A")
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }

        // Reject button
        holder.btnReject.setOnClickListener {
            val context = holder.itemView.context // Context berasal dari ViewHolder
            AlertDialog.Builder(context).apply {
                setTitle("Confirmation")
                setMessage("Are you sure you want to reject this recruitment?")
                setPositiveButton("Yes") { _, _ ->
                    // Handle rejection logic
                    onUpdateApproval(recruitment, "R")
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }
    }




    override fun getItemCount(): Int = recruitmentList.size
}

