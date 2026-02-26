package com.ulisesg.encuestasapp.freature.domain.usescases

import com.ulisesg.encuestasapp.freature.domain.entities.Encuesta
import com.ulisesg.encuestasapp.freature.domain.repositories.EncuestaRepository
import javax.inject.Inject

class VotePollUseCase @Inject constructor(
    private val repository: EncuestaRepository
) {
    suspend operator fun invoke(pollId: String, optionIndex: Int): Encuesta {
        return repository.votePoll(pollId, optionIndex)
    }
}
