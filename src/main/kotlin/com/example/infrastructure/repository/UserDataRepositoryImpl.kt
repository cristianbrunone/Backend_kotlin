package com.example.infrastructure.repository

import com.example.domain.entity.UserData
import com.example.domain.ports.UserDataRepository
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import com.mongodb.client.model.Filters
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.model.Updates
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import org.bson.types.ObjectId

class UserDataRepositoryImpl(
    private val mongoDatabase: MongoDatabase
) : UserDataRepository {
    companion object {
        const val USER_COLLECTION = "usuarios" // Asegúrate de que el nombre de la colección sea correcto
    }

    override suspend fun findByUserId(userId: String): UserData? {
        return mongoDatabase.getCollection<UserData>(USER_COLLECTION)
            .find(Filters.eq("_id", userId)) // Asegúrate de que estés filtrando por el campo correcto
            .firstOrNull()
    }

    override suspend fun insertOne(user: UserData): String? {
        val result: InsertOneResult = mongoDatabase.getCollection<UserData>(USER_COLLECTION)
            .insertOne(user)
        return user.userId // Devuelve el userId como String
    }

    override suspend fun findById(id: String): UserData? {
        return mongoDatabase.getCollection<UserData>(USER_COLLECTION)
            .find(Filters.eq("_id", id))
            .firstOrNull()
    }

    override suspend fun deleteById(id: String): Long {
        val result: DeleteResult = mongoDatabase.getCollection<UserData>(USER_COLLECTION)
            .deleteOne(Filters.eq("_id", id))
        return result.deletedCount
    }

    override suspend fun updateOne(id: String, user: UserData): Long {
        val result: UpdateResult = mongoDatabase.getCollection<UserData>(USER_COLLECTION)
            .updateOne(Filters.eq("_id", id), Updates.combine(
                Updates.set("nome", user.nome),
                Updates.set("cpf", user.cpf),

            ))
        return result.modifiedCount
    }
}
