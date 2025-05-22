package com.example.taskflow

import TaskFlowTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.taskflow.ui.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode = remember { mutableStateOf(false) }

            TaskFlowTheme(isDarkTheme = isDarkMode.value) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavGraph(
                        isDarkMode = isDarkMode.value,
                        onToggleTheme = { isDarkMode.value = !isDarkMode.value }
                    )
                }
            }
        }
    }
}