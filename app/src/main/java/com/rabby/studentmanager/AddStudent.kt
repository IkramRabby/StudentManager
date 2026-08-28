package com.rabby.studentmanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import com.rabby.studentmanager.databinding.ActivityAddStudentBinding

class AddStudent : AppCompatActivity() {

    private lateinit var binding: ActivityAddStudentBinding

    private val viewModel : StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnAddStudent.setOnClickListener {

            binding.addNameLayout.error = null
            binding.addMarksLayout.error = null

            val name = binding.etStudentName.text.toString().trim()
            val marks = binding.etStudentMarks.text.toString().toIntOrNull()

            if (name.isBlank()){
                binding.addNameLayout.error = "Enter student name"
                return@setOnClickListener
            }

            if (marks == null){
                binding.addMarksLayout.error = "Enter student marks"
                return@setOnClickListener
            }

            if ((marks > 100) || (marks < 0)){
                binding.addMarksLayout.error = "Please enter a valid score between 1 and 100"
                return@setOnClickListener
            }

            viewModel.addStudent(
                name,
                marks
            )

            Toast.makeText(this,"Add successfully", Toast.LENGTH_SHORT).show()
            finish()
        }




    }
}