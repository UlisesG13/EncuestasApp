package com.ulisesg.encuestasapp.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @Singleton
    fun provideWebSocket(okHttpClient: OkHttpClient): WebSocket {
        val request = okhttp3.Request.Builder()
            .url(NetworkModule.WEBSOCKET_URL)
            .build()

        return okHttpClient.newWebSocket(
            request,
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                    println("conectado")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    println("Mensaje: $text")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                    println("Error WebSocket: ${t.message}")
                }
            }
        )
    }
}