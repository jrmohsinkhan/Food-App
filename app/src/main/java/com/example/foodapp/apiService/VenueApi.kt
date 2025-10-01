package com.example.foodapp.apiService

import com.example.foodapp.config.ApiClient
import com.example.foodapp.model.Venue
import com.example.foodapp.utils.ApiRoutes
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.io.IOException

object VenueApi {
    suspend fun getVenues(): Result<List<Venue>> {
        return try {
            val response: List<Venue> = ApiClient.client.get(ApiRoutes.VENUES).body()
            Result.success(response)
        } catch (e: IOException) {
            Result.failure(Exception("Cannot reach the server. Check your internet."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
