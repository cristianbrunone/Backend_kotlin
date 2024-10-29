package com.example.utils

import com.google.firebase.auth.FirebaseToken

object TokenVerifier {
    fun verify(token: String): FirebaseToken? {
        return FirebaseAuthService.verifyToken(token)
    }
}