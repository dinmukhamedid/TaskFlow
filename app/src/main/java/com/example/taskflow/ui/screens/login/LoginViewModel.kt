package com.example.taskflow.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository(
        firebaseAuth = FirebaseAuth.getInstance()
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val result = authRepository.login(email, password)
            _uiState.value = if (result.isSuccess) {
                val role = result.getOrNull() ?: "user"
                LoginUiState.Success(role)
            } else {
                LoginUiState.Error(result.exceptionOrNull()?.message ?: "Белгісіз қате")
            }
        }
    }

    sealed class LoginUiState {
        object Idle : LoginUiState()
        object Loading : LoginUiState()
        data class Success(val role: String) : LoginUiState()
        data class Error(val message: String) : LoginUiState()
    }
}

