package com.rabby.studentmanager


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rabby.studentmanager.databinding.ItemStudentBinding

class StudentAdapter(
    private val list: MutableList<Student>,
    private val onDeleteClick : (Student) -> Unit,
    private val onEditClick : (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    fun updateList(newList : List<Student>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        val binding: ItemStudentBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val students = list[position]

        holder.binding.studentName.text = students.name
        holder.binding.studentMarks.text = "Marks : ${students.marks}"

        if (students.marks >= 40) {
            holder.binding.tvResult.text = "Passed"
            holder.binding.tvResult.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.green)
            )

        } else {
            holder.binding.tvResult.text = "Failed"
            holder.binding.tvResult.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.red)
            )

        }

        holder.binding.buttonEdit.setOnClickListener {
            onEditClick(students)
        }

        holder.binding.buttonDelete.setOnClickListener {
            onDeleteClick(students)
        }
    }

    override fun getItemCount(): Int { return list.size }


}



