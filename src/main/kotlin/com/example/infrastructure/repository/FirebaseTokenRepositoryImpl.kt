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
        try {
            val result = mongoDatabase.getCollection<FirebaseToken>(TOKEN_COLLECTION).insertOne(
                firebaseToken
            )
            return result.insertedId
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
        }
        return null
    }

    override suspend fun deleteById(objectId: ObjectId): Long {
        try {
            val result = mongoDatabase.getCollection<FirebaseToken>(TOKEN_COLLECTION)
                .deleteOne(Filters.eq("_id", objectId))
            return result.deletedCount
        } catch (e: MongoException) {
            System.err.println("Unable to delete due to an error: $e")
        }
        return 0
    }

    override suspend fun findById(objectId: ObjectId): FirebaseToken? =
        mongoDatabase.getCollection<FirebaseToken>(TOKEN_COLLECTION)
            .withDocumentClass<FirebaseToken>()
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    override suspend fun updateOne(objectId: ObjectId, firebaseToken: FirebaseToken): Long {
        try {
            val query = Filters.eq("_id", objectId)
            val updates = Updates.combine(
                Updates.set(FirebaseToken::userId.name, firebaseToken.userId),
                Updates.set(FirebaseToken::token.name, firebaseToken.token),
                Updates.set(FirebaseToken::createdAt.name, firebaseToken.createdAt)
            )
            val options = UpdateOptions().upsert(true)
            val result = mongoDatabase.getCollection<FirebaseToken>(TOKEN_COLLECTION)
                .updateOne(query, updates, options)
            return result.modifiedCount
        } catch (e: MongoException) {
            System.err.println("Unable to update due to an error: $e")
        }
        return 0
    }
}