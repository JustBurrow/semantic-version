package kr.lul.version

/**
 * [VersionCore] 인스턴스 생성.
 */
fun VersionCore(core: String): VersionCore {
    when {
        core.isEmpty() ->
            throw IllegalArgumentException("core is empty.")

        !core.matches(VersionCore.REGEX) ->
            throw IllegalArgumentException("illegal core format : core=$core, pattern=${VersionCore.PATTERN}")
    }

    val tokens = core.split('.')

    return VersionCore(
        tokens[0].toInt(10),
        tokens[1].toInt(10),
        tokens[2].toInt(10)
    )
}

fun PreRelease(preRelease: String): PreRelease {
    when {
        preRelease.isEmpty() ->
            throw IllegalArgumentException("preRelease is empty.")

        !preRelease.matches(PreRelease.REGEX) ->
            throw IllegalArgumentException("illegal preRelease pattern : preRelease=$preRelease, pattern=${PreRelease.PATTERN}")
    }

    return PreRelease(
        preRelease.split('.')
            .map { Part(it) }
    )
}

fun Part(part: String): PreRelease.Part = if (part.matches(PreRelease.Part.NUMBER_REGEX)) {
    PreRelease.Part(part.toInt(10))
} else {
    PreRelease.Part(text = part)
}
