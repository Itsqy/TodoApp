package com.dicoding.todoapp.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    fun addTask(title: String, desc: String, date: Long) {
        val task = Task(
            title = title,
            description = desc,
            dueDateMillis = date
        )
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }
}