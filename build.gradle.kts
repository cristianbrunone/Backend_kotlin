val ktor_version = "3.0.0"
val firebase_bom_version = "33.5.1"

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    implementation("io.ktor:ktor-server-websockets:$ktor_version")

    implementation("io.ktor:ktor-server-auth:$ktor_version")


    implementation("com.google.firebase:firebase-admin:9.2.0")

       // Firestore
    implementation("com.google.firebase:firebase-firestore:25.1.1")

       // Storage
    implementation("com.google.firebase:firebase-storage:21.0.1")

    //verficador de token
    implementation("com.auth0:java-jwt:4.4.0") 


    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}

// Configuración de la compilación de Java
tasks.withType<JavaCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

// Configuración de Kotlin
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}
