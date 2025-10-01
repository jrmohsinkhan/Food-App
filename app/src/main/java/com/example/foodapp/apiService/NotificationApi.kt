package com.example.foodapp.apiService

import com.example.foodapp.config.ApiClient
import com.example.foodapp.model.ConfirmationResult
import com.example.foodapp.model.ErrorResponse
import com.example.foodapp.model.FcmTokenRequest
import com.example.foodapp.model.FcmTokenResponse
import com.example.foodapp.utils.ApiRoutes
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.IOException
import kotlinx.serialization.json.Json

object NotificationApi {

    suspend fun sendFcmToken(fcmToken: String): FcmTokenResponse {
        return ApiClient.client.post(ApiRoutes.SAVE_FCM) {
            contentType(ContentType.Application.Json)
            setBody(FcmTokenRequest(fcmToken))
        }.body()
    }

    suspend fun confirmBooking(): ConfirmationResult {
        return try {
            // Perform the authenticated GET request
            val response = ApiClient.client.get(ApiRoutes.BOOK)

            when (response.status) {
                HttpStatusCode.OK -> {
                    // SUCCESS (HTTP 200): Body is the string "Booking confirmed & notifications sent."
                    val message = response.bodyAsText()
                    ConfirmationResult.Success(message)
                }

                HttpStatusCode.BadRequest -> {
                    // API Error (HTTP 400): Body is the string "No FCM tokens registered for this user."
                    // or a standard ErrorResponse if your server is consistent.

                    val errorBody = response.bodyAsText()

                    // Try to parse as ErrorResponse, fallback to raw string
                    val errorMessage = try {
                        Json.decodeFromString<ErrorResponse>(errorBody).message
                    } catch (_: Exception) {
                        // If it's not JSON, use the raw string from the server (e.g., "No FCM tokens...")
                        errorBody
                    }
                    ConfirmationResult.Error(errorMessage)
                }

                else -> {
                    // Other 4xx or 5xx codes
                    ConfirmationResult.NetworkError("Server responded with status: ${response.status.value}")
                }
            }

        } catch (e: ClientRequestException) {
            // General 4xx handling (if status is not explicitly handled above)
            ConfirmationResult.Error(e.response.status.description)

        } catch (e: ServerResponseException) {
            // 5xx errors
            ConfirmationResult.NetworkError("Server is currently unavailable.")

        } catch (e: IOException) {
            // Network failure (no internet)
            ConfirmationResult.NetworkError("Cannot connect to the server. Check your connection.")

        } catch (e: Exception) {
            // Catch all other exceptions (serialization, etc.)
            ConfirmationResult.Error("An unexpected error occurred: ${e.message ?: "Unknown"}")
        }
    }

}