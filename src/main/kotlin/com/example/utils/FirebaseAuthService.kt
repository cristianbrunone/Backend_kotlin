// FirebaseAuthService.kt
package com.example.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken

object FirebaseAuthService {
    fun verifyToken(idToken: String): FirebaseToken? {
        return try {
            FirebaseAuth.getInstance().verifyIdToken(idToken)
        } catch (e: Exception) {
            println("Error verifying token: ${e.message}")
            null
        }
    }
}
