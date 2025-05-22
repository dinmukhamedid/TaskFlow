package com.example.taskflow.data.model

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val userId: String = "",
    val isDone: Boolean = false
)
