package com.example.taskflow.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _userEmail = MutableStateFlow<String?>(firebaseAuth.currentUser?.email)
    val userEmail: StateFlow<String?> = _userEmail

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filterDone: MutableStateFlow<FilterDone> = MutableStateFlow(FilterDone.ALL)
    val filterDone: StateFlow<FilterDone> = _filterDone

    val filteredTasks: StateFlow<List<Task>> = combine(_allTasks, _searchQuery, _filterDone) { tasks, query, filter ->
        tasks.filter { task ->
            val matchesQuery = task.title.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                FilterDone.ALL -> true
                FilterDone.DONE -> task.isDone
                FilterDone.NOT_DONE -> !task.isDone
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var tasksListener: ListenerRegistration? = null

    enum class FilterDone {
        ALL, DONE, NOT_DONE
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterDone(filter: FilterDone) {
        _filterDone.value = filter
    }

    fun loadTasks() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        tasksListener?.remove()

        tasksListener = firestore.collection("users")
            .document(userId)
            .collection("tasks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val tasksList = snapshot?.documents?.map { doc ->
                    Task(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        isDone = doc.getBoolean("isDone") == true
                    )
                } ?: emptyList()

                _allTasks.value = tasksList
            }
    }

    fun addTask(title: String, description: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
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

    fun toggleTaskDone(task: Task) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(userId)
            .collection("tasks")
            .document(task.id)
            .update("isDone", !task.isDone)
    }

    fun updateTask(task: Task) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(userId)
            .collection("tasks")
            .document(task.id)
            .update(
                mapOf(
                    "title" to task.title,
                    "description" to task.description,
                    "isDone" to task.isDone
                )
            )
    }

    fun deleteTask(taskId: String) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(userId)
            .collection("tasks")
            .document(taskId)
            .delete()
    }

    fun logout() {
        firebaseAuth.signOut()
        _userEmail.value = null
        tasksListener?.remove()
    }
}