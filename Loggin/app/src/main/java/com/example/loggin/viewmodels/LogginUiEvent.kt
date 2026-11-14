package com.example.loggin.viewmodels

sealed interface LoginUiEvent {
    object NavigateToHome : LoginUiEvent
    data class ShowError(val messageResId: Int) : LoginUiEvent
}