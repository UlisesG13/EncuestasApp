package com.ulisesg.encuestasapp.freature.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulisesg.encuestasapp.freature.domain.entities.Encuesta
import com.ulisesg.encuestasapp.freature.domain.usescases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EncuestasViewModel @Inject constructor(
    private val getEncuestasUseCase: GetEncuestasUseCase,
    private val createPollUseCase: CreatePollUseCase,
    private val votePollUseCase: VotePollUseCase,
    private val getPollUseCase: GetPollUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<EncuestaUiState>(EncuestaUiState.Success(emptyList()))
    val uiState: StateFlow<EncuestaUiState> = _uiState

    // Flujo para notificar cuando una encuesta ha sido creada exitosamente
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun createPoll(question: String, options: List<String>) {
        viewModelScope.launch {
            _uiState.value = EncuestaUiState.Loading
            try {
                val newPoll = createPollUseCase(question, options)
                addPollToList(newPoll)
                _eventFlow.emit(UiEvent.PollCreated(newPoll))
            } catch (e: Exception) {
                _uiState.value = EncuestaUiState.Error(e.message ?: "Error al crear encuesta")
            }
        }
    }

    fun joinPoll(pollId: String) {
        viewModelScope.launch {
            _uiState.value = EncuestaUiState.Loading
            try {
                val poll = getPollUseCase(pollId)
                addPollToList(poll)
            } catch (e: Exception) {
                _uiState.value = EncuestaUiState.Error("No se encontró la encuesta con ID: $pollId")
            }
        }
    }

    private fun addPollToList(poll: Encuesta) {
        val currentState = _uiState.value
        val currentList = if (currentState is EncuestaUiState.Success) currentState.encuestas else emptyList()
        
        if (currentList.none { it.id == poll.id }) {
            _uiState.value = EncuestaUiState.Success(currentList + poll)
        } else {
            _uiState.value = EncuestaUiState.Success(currentList)
        }
    }

    fun vote(pollId: String, optionIndex: Int) {
        viewModelScope.launch {
            try {
                val updatedPoll = votePollUseCase(pollId, optionIndex)
                val currentState = _uiState.value
                if (currentState is EncuestaUiState.Success) {
                    val updatedList = currentState.encuestas.map {
                        if (it.id == updatedPoll.id) updatedPoll else it
                    }
                    _uiState.value = EncuestaUiState.Success(updatedList)
                }
            } catch (e: Exception) {
                _uiState.value = EncuestaUiState.Error("Error al votar: ${e.message}")
            }
        }
    }

    fun loadEncuestas() {
        // Implementación vacía
    }

    sealed class UiEvent {
        data class PollCreated(val poll: Encuesta) : UiEvent()
    }
}

sealed class EncuestaUiState {
    object Loading : EncuestaUiState()
    data class Success(val encuestas: List<Encuesta>) : EncuestaUiState()
    data class Error(val message: String) : EncuestaUiState()
}
