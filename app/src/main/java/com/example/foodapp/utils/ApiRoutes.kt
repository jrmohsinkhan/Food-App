package com.example.foodapp.utils

object ApiRoutes {
//    for emulator
//    const val BASE_URL = "http://10.0.2.2:9090"

//    for real device
    const val BASE_URL = "http://192.168.137.211:9090"


    const val LOGIN = "$BASE_URL/auth/login"
    const val SIGNUP = "$BASE_URL/auth/signup"
    const val LOGOUT = "$BASE_URL/auth/logout"
    const val SAVE_FCM = "$BASE_URL/user/save-token"
    const val BOOK = "$BASE_URL/user/confirmation"
    const val VENUES = "$BASE_URL/user/venues"
}