pluginManagement {

    plugins {
        kotlin("jvm") version "1.9.0-RC"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "K2PluginBase"

include("kotlin-plugin")
include("example")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
