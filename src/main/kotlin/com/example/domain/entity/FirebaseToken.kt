package com.example.domain.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class FirebaseToken(
   @BsonId
    val id: ObjectId = ObjectId.get(), // Generar un nuevo ObjectId automáticamente
    val userId: String,                // ID del usuario al que pertenece el token
    val token: String,                  // El token de Firebase
    val createdAt: Long = System.currentTimeMillis() // Timestamp de creación
)