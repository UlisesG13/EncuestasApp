package com.ulisesg.encuestasapp.freature.domain.entities

data class Encuesta(
    val id: String,
    val question: String,
    val options: List<String>,
    val votes: List<Int>,
    val total: Int,
    val percentages: List<Int>
)
