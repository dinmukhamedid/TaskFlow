package com.example.taskflow.ui.screens.admin

import androidx.lifecycle.ViewModel
import com.example.taskflow.data.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class AdminViewModel(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val filteredTasks: StateFlow<List<Task>> = combine(_allTasks, _searchQuery) { tasks, query ->
        tasks.filter { task ->
            task.title.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun loadAllTasks() {
        firestore.collectionGroup("tasks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val tasksList = snapshot?.documents?.mapNotNull { doc ->
                    val title = doc.getString("title")
                    val description = doc.getString("description")
                    val isDone = doc.getBoolean("isDone") ?: false
                    val userId = doc.reference.path.split("/")[1]

                    if (title != null && description != null) {
                        Task(
                            id = doc.id,
                            title = title,
                            description = description,
                            isDone = isDone,
                            userId = userId
                        )
                    } else null
                } ?: emptyList()

                _allTasks.value = tasksList
            }
    }

    fun toggleTaskDone(task: Task) {
        firestore.collection("users")
            .document(task.userId)
            .collection("tasks")
            .document(task.id)
            .update("isDone", !task.isDone)
    }

    fun deleteTask(task: Task) {
        firestore.collection("users")
            .document(task.userId)
            .collection("tasks")
            .document(task.id)
            .delete()
    }

    fun addTaskForUser(userEmail: String, title: String, description: String) {
        firestore.collection("users")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val userDoc = querySnapshot.documents.firstOrNull()
                val userId = userDoc?.id ?: return@addOnSuccessListener

                val taskData = hashMapOf(
                    "title" to title,
                    "description" to description,
                    "isDone" to false
                )

                firestore.collection("users")
                    .document(userId)
                    .collection("tasks")
                    .add(taskData)
            }
    }

    fun deleteUserByEmail(userEmail: String) {
        firestore.collection("users")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val userDoc = querySnapshot.documents.firstOrNull() ?: return@addOnSuccessListener
                val userId = userDoc.id

                // 1. Барлық тапсырмаларды өшіру
                firestore.collection("users")
                    .document(userId)
                    .collection("tasks")
                    .get()
                    .addOnSuccessListener { tasksSnapshot ->
                        for (taskDoc in tasksSnapshot.documents) {
                            taskDoc.reference.delete()
                        }

                        // 2. Қолданушыны өшіру
                        firestore.collection("users")
                            .document(userId)
                            .delete()
                    }
            }
    }


    fun logout() {
        firebaseAuth.signOut()
    }
}
