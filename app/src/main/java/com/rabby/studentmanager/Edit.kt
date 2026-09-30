package com.rabby.studentmanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rabby.studentmanager.databinding.ActivityEditBinding

class Edit : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    private val viewmodel : StudentViewModel by viewModels()

    private var studentId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        studentId = intent.getIntExtra(
            "Student_Id",
            -1
        )

        val students = viewmodel.getStudentById(studentId)

        students?.let {
            binding.etStudentName.setText(it.name)
            binding.etStudentMarks.setText(it.marks.toString())
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

            if (marks !in 0..100){
                binding.addMarksLayout.error = "Please enter a valid score between 0 and 100"
                return@setOnClickListener
            }



            viewmodel.updateStudent(
                studentId,
                name,
                marks
            )

            Toast.makeText(this,"Student Updated", Toast.LENGTH_SHORT).show()
            finish()
        }



    }
}