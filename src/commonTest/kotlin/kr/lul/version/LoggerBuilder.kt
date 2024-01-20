package kr.lul.version

import kotlin.reflect.KClass

expect fun Logger(name: String): Logger

fun Logger(kClass: KClass<*>): Logger = Logger(kClass.simpleName!!)
