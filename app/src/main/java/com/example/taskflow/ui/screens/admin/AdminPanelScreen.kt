package com.example.taskflow.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.taskflow.ui.components.TaskCard
import com.example.taskflow.ui.navigation.Routes

@Composable
fun AdminPanelScreen(
    navController: NavController,
    viewModel: AdminViewModel = viewModel()
) {
    val tasks by viewModel.filteredTasks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var newUserEmail by remember { mutableStateOf("") }
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var emailToDelete by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadAllTasks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Admin панелі", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newUserEmail,
            onValueChange = { newUserEmail = it },
            label = { Text("Қолданушы email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = newTitle,
            onValueChange = { newTitle = it },
            label = { Text("Тапсырма атауы") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = newDescription,
            onValueChange = { newDescription = it },
            label = { Text("Сипаттама") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.addTaskForUser(
                    newUserEmail.trim(),
                    newTitle.trim(),
                    newDescription.trim()
                )
                newTitle = ""
                newDescription = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Тапсырма қосу")
        }

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = emailToDelete,
            onValueChange = { emailToDelete = it },
            label = { Text("Өшіру үшін email") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.deleteUserByEmail(emailToDelete.trim())
                emailToDelete = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Қолданушыны өшіру", color = MaterialTheme.colorScheme.onError)
        }


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text("Іздеу") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Барлық тапсырмалар:", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        tasks.forEach { task ->
            TaskCard(
                task = task,
                onToggleDone = { viewModel.toggleTaskDone(task) },
                onDelete = { viewModel.deleteTask(task) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate(Routes.PROFILE) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Профиль")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.logout()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.ADMIN) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Шығу", color = MaterialTheme.colorScheme.onError)
        }
    }
}
