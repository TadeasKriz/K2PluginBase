var isLoggingEnabled = false

fun logVerbose(message: Any): String {
    if (isLoggingEnabled) {
        println("Verbose: $message")
    }
    return message.toString()
}


inline fun log(@AutoClosure message: () -> String) {
    if (isLoggingEnabled) {
        println("Before")
        println(message())
    }
}
