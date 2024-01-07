package kr.lul.version

/**
 * [VersionCore] 인스턴스 생성.
 */
fun VersionCore(core: String): VersionCore {
    when {
        core.isEmpty() ->
            throw IllegalArgumentException("core is empty.")

        !core.matches(VersionCore.REGEX) ->
            throw IllegalArgumentException("illegal core pattern : core=$core, pattern=${VersionCore.PATTERN}")
    }
    TODO()
}
