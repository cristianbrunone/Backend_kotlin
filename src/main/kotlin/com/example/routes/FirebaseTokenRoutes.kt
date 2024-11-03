package com.example.routes

import com.example.domain.entity.FirebaseToken
import com.example.domain.ports.FirebaseTokenRepository
import com.example.application.request.TokenRequest
import com.example.application.request.toDomain
import com.example.application.services.TokenVerifier
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.firebaseTokenRoutes(repository: FirebaseTokenRepository) {

    
    route("/secure") {
        post("/firebase-token") {
            val idToken = call.request.headers["Authorization"]?.replace("Bearer ", "")
            if (idToken == null) {
                call.respond(HttpStatusCode.BadRequest, "Falta el token de autorización")
                return@post
            }
            
            // Verificar el token de Firebase
            val firebaseToken = TokenVerifier.verify(idToken)
            if (firebaseToken != null) {
                val userId = firebaseToken.uid
                val existingUser = repository.findByUserId(userId) // Verificar existencia

                if (existingUser != null) {
                    // Usuario ya existe; responder sin insertar
                    call.respondText("Usuario ya autenticado con UID: $userId", status = HttpStatusCode.OK)
                } else {
                    // Crear nuevo token y guardar
                    val tokenRequest = TokenRequest(idToken = idToken)
                    val domainToken = tokenRequest.toDomain(userId)
                    val insertedId = repository.insertOne(domainToken)
                    
                    if (insertedId != null) {
                        call.respondText("Usuario autenticado con UID: ${domainToken.userId}, Token guardado con ID: $insertedId")
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, "Error al guardar el token")
                    }
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Token inválido o expirado")
            }
        }


        delete("/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                text = "Missing Firebase token id",
                status = HttpStatusCode.BadRequest
            )
            val deleteCount: Long = repository.deleteById(ObjectId(id))
            if (deleteCount == 1L) {
                return@delete call.respondText("Firebase Token deleted successfully", status = HttpStatusCode.OK)
            }
            return@delete call.respondText("Firebase Token not found", status = HttpStatusCode.NotFound)
        }

        get("/{id?}") {
            val id = call.parameters["id"]
            if (id.isNullOrEmpty()) {
                return@get call.respondText(
                    text = "Missing id",
                    status = HttpStatusCode.BadRequest
                )
            }
            repository.findById(ObjectId(id))?.let {
                call.respond(it)
            } ?: call.respondText("No records found for id $id", status = HttpStatusCode.NotFound)
        }

        patch("/{id?}") {
            val id = call.parameters["id"] ?: return@patch call.respondText(
                text = "Missing Firebase token id",
                status = HttpStatusCode.BadRequest
            )
            val updatedToken = call.receive<FirebaseToken>()
            val updatedCount = repository.updateOne(ObjectId(id), updatedToken)
            call.respondText(
                text = if (updatedCount == 1L) "Firebase Token updated successfully" else "Firebase Token not found",
                status = if (updatedCount == 1L) HttpStatusCode.OK else HttpStatusCode.NotFound
            )
        }
    }
}
