package kr.lul.version

import mu.KotlinLogging

actual fun Logger(name: String): Logger = object : Logger {
    private val logger = KotlinLogging.logger(name)

    override fun log(message: String) {
        logger.info(message)
    }

    override fun log(message: String, throwable: Throwable) {
        logger.info(message, throwable)
    }
}
