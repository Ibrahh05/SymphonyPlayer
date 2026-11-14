package com.example.loggin.viewmodels

sealed interface RegistroUiEvent {
    object NavigateBackToLogin : RegistroUiEvent
    data class ShowError(val messageResId: Int) : RegistroUiEvent
}