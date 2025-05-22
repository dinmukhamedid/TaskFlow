package com.example.taskflow.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskflow.data.model.Task

@Composable
fun TaskCard(
    task: Task,
    onToggleDone: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (task.isDone) Color(0xFFD3F9D8) else Color(0xFFFFF3CD))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
            Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "User ID: ${task.userId}", style = MaterialTheme.typography.labelSmall)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onToggleDone) {
                    Text(if (task.isDone) "Белгісіз ету" else "Аяқталды деп белгілеу")
                }

                TextButton(onClick = onDelete) {
                    Text("Өшіру", color = Color.Red)
                }
            }
        }
    }
}
