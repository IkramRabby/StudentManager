package com.rabby.studentmanager

object StudentRepository {

   val students = mutableListOf<Student>()

    var studentId = 1

    fun add(name: String,marks: Int){
        students.add(
            Student(
                name = name,
                marks = marks,
                id = studentId++
            )
        )

    }

    fun all () : List<Student>{
        return students.toList()
    }

    fun getStudentById(id: Int) : Student? {
       return students.find { it.id == id }
    }

    fun updateStudent(id: Int,name: String,marks: Int){

        val index = students.indexOfFirst {
            it.id == id
        }

        if (index != -1){
            students[index] = Student(
                id = id,
                name = name,
                marks = marks
            )
        }

    }

    fun deleteStudent( id: Int){
        students.removeIf {
            it.id == id
        }


    }

}