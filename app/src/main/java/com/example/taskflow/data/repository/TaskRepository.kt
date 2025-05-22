package com.example.taskflow.data.repository

import com.example.taskflow.data.model.Task

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: String)
}

