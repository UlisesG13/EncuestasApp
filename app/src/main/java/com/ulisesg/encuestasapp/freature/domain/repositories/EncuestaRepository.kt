package com.ulisesg.encuestasapp.freature.domain.repositories

import com.ulisesg.encuestasapp.freature.domain.entities.Encuesta

interface EncuestaRepository {
    suspend fun getEncuestas(): List<Encuesta>
    suspend fun getPoll(id: String): Encuesta
    suspend fun createPoll(question: String, options: List<String>): Encuesta
    suspend fun votePoll(pollId: String, optionIndex: Int): Encuesta
}
