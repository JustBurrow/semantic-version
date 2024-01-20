package kr.lul.version

/**
 * [Semantic Versioning 2.0.0](https://semver.org) 구현.
 */
class Version(
    val core: Core,
    val preRelease: PreRelease? = null,
    val build: Build? = null
) : Comparable<Version> {
    companion object {
        const val PATTERN = "${Core.PATTERN}(-${PreRelease.PATTERN})?(\\+${Build.PATTERN})?"
        val REGEX = PATTERN.toRegex()
    }

    val major = core.major
    val minor = core.minor
    val patch = core.patch

    override fun compareTo(other: Version): Int = when {
        core != other.core ||
                (null == preRelease && null == other.preRelease) ->
            core.compareTo(other.core)

        null == preRelease ->
            1

        null == other.preRelease ->
            -1

        else ->
            preRelease.compareTo(other.preRelease)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Version

        if (core != other.core) return false
        if (preRelease != other.preRelease) return false
        return build == other.build
    }

    override fun hashCode(): Int {
        var result = core.hashCode()
        result = 31 * result + (preRelease?.hashCode() ?: 0)
        result = 31 * result + (build?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder(core.toString())
        if (null != preRelease) {
            sb.append('-').append(preRelease)
        }
        if (null != build) {
            sb.append('+').append(build)
        }
        return sb.toString()
    }
}
