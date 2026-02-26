package com.ulisesg.encuestasapp.freature.data.di

import com.ulisesg.encuestasapp.freature.data.datasources.remote.api.EncuestasApi
import com.ulisesg.encuestasapp.freature.data.repositories.EncuestaRepositoryImpl
import com.ulisesg.encuestasapp.freature.domain.repositories.EncuestaRepository
import com.ulisesg.encuestasapp.freature.domain.usescases.GetEncuestasUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideEncuestaRepository(
        encuestasApi: EncuestasApi
    ): EncuestaRepository {
        return EncuestaRepositoryImpl(encuestasApi)
    }

    @Singleton
    @Provides
    fun provideGetEncuestasUseCase(
        repository: EncuestaRepository
    ): GetEncuestasUseCase {
        return GetEncuestasUseCase(repository)
    }
}