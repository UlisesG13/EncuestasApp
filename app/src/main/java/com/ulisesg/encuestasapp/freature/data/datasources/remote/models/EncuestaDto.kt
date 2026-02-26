package com.ulisesg.encuestasapp.freature.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class EncuestaDto(
    @SerializedName("pollId")
    val pollId: String,
    @SerializedName("question")
    val question: String,
    @SerializedName("options")
    val options: List<String>,
    @SerializedName("votes")
    val votes: List<Int>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("percentages")
    val percentages: List<Int>
)

data class CreatePollRequest(
    val question: String,
    val options: List<String>
)

data class VoteRequest(
    val optionIndex: Int
)
