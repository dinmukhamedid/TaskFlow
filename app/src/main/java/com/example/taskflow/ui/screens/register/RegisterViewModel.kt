package com.example.taskflow.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository = AuthRepository(
        firebaseAuth = FirebaseAuth.getInstance()
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            val result = authRepository.register(email, password)
            _uiState.value = if (result.isSuccess) {
                RegisterUiState.Success
            } else {
                RegisterUiState.Error(result.exceptionOrNull()?.message ?: "Белгісіз қате")
            }
        }
    }
}

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}
