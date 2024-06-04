fun main() {
    val message = println("This won't be logged")
    log("This also isn't logged - ${println("Neither is this?")}")

    isLoggingEnabled = true

    println("Now we're logging (previous message: $message)")
    log("This is also now logged - ${println("And this will print just before we log it")}")

    val factory = ExampleClass.Factory(firstPart = "Hello")
    println(factory)

    val example = factory.create(secondPart = "World")
    println(example)

    println(example.mutableStateFlow.value)

    example.mutableStateFlow = "Hello Droidcon!"
    println(example.mutableStateFlow.value)
}
