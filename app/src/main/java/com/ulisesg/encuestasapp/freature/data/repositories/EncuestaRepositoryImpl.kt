package com.ulisesg.encuestasapp.freature.data.repositories

import com.ulisesg.encuestasapp.freature.data.datasources.remote.api.EncuestasApi
import com.ulisesg.encuestasapp.freature.data.datasources.remote.mapper.toEntity
import com.ulisesg.encuestasapp.freature.data.datasources.remote.models.CreatePollRequest
import com.ulisesg.encuestasapp.freature.data.datasources.remote.models.VoteRequest
import com.ulisesg.encuestasapp.freature.domain.entities.Encuesta
import com.ulisesg.encuestasapp.freature.domain.repositories.EncuestaRepository
import javax.inject.Inject

class EncuestaRepositoryImpl @Inject constructor(
    private val encuestasApi: EncuestasApi
) : EncuestaRepository {

    override suspend fun getEncuestas(): List<Encuesta> {
        return encuestasApi.getEncuestas().map { it.toEntity() }
    }

    override suspend fun getPoll(id: String): Encuesta {
        return encuestasApi.getPoll(id).toEntity()
    }

    override suspend fun createPoll(question: String, options: List<String>): Encuesta {
        val request = CreatePollRequest(question, options)
        return encuestasApi.createPoll(request).toEntity()
    }

    override suspend fun votePoll(pollId: String, optionIndex: Int): Encuesta {
        val request = VoteRequest(optionIndex)
        return encuestasApi.votePoll(pollId, request).toEntity()
    }
}
