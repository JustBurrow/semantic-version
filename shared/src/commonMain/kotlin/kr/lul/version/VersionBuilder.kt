package kr.lul.version

fun VersionCore(versionCore: String): VersionCore {
    when {
        versionCore.isEmpty() ->
            throw IllegalArgumentException("versionCore is empty.")
    }
    TODO()
}
