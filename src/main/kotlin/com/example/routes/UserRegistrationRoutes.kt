package com.example.routes

import com.example.domain.entity.UserData
import com.example.domain.ports.UserDataRepository
import com.example.application.request.UserRegistrationRequest

import com.example.application.response.UserRegistrationResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import com.example.domain.ports.FirebaseTokenRepository
import io.ktor.server.response.respondText
import com.example.application.request.toDomain
import com.example.application.request.TokenRequest
import com.example.application.services.TokenVerifier
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.userRegistrationRoutes(tokenRepository: FirebaseTokenRepository ,userRepository: UserDataRepository) {
    route("/user") {
        // Ruta para el registro de un nuevo usuario
       post("/register") {
    // Verificar el token en el encabezado Authorization
    val idToken = call.request.headers["Authorization"]?.replace("Bearer ", "")
    if (idToken == null) {
        call.respond(HttpStatusCode.BadRequest, "Falta el token de autorización")
        return@post
    }

    // Verificar el token de Firebase
    val firebaseToken = TokenVerifier.verify(idToken)
    if (firebaseToken == null) {
        call.respond(HttpStatusCode.Unauthorized, "Token inválido o expirado")
        return@post
    }

    // Continuar con la lógica de registro del usuario
    val userRequest = call.receive<UserRegistrationRequest>()
    val userIdFromToken = firebaseToken.uid

    // Comprobar si el usuario ya existe
    val existingUser = userRepository.findByUserId(userIdFromToken)
    if (existingUser != null) {
        call.respond(HttpStatusCode.Conflict, "Usuario ya existe")
        return@post
    }

    // Crear un nuevo objeto de usuario
    val newUser = UserData(
        userId = userIdFromToken,
        nome = userRequest.nome,
        cpf = userRequest.cpf,
        fotos = userRequest.fotos,
        dataNascimento = userRequest.dataNascimento,
        sexo = userRequest.sexo,
        identificacaoLGBT = userRequest.identificacaoLGBT,
        crenca = userRequest.crenca,
        signos = userRequest.signos,
        decanatos = userRequest.decanatos,
        formacaoAcademica = userRequest.formacaoAcademica,
        profissao = userRequest.profissao,
        altura = userRequest.altura,
        peso = userRequest.peso,
        racaEtnia = userRequest.racaEtnia,
        corpo = userRequest.corpo,
        filhos = userRequest.filhos,
        hobbyEsporte = userRequest.hobbyEsporte
    )

    // Insertar el nuevo usuario
    val insertedId = userRepository.insertOne(newUser)
    if (insertedId != null) {
        // Guardar el token en la base de datos después del registro
        val tokenRequest = TokenRequest(idToken = idToken)
        val domainToken = tokenRequest.toDomain(userIdFromToken)
        val tokenInsertedId = tokenRepository.insertOne(domainToken)

        // Responder con el estado de registro
        call.respond(
            HttpStatusCode.Created,
            UserRegistrationResponse(
                success = true,
                userId = insertedId,
                message = "Usuario registrado exitosamente, token guardado con ID: $tokenInsertedId"
            )
        )
    } else {
        call.respond(HttpStatusCode.InternalServerError, "Error al registrar el usuario")
    }
}

        // Ruta para obtener un usuario por ID
        get("/{id?}") {
            val id = call.parameters["id"]
            if (id.isNullOrEmpty()) {
                call.respond(HttpStatusCode.BadRequest, "ID de usuario faltante")
                return@get
            }

            // Aquí se debe usar el userId como String
            val user = userRepository.findById(id)
            if (user != null) {
                call.respond(user)
            } else {
                call.respond(HttpStatusCode.NotFound, "Usuario no encontrado")
            }
        }

        // Ruta para eliminar un usuario por ID
        delete("/{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                text = "ID de usuario faltante",
                status = HttpStatusCode.BadRequest
            )

            // Eliminamos el usuario usando el userId como String
            val deleteCount: Long = userRepository.deleteById(id)
            if (deleteCount == 1L) {
                call.respondText("Usuario eliminado exitosamente", status = HttpStatusCode.OK)
            } else {
                call.respondText("Usuario no encontrado", status = HttpStatusCode.NotFound)
            }
        }

        // Ruta para actualizar un usuario por ID
        patch("/{id?}") {
            val id = call.parameters["id"] ?: return@patch call.respondText(
                text = "ID de usuario faltante",
                status = HttpStatusCode.BadRequest
            )
            try {
                // No se necesita convertir a ObjectId si estamos usando userId como String
                val updatedUser = call.receive<UserData>()
                val updatedCount = userRepository.updateOne(id, updatedUser)
                call.respondText(
                    text = if (updatedCount == 1L) "Usuario actualizado exitosamente" else "Usuario no encontrado",
                    status = if (updatedCount == 1L) HttpStatusCode.OK else HttpStatusCode.NotFound
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Error al actualizar el usuario")
            }
        }
    }
}
