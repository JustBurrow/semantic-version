package kr.lul.version

interface Logger {
    fun log(message: String)

    fun log(message: String, throwable: Throwable)
}
