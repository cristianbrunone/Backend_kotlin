package com.example.application.services

import com.google.firebase.auth.FirebaseToken

object TokenVerifier {
    fun verify(token: String): FirebaseToken? {
        return FirebaseAuthService.verifyToken(token)
    }
}