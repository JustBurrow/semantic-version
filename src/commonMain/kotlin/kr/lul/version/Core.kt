package kr.lul.version


/**
 * [Semantic Versioning 2.0.0](https://semver.org)의 `<version core>` 구현.
 */
class Core(
    val major: Int,
    val minor: Int,
    val patch: Int
) : Comparable<Core> {
    companion object {
        /**
         * `<version core>` 부분.
         *
         * 완전한 정규표현식으로 표한할 경우 지나치게 복잡해지기 때문에 일부분만 기술한다.
         */
        const val PATTERN = "(0|([1-9][0-9]*))\\.(0|([1-9][0-9]*))\\.(0|([1-9][0-9]*))"

        val REGEX = PATTERN.toRegex()
    }

    init {
        when {
            0 > major ->
                throw IllegalArgumentException("major is negative : major=$major")

            0 > minor ->
                throw IllegalArgumentException("minor is negative : minor=$minor")

            0 > patch ->
                throw IllegalArgumentException("patch is negative : patch=$patch")
        }
    }

    override fun compareTo(other: Core) = when {
        this === other ->
            0

        major != other.major ->
            major - other.major

        minor != other.minor ->
            minor - other.minor

        else ->
            patch - other.patch
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Core

        if (major != other.major) return false
        if (minor != other.minor) return false
        return patch == other.patch
    }

    override fun hashCode(): Int {
        var result = major
        result = 31 * result + minor
        result = 31 * result + patch
        return result
    }

    override fun toString() = "$major.$minor.$patch"
}
