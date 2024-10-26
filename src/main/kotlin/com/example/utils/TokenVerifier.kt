package com.example.utils

import com.auth0.jwt.interfaces.JWTVerifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

object TokenVerifier {
    private const val SECRET = "yrina-secret"

    // Define el verificador JWT para tokens adicionales si es necesario
    val verifier: JWTVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build()

    // Verifica el token exclusivamente con Firebase
    fun verifyFirebaseToken(token: String): FirebaseToken? {
        return try {
            FirebaseAuth.getInstance().verifyIdToken(token)
        } catch (e: Exception) {
            null
        }
    }

    // Verificación adicional con el verificador JWT si necesitas verificar otros tipos de tokens
    fun verifyCustomToken(token: String): Boolean {
        return try {
            verifier.verify(token)
            true
        } catch (e: Exception) {
            false
        }
    }
}
