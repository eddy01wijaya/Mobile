package com.eddy01wijaya.bcafhive

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eddy01wijaya.bcafhive.adapter.RecruitmentAdapter
import com.eddy01wijaya.bcafhive.viewmodel.RecruitmentViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: RecruitmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.lstRecruitmentt)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.recruitments.observe(this, Observer { recruitments ->
            val adapter = RecruitmentAdapter(
                recruitments,
                onUpdateApproval = { recruitment, newApproval ->
                    val updatedRecruitment = mapOf(
                        "id" to recruitment.idRecruitment.toString(),
                        "recruitment-name" to recruitment.recruitmentName,
                        "requirements" to recruitment.requirements,
                        "open-date" to recruitment.openDate,
                        "close-date" to recruitment.closeDate,
                        "approval" to newApproval
                    )
                    viewModel.updateRecruitment(
                        recruitment.idRecruitment,
                        updatedRecruitment,
                        onSuccess = {
                            Toast.makeText(this, "Recruitment $newApproval", Toast.LENGTH_SHORT).show()
                            viewModel.fetchRecruitments() // Refresh data
                        },
                        onError = { error ->
                            Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
            recyclerView.adapter = adapter
        })

// Ambil data awal
        viewModel.fetchRecruitments()


        val btnAll: Button = findViewById(R.id.btnAll);
        val btnApproved: Button = findViewById(R.id.btnApproved);
        val btnPending: Button = findViewById(R.id.btnPending);
        val btnRejected: Button = findViewById(R.id.btnRejected);

        setButtonActive(btnAll, btnPending, btnApproved, btnRejected)
        btnAll.setOnClickListener {
            setButtonActive(btnAll, btnPending, btnApproved, btnRejected)
            viewModel.fetchRecruitments()
        }

        btnPending.setOnClickListener {
            setButtonActive(btnPending, btnApproved, btnAll , btnRejected)
            viewModel.fetchRecruitmentsPending()
        }

        btnApproved.setOnClickListener {
            setButtonActive(btnApproved, btnAll, btnPending, btnRejected)
            viewModel.fetchRecruitmentsApproved()
        }

        btnRejected.setOnClickListener {
            setButtonActive(btnRejected, btnAll, btnPending, btnApproved)
            viewModel.fetchRecruitmentsRejected()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Function to set the active button and reset others
    private fun setButtonActive(activeButton: Button, vararg otherButtons: Button) {
        activeButton.isSelected = true
        activeButton.setBackgroundResource(R.drawable.button_rounded_active)
        activeButton.setTextColor(resources.getColor(R.color.backgroundColor))

        // Reset other buttons
        for (button in otherButtons) {
            button.isSelected = false
            button.setTextColor(resources.getColor(R.color.textColor))
            button.setBackgroundResource(R.drawable.button_rounded)
        }
    }
}