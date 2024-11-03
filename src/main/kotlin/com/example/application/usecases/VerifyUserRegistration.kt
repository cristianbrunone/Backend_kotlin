package com.example.application.usecases

import com.example.domain.ports.UserDataRepository
import io.ktor.http.HttpStatusCode

class VerifyUserRegistrationUseCase(
    private val userDataRepository: UserDataRepository // Repositorio para acceder a la información del usuario
) {
    suspend fun execute(userId: String): VerificationResponse {
        val userDetails = userDataRepository.findByUserId(userId)

        return if (userDetails != null && userDetails.isComplete()) {
            VerificationResponse(success = true, statusCode = HttpStatusCode.OK)
        } else {
            VerificationResponse(success = false, statusCode = HttpStatusCode.Found, redirectUrl = "/complete-registration")
        }
    }
}

data class VerificationResponse(
    val success: Boolean,
    val statusCode: HttpStatusCode,
    val redirectUrl: String? = null
)
