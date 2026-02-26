package com.ulisesg.encuestasapp.freature.domain.usescases

import com.ulisesg.encuestasapp.freature.domain.entities.Encuesta
import com.ulisesg.encuestasapp.freature.domain.repositories.EncuestaRepository
import javax.inject.Inject

class GetPollUseCase @Inject constructor(
    private val repository: EncuestaRepository
) {
    suspend operator fun invoke(id: String): Encuesta {
        return repository.getPoll(id)
    }
}
