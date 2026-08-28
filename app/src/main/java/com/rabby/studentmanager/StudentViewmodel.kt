package com.rabby.studentmanager

import androidx.lifecycle.ViewModel

class StudentViewModel : ViewModel() {

    fun addStudent(name : String, marks : Int){
        StudentRepository.add(name,marks)
    }

    fun allStudents () : List<Student>{
        return StudentRepository.all()
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

}