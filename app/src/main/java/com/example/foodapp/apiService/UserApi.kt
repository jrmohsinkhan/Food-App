package com.example.foodapp.apiService

import com.example.foodapp.config.ApiClient
import com.example.foodapp.model.ErrorResponse
import com.example.foodapp.model.LoginRequest
import com.example.foodapp.model.LoginResponse
import com.example.foodapp.model.LoginResult
import com.example.foodapp.model.LogoutResponse
import com.example.foodapp.model.SignupRequest
import com.example.foodapp.model.SignupResponse
import com.example.foodapp.utils.ApiRoutes
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.IOException
import kotlinx.serialization.json.Json

object UserApi {
    suspend fun login(email: String, password: String): LoginResult {

        // Use a mutable variable for the raw response
        lateinit var response: HttpResponse

        try {
            // 1. Perform the API request
            response = ApiClient.client.post(ApiRoutes.LOGIN) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }

            // 2. Check the status code
            if (response.status == HttpStatusCode.OK) {
                // SUCCESS (HTTP 200)
                val success = response.body<LoginResponse>()
                return LoginResult.Success(success)
            } else if (response.status == HttpStatusCode.Unauthorized || response.status == HttpStatusCode.BadRequest) {
                // API ERROR (e.g., HTTP 401 or 400)

                // Read the body as text FIRST, before any deserialization attempts
                val errorBody = response.bodyAsText()

                // Attempt to deserialize the error JSON
                val errorMessage = try {
                    val errorResponse = Json.decodeFromString<ErrorResponse>(errorBody)
                    errorResponse.message
                } catch (e: Exception) {
                    // Fallback if the error JSON is malformed or not standard
                    "Login failed with an unexpected response from the server."
                }

                return LoginResult.Error(errorMessage)
            } else {
                // Other 4xx or 5xx unhandled by the API's standard error format
                return LoginResult.NetworkError("Server error: HTTP ${response.status.value}")
            }

        } catch (e: IOException) {
            // General network failure (no internet, timeout)
            return LoginResult.NetworkError("Cannot reach the server. Check your connection.")

        } catch (e: ClientRequestException) {
            // This catches the standard Ktor failure for 4xx status codes.
            // Since we handled 401/400 above, this may catch other 4xx errors (403, 404, etc.).

            // We read the body from the exception's response object
            val errorBody = try {
                e.response.bodyAsText()
            } catch (ioe: Exception) {
                return LoginResult.NetworkError("Server returned error ${e.response.status.value}, body unreadable.")
            }

            // Attempt to parse the body in case it's a standard ErrorResponse
            val errorMessage = try {
                val errorResponse = Json.decodeFromString<ErrorResponse>(errorBody)
                errorResponse.message
            } catch (jsonE: Exception) {
                // Fallback, using the HTTP status description as a last resort
                "API Error: ${e.response.status.description}"
            }

            return LoginResult.Error(errorMessage)

        } catch (e: Exception) {
            // Catch all remaining exceptions, including serialization errors that might not be wrapped
            // This is the last safety net.
            return LoginResult.NetworkError("An unknown error occurred: ${e.message ?: "Unknown"}")
        }
    }

    suspend fun signup(name: String, email: String, password: String, phone: String, userType: Int) : SignupResponse {
        return ApiClient.client.post(ApiRoutes.SIGNUP){
            contentType(ContentType.Application.Json)
            setBody(SignupRequest(name, email, password, phone, userType))
        }.body()
    }

    suspend fun logout() : LogoutResponse {
        return ApiClient.client.post(ApiRoutes.LOGOUT){
            contentType(ContentType.Application.Json)
        }.body()
    }
}