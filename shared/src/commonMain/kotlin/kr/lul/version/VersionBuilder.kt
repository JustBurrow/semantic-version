package kr.lul.version

/**
 * [Core] 인스턴스 생성.
 */
fun Core(core: String): Core {
    when {
        core.isEmpty() ->
            throw IllegalArgumentException("core is empty.")

        !core.matches(Core.REGEX) ->
            throw IllegalArgumentException("illegal core format : core=$core, pattern=${Core.PATTERN}")
    }

    val tokens = core.split('.')

    return Core(
        tokens[0].toInt(10),
        tokens[1].toInt(10),
        tokens[2].toInt(10)
    )
}

fun Part(part: String): PreRelease.Part = if (part.matches(PreRelease.Part.NUMBER_REGEX)) {
    PreRelease.Part(part.toInt(10))
} else {
    PreRelease.Part(text = part)
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

fun Version(version: String): Version {
    when {
        version.isEmpty() ->
            throw IllegalArgumentException("version is empty.")

        !version.matches(Version.REGEX) ->
            throw IllegalArgumentException("illegal version pattern : version=$version, pattern=${Version.PATTERN}")
    }

    val core = when {
        version.contains('-') ->
            Core(version.substringBefore('-'))

        version.contains('+') ->
            Core(version.substringBefore('+'))

        else ->
            Core(version)
    }
    val preRelease = try {
        when {
            !version.contains('-') ->
                null

            !version.contains('+') ->
                PreRelease(version.substringAfter('-'))

            else ->
                PreRelease(version.substring(version.indexOf('-') + 1, version.indexOf('+')))
        }
    } catch (e: IllegalArgumentException) {
        throw IllegalArgumentException(
            "illegal version pattern : version=$version, pattern=${Version.PATTERN}",
            e
        )
    }
    val build = try {
        if (version.contains('+')) {
            Build(version.substringAfter('+'))
        } else {
            null
        }
    } catch (e: IllegalArgumentException) {
        throw IllegalArgumentException(
            "illegal version pattern : version=$version, pattern=${Version.PATTERN}",
            e
        )
    }

    return Version(core, preRelease, build)
}
