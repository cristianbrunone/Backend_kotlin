package com.example.application.request

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationRequest(
    val userId: String,              
    val nome: String,
    val cpf: String,
    val fotos: List<String>,
    val dataNascimento: String,
    val sexo: String,
    val identificacaoLGBT: String?,
    val crenca: String,
    val signos: String,
    val decanatos: String,
    val formacaoAcademica: String,
    val profissao: String,
    val altura: String,
    val peso: String,
    val racaEtnia: String,
    val corpo: String,
    val filhos: String,
    val hobbyEsporte: String
)
