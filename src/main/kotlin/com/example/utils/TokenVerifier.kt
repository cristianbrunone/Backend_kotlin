// TokenVerifier.kt
package com.example.utils

import com.google.firebase.auth.FirebaseToken

object TokenVerifier {
    fun verifyFirebaseToken(token: String): FirebaseToken? {
        return FirebaseAuthService.verifyToken(token)
    }
}
