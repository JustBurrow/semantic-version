package kr.lul.version

import android.util.Log

actual fun Logger(name: String): Logger = object : Logger {
    override fun log(message: String) {
        Log.i(name, message)
    }

    override fun log(message: String, throwable: Throwable) {
        Log.i(name, message, throwable)
    }
}
