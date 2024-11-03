package com.example.domain.ports

import com.example.domain.entity.UserData

interface UserDataRepository {
    suspend fun findByUserId(userId: String): UserData?
    suspend fun insertOne(user: UserData): String? // Cambia a String
    suspend fun findById(userId: String): UserData?
    suspend fun deleteById(userId: String): Long 
    suspend fun updateOne(userId: String, user: UserData): Long 
}
