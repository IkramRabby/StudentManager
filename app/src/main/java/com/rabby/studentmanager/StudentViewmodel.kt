package com.rabby.studentmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


class StudentViewModel : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    private val _filterType = MutableStateFlow("ALL")

    val filteredStudents : StateFlow<List<Student>>  =
        combine(
            StudentRepository.students,
            _searchQuery,
            _filterType

        ){ students,query,filter ->

            var result = students

            //query

            if (query.isNotBlank()){
                result = result.filter {
                    it.name.contains(
                        query,
                        true
                    )
                }
            }

            //filter

            result = when(filter){

                "ALL" -> result

                "PASSED" -> result.filter {
                    it.marks >= 40
                }

                "FAILED" -> result.filter {
                    it.marks < 40
                }

                else -> result

            }

            result

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addStudent(name : String, marks : Int){
        StudentRepository.add(name,marks)

    }

    fun getStudentById(id : Int) : Student? {
       return StudentRepository.getStudentById(id)
    }

    fun updateStudent(id: Int,name: String,marks: Int){
        StudentRepository.updateStudent(
            id = id,
            name = name,
            marks = marks
        )
    }

    fun deleteStudent( id : Int ){
        StudentRepository.deleteStudent(id)
    }

    fun searchStudent(name : String){

        _searchQuery.value = name
    }

    fun filterStudents(type : String){

        _filterType.value = type
    }

    fun calculateStatistics() : StudentStats {

        val students = StudentRepository.students.value

        val topper = students.maxByOrNull { it.marks }
        val topperName = topper?.let { it.name } ?: "N/A"
        val topperMarks = topper?.let { it.marks } ?: 0

        val avg = if (students.isNotEmpty())
           students.map { it.marks }.average()

        else 0.0

        val totalStudents = students.size

        return StudentStats(
            topperName = topperName,
            topperMarks = topperMarks,
            averageMarks = avg,
            totalStudents = totalStudents
        )
    }
}

data class StudentStats(
    val topperName: String,
    val topperMarks: Int,
    val averageMarks: Double,
    val totalStudents: Int
)


