package com.example.domain.ports

import com.example.domain.entity.UserData
import org.bson.types.ObjectId

interface UserDataRepository {
    suspend fun findByUserId(userId: String): UserData?
}