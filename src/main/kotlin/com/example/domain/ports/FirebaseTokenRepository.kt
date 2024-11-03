package com.example.domain.ports

import com.example.domain.entity.FirebaseToken
import org.bson.BsonValue

interface FirebaseTokenRepository {
    /**
     * Método para insertar un nuevo token en la base de datos.
     * Recibe un objeto FirebaseToken y devuelve un valor BSON 
     * (puede ser el ID del nuevo documento o algún otro valor relevante).
     */
    suspend fun insertOne(firebaseToken: FirebaseToken): BsonValue?

    /**
     * Método para eliminar un token de la base de datos usando su ID (String).
     * Devuelve un Long, que podría representar el número de documentos eliminados.
     */
    suspend fun deleteById(id: String): Long
    
    /**
     * Método para buscar un token en la base de datos utilizando su ID (String).
     * Devuelve un objeto FirebaseToken si se encuentra, o null si no.
     */
    suspend fun findById(id: String): FirebaseToken?

    /**
     * Método para actualizar un token existente en la base de datos.
     * Recibe el ID del token (String) y el nuevo objeto FirebaseToken que contiene los datos actualizados.
     * Devuelve un Long, que podría representar el número de documentos actualizados.
     */
    suspend fun updateOne(id: String, firebaseToken: FirebaseToken): Long

    // Nuevo método para buscar por userId
    suspend fun findByUserId(userId: String): FirebaseToken?
}
