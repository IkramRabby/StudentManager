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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rabby.studentmanager.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel : StudentViewModel by viewModels()

    private val studentAdapter = StudentAdapter(
        mutableListOf(),
        onDeleteClick = {student ->
            viewModel.deleteStudent(student.id)
        },
        onEditClick = {student ->
            openEditScreen(student)

        }
    )

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
        binding.rvStudent.adapter = studentAdapter

        observeFilteredStudents()
        setUpButtons()


        binding.etSearch.addTextChangedListener { name ->
            viewModel.searchStudent(name.toString().trim())
        }


    }

    private fun  observeFilteredStudents(){

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel.filteredStudents.collect {
                    studentAdapter.updateList(it)
                    updateStatistics()
                }
            }


        }

    }

    private fun openEditScreen(student : Student) {

        val intent = Intent(this, Edit::class.java)

        intent.putExtra(
            "Student_Id",
            student.id
        )

        startActivity(intent)

    }

    private fun setUpButtons(){

        binding.btnAll.setOnClickListener {
         viewModel.filterStudents("ALL")
        }

        binding.btnPassed.setOnClickListener {

          viewModel.filterStudents("PASSED")
        }

        binding.btnFailed.setOnClickListener {

            viewModel.filterStudents("FAILED")
        }

        binding.btnAdd.setOnClickListener {

            startActivity(Intent(this, AddStudent :: class.java))
        }


    }


    private fun updateStatistics(){

        val stats = viewModel.calculateStatistics()

        binding.topperName.text = "${stats.topperName}"
        binding.topperMarks.text = "${stats.topperMarks}"
        binding.averageMarks.text = "%.2f".format(stats.averageMarks)
        binding.totalStudents.text ="${stats.totalStudents}"

    }

    override fun onResume() {
        super.onResume()

        binding.etSearch.clearFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(binding.etSearch.windowToken,0)



    }


}