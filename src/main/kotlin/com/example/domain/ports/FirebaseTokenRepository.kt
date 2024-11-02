package com.example.domain.ports

import com.example.domain.entity.FirebaseToken
import org.bson.BsonValue
import org.bson.types.ObjectId

interface FirebaseTokenRepository {
    /*insertOne: Método para insertar un nuevo token en la base de datos. 
    Recibe un objeto FirebaseToken y devuelve un valor BSON 
    (puede ser el ID del nuevo documento o algún otro valor relevante). */
    suspend fun insertOne(firebaseToken: FirebaseToken): BsonValue? 
   
    /*deleteById: Método para eliminar un token de la base de datos usando su ID (ObjectId). 
    Devuelve un Long, que podría representar el número de documentos eliminados. */
    suspend fun deleteById(objectId: ObjectId): Long
   
    /*findById: Método para buscar un token en la base de datos utilizando su ID. 
    Devuelve un objeto FirebaseToken si se encuentra, o null si no. */
    suspend fun findById(objectId: ObjectId): FirebaseToken?
   
    /*updateOne: Método para actualizar un token existente en la base de datos. Recibe el ID del token 
    y el nuevo objeto FirebaseToken que contiene los datos actualizados. Devuelve un Long,
     que podría representar el número de documentos actualizados. */
    suspend fun updateOne(objectId: ObjectId, firebaseToken: FirebaseToken): Long

}