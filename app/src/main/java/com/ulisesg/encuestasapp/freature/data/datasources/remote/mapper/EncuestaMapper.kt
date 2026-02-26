package com.ulisesg.encuestasapp.freature.data.datasources.remote.mapper

import com.ulisesg.encuestasapp.freature.data.datasources.remote.models.EncuestaDto
import com.ulisesg.encuestasapp.freature.domain.entities.Encuesta

fun EncuestaDto.toEntity(): Encuesta {
    return Encuesta(
        id = this.pollId,
        question = this.question,
        options = this.options,
        votes = this.votes,
        total = this.total,
        percentages = this.percentages
    )
}
