package com.example.taskflow.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taskflow.ui.components.FilterDropdown
import com.example.taskflow.ui.navigation.Routes

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val userEmail by homeViewModel.userEmail.collectAsState()
    val tasks by homeViewModel.filteredTasks.collectAsState()

    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var editingTaskId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        homeViewModel.loadTasks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Қош келдіңіз, ${userEmail ?: "Пайдаланушы"}!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = homeViewModel.searchQuery.collectAsState().value,
            onValueChange = { homeViewModel.setSearchQuery(it) },
            label = { Text("Іздеу...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сүзгі: ")
            Spacer(modifier = Modifier.width(8.dp))
            FilterDropdown(
                selected = homeViewModel.filterDone.collectAsState().value,
                onSelected = { homeViewModel.setFilterDone(it) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newTitle,
            onValueChange = { newTitle = it },
            label = { Text("Тапсырма тақырыбы") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = newDescription,
            onValueChange = { newDescription = it },
            label = { Text("Тапсырма сипаттамасы") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (newTitle.isNotBlank()) {
                    if (editingTaskId != null) {
                        val updatedTask = tasks.find { it.id == editingTaskId }?.copy(
                            title = newTitle.trim(),
                            description = newDescription.trim()
                        )
                        if (updatedTask != null) {
                            homeViewModel.updateTask(updatedTask)
                            editingTaskId = null
                        }
                    } else {
                        homeViewModel.addTask(newTitle.trim(), newDescription.trim())
                    }
                    newTitle = ""
                    newDescription = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editingTaskId != null) "Жаңарту" else "Қосу")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Тапсырмалар", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        tasks.forEach { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = { homeViewModel.toggleTaskDone(task) }
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(task.title, style = MaterialTheme.typography.bodyLarge)
                    if (task.description.isNotBlank()) {
                        Text(task.description, style = MaterialTheme.typography.bodySmall)
                    }
                }
                IconButton(onClick = {
                    editingTaskId = task.id
                    newTitle = task.title
                    newDescription = task.description
                }) {
                    Icon(Icons.Default.Edit, contentDescription = "Өңдеу")
                }
                IconButton(onClick = {
                    homeViewModel.deleteTask(task.id)
                    if (editingTaskId == task.id) {
                        editingTaskId = null
                        newTitle = ""
                        newDescription = ""
                    }
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Жою")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { navController.navigate(Routes.PROFILE) }) {
                Text("Профиль")
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                homeViewModel.logout()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Шығу")
        }
    }
}