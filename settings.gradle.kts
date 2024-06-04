pluginManagement {

    plugins {
        kotlin("jvm") version "2.0.0"
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
