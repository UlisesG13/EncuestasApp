package com.ulisesg.encuestasapp.freature.presentation.screens

import com.ulisesg.encuestasapp.freature.domain.entities.Encuesta

data class EncuestasUiState(
    val isLoading: Boolean = false,
    val encuestas: List<Encuesta> = emptyList(),
    val errorMessage: String? = null
)
