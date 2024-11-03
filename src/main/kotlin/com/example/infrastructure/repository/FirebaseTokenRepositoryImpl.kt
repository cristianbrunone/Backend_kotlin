package com.example.infrastructure.repository

import com.example.domain.entity.FirebaseToken
import com.example.domain.ports.FirebaseTokenRepository
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.BsonValue
import org.bson.types.ObjectId

class FirebaseTokenRepositoryImpl(
    private val mongoDatabase: MongoDatabase
) : FirebaseTokenRepository {
    companion object {
        const val TOKEN_COLLECTION = "usuarios"
    }

    override suspend fun insertOne(firebaseToken: FirebaseToken): BsonValue? {
        return try {
            val existingUser = findByUserId(firebaseToken.userId)
            if (existingUser != null) {
                println("User with userId ${firebaseToken.userId} already exists. Authentication successful.")
                null
            } else {
                val result = mongoDatabase.getCollection<FirebaseToken>(TOKEN_COLLECTION).insertOne(firebaseToken)
                result.insertedId
            }
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
            null
        }
    }

    override suspend fun deleteById(id: String): Long {
        return try {
            val objectId = ObjectId(id)  // Convertir String a ObjectId
            val result = mongoDatabase.getCollection<FirebaseToken>(TOKEN_COLLECTION)
                .deleteOne(Filters.eq("_id", objectId))
            result.deletedCount
        } catch (e: MongoException) {
            System.err.println("Unable to delete due to an error: $e")
            0
        } catch (e: IllegalArgumentException) {
            System.err.println("Invalid ID format: $e")
            0
        }
    }

    override suspend fun findById(id: String): FirebaseToken? {
        return try {
            val objectId = ObjectId(id)  // Convertir String a ObjectId
            mongoDatabase.getCollection<FirebaseToken>(TOKEN_COLLECTION)
                .find(Filters.eq("_id", objectId))
                .firstOrNull()
        } catch (e: IllegalArgumentException) {
            System.err.println("Invalid ID format: $e")
            null
        }
    }

    override suspend fun updateOne(id: String, firebaseToken: FirebaseToken): Long {
        return try {
            val objectId = ObjectId(id)  // Convertir String a ObjectId
            val query = Filters.eq("_id", objectId)
            val updates = Updates.combine(
                Updates.set(FirebaseToken::userId.name, firebaseToken.userId),
                Updates.set(FirebaseToken::token.name, firebaseToken.token),
                Updates.set(FirebaseToken::createdAt.name, firebaseToken.createdAt)
            )
            val options = UpdateOptions().upsert(true)
            val result = mongoDatabase.getCollection<FirebaseToken>(TOKEN_COLLECTION)
                .updateOne(query, updates, options)
            result.modifiedCount
        } catch (e: IllegalArgumentException) {
            System.err.println("Invalid ID format: $e")
            0
        } catch (e: MongoException) {
            System.err.println("Unable to update due to an error: $e")
            0
        }
    }

    // Implementación de findByUserId
    override suspend fun findByUserId(userId: String): FirebaseToken? =
        mongoDatabase.getCollection<FirebaseToken>(TOKEN_COLLECTION)
            .find(Filters.eq("userId", userId))
            .firstOrNull()
}
