package com.example.credieficaz.models

data class AuthResponse(
    val authorized: Boolean,
    val message: String? = null
)