package com.rabby.studentmanager


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object StudentRepository {

    private val _students = MutableStateFlow<List<Student>>(emptyList())

    val students : StateFlow<List<Student>> = _students.asStateFlow()


    private var studentId = 1

    fun add(name: String,marks: Int){

        val newStudent = Student(
            id = studentId++,
            name = name,
            marks = marks
        )

        _students.value = _students.value + newStudent

    }


    fun getStudentById(id: Int) : Student? {
       return _students.value.find { it.id == id }

    }



    fun updateStudent(id: Int,name: String,marks: Int){

        _students.value = _students.value.map { student ->

            if (student.id == id){
                Student(
                    id = id,
                    name = name,
                    marks = marks
                )

            } else {
                student
            }

        }

        }


    fun deleteStudent( id: Int){
        _students.value = _students.value.filter {
            it.id != id
        }

    }

}