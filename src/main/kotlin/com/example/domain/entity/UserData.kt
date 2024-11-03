package com.example.domain.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UserData(
    @BsonId
    val userId: String = ObjectId.get().toString(), // Generar un nuevo ObjectId automáticamente como String
    val nome: String,                  // Nombre completo
    val cpf: String,                   // Número de CPF
    val fotos: List<String> = listOf(), // Lista de hasta 5 fotos
    val dataNascimento: String,        // Fecha de nacimiento en formato "YYYY-MM-DD"
    val sexo: String,                  // "Binário", "Não binário" o "LGBT+"
    val identificacaoLGBT: String? = null, // Campo opcional si el sexo es "LGBT+"
    val crenca: String,                // Crença o religión
    val signos: String,                // Signo zodiacal
    val decanatos: String,             // Decanato
    val formacaoAcademica: String,     // Nivel de escolaridad
    val profissao: String,             // Profesión
    val altura: String,                // Altura
    val peso: String,                  // Peso
    val racaEtnia: String,             // Raza/etnia
    val corpo: String,                 // Descripción del cuerpo
    val filhos: String,                // Información sobre hijos
    val hobbyEsporte: String       
) {
    fun isComplete(): Boolean {
        val requiredFields = listOf(
            nome, cpf, dataNascimento, sexo, crenca, signos, decanatos,
            formacaoAcademica, profissao, altura, peso, racaEtnia, corpo, filhos, hobbyEsporte
        )

        return requiredFields.all { it.isNotBlank() } &&
               fotos.size == 5 &&
               (sexo != "LGBT+" || (identificacaoLGBT?.isNotBlank() == true))
    }
}
