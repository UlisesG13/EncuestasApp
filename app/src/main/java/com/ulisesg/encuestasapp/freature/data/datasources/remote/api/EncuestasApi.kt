package com.ulisesg.encuestasapp.freature.data.datasources.remote.api

import com.ulisesg.encuestasapp.freature.data.datasources.remote.models.CreatePollRequest
import com.ulisesg.encuestasapp.freature.data.datasources.remote.models.EncuestaDto
import com.ulisesg.encuestasapp.freature.data.datasources.remote.models.VoteRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EncuestasApi {

    @GET("api/polls")
    suspend fun getEncuestas(): List<EncuestaDto>

    @POST("api/polls")
    suspend fun createPoll(
        @Body request: CreatePollRequest
    ): EncuestaDto

    @GET("api/polls/{pollId}")
    suspend fun getPoll(
        @Path("pollId") pollId: String
    ): EncuestaDto

    @POST("api/polls/{pollId}/vote")
    suspend fun votePoll(
        @Path("pollId") pollId: String,
        @Body request: VoteRequest
    ): EncuestaDto
}
