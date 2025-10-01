package com.example.foodapp.config

import android.content.Context
import com.example.foodapp.utils.SecureStorage
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    private var appContext: Context? = null

    // Exposed client
    val client: HttpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    prettyPrint = true
                })
            }

            install(DefaultRequest) {
                appContext?.let { ctx ->
                    val loginResponse = SecureStorage.readData(ctx)
                    headers.remove(HttpHeaders.Authorization) // clear old header
                    if (loginResponse?.token != null) {
                        headers.append(
                            HttpHeaders.Authorization,
                            "Bearer ${loginResponse.token}"
                        )
                    }
                }
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        android.util.Log.d("KtorClient", message)
                    }
                }
            }
        }
    }

    // Call this in MyApp.onCreate()
    fun init(context: Context) {
        appContext = context.applicationContext
    }
}
