plugins {
    kotlin("jvm")
    application
}

group = "com.tadeaskriz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    kotlinCompilerPluginClasspath(projects.kotlinPlugin)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}
