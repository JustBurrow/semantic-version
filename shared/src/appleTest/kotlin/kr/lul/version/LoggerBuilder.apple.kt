package kr.lul.version

actual fun Logger(name: String): Logger = object : Logger {
    override fun log(message: String) {
        println("$name - $message")
    }

    override fun log(message: String, throwable: Throwable) {
        println("$name - $message\n${throwable.stackTraceToString()}")
    }
}
