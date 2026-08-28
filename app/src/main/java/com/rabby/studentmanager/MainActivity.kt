package com.rabby.studentmanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.rabby.studentmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel : StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvStudent.layoutManager = LinearLayoutManager(this)
        refreshUI()
        setUpButtons()

        binding.etSearch.addTextChangedListener {
            searchStudent()
        }

    }

    fun refreshUI(){

        adapterView(viewModel.allStudents())
        updateStatistics()
    }


    fun openEditScreen(student : Student) {

        val intent = Intent(this, Edit::class.java)

        intent.putExtra(
            "Student_Id",
            student.id
        )

        startActivity(intent)

    }

    fun setUpButtons(){

        binding.btnAll.setOnClickListener {
            refreshUI()
        }

        binding.btnPassed.setOnClickListener {

            val passList = viewModel.allStudents().filter {
                it.marks >= 40
            }

           adapterView(passList)
        }

        binding.btnFailed.setOnClickListener {

            val failList = viewModel.allStudents().filter {
                it.marks < 40
            }

           adapterView(failList)

        }

        binding.btnAdd.setOnClickListener {

            startActivity(Intent(this, AddStudent :: class.java))
        }
    }


    fun updateStatistics(){

        val list = viewModel.allStudents()

        val topper = list.maxByOrNull { it.marks }
        binding.topperName.text =
            topper?.let { "${it.name}" } ?: "N/A"

        binding.topperMarks.text =
            topper?.let { "${it.marks} Marks" } ?: "00"

        val avg =  if (list.isNotEmpty())
            list.map { it.marks }.average()

        else 0.0

        binding.averageMarks.text =
            "%.2f".format(avg)

        binding.totalStudents.text =
            "${list.size}"

    }

    fun searchStudent(){

        val input = binding.etSearch.text.toString().trim()

        val filterListed = viewModel.allStudents().filter {
            it.name.contains(input,ignoreCase = true)
        }

        adapterView(filterListed)

    }



    fun adapterView(list : List<Student>) {


        binding.rvStudent.adapter = StudentAdapter(

            list,

            onDeleteClick = { student ->
                viewModel.deleteStudent(student.id)
                refreshUI()
            },

            onEditClick = { student ->
                openEditScreen(student)
            }
        )

    }



    override fun onResume() {
        super.onResume()

        binding.etSearch.clearFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(binding.etSearch.windowToken,0)

        refreshUI()

    }


}