package com.example

import com.example.plugins.configureRouting
import com.example.plugins.FirebaseConfig
import com.example.application.usecases.VerifyUserRegistrationUseCase
import com.example.application.services.FirebaseAuthService
import com.example.infrastructure.repository.FirebaseTokenRepositoryImpl
import com.example.infrastructure.repository.UserDataRepositoryImpl
import com.example.domain.ports.FirebaseTokenRepository
import com.example.domain.ports.UserDataRepository
import io.github.cdimascio.dotenv.dotenv
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import io.ktor.serialization.gson.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.websocket.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        gson {}
    }

    // Cargar variables de entorno
    val dotenv = dotenv { 
        directory = System.getProperty("user.dir") 
        ignoreIfMissing = true 
    }

    val mongoUri = dotenv["MONGO_URI"] ?: "mongodb://localhost:27017"
    val mongoDatabase = dotenv["MONGO_DATABASE"] ?: "encontros"
    
    println("Mongo URI: $mongoUri")
    println("Mongo Database: $mongoDatabase")

    // Inicializar Firebase
    FirebaseConfig.initialize()

    // Configurar CORS
    install(CORS) {
        allowHost("192.168.100.53:8080") // Permitir tu IP local
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
    }

    // Instalar WebSockets
    install(WebSockets)

    // Configurar autenticación JWT
    install(Authentication) {
        jwt("auth-jwt") {
            skipWhen { true } // Salta la verificación de JWT de Ktor

            validate { credential ->
                val idToken = credential.payload.getClaim("sub").asString() // 'sub' es el UID en el token de Firebase
                val firebaseToken = FirebaseAuthService.verifyToken(idToken)
                if (firebaseToken != null) {
                    UserIdPrincipal(firebaseToken.uid) // Retorna el principal del usuario
                } else {
                    null // Token inválido
                }
            }
        }
    }

    // Crear el cliente de MongoDB y la base de datos usando la versión Kotlin asincrónica
    val mongoClient: MongoClient = MongoClient.create(mongoUri)
    val mongoDb: MongoDatabase = mongoClient.getDatabase(mongoDatabase)

    // Crear el repositorio de tokens de Firebase
    val firebaseTokenRepository: FirebaseTokenRepository = FirebaseTokenRepositoryImpl(mongoDb)

     // Crear el repositorio de datos de usuario
    val userDataRepository: UserDataRepository = UserDataRepositoryImpl(mongoDb) // Asegúrate de tener esta implementación

     // Crear el caso de uso
    val verifyUserRegistrationUseCase = VerifyUserRegistrationUseCase(userDataRepository)

    // Configurar las rutas, pasando el repositorio
    configureRouting(firebaseTokenRepository, verifyUserRegistrationUseCase)
}
