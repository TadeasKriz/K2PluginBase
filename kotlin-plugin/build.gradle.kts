plugins {
    kotlin("jvm")
}

dependencies {
    compileOnly(kotlin("compiler-embeddable"))

    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.5.0")
}
