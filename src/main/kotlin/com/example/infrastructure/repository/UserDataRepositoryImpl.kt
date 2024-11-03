package com.example.infrastructure.repository

import com.example.domain.entity.UserData
import com.example.domain.ports.UserDataRepository
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import com.mongodb.client.model.Filters

class UserDataRepositoryImpl(
    private val mongoDatabase: MongoDatabase
) : UserDataRepository {
    companion object {
        const val USER_COLLECTION = "usuarios" // Asegúrate de que el nombre de la colección sea correcto
    }

    // Implementación del método definido en la interfaz
    override suspend fun findByUserId(userId: String): UserData? {
        return mongoDatabase.getCollection<UserData>(USER_COLLECTION)
            .find(Filters.eq("userId", userId)) // Asegúrate de que estés filtrando por el campo correcto
            .firstOrNull()
    }
}
