package kr.lul.version

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform