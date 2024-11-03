// Routing.kt
package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.example.routes.firebaseTokenRoutes
import com.example.routes.userRegistrationRoutes
import com.example.domain.ports.UserDataRepository
import com.example.domain.ports.FirebaseTokenRepository 
import com.example.application.usecases.VerifyUserRegistrationUseCase 


fun Application.configureRouting(firebaseTokenRepository: FirebaseTokenRepository, verifyUserRegistrationUseCase: VerifyUserRegistrationUseCase, userDataRepository: UserDataRepository) {
    routing {
        userRegistrationRoutes(firebaseTokenRepository, userDataRepository)
        firebaseTokenRoutes(firebaseTokenRepository, verifyUserRegistrationUseCase)
  
    }
}
