package kr.lul.version

/**
 * [Semantic Versioning 2.0.0](https://semver.org) 구현.
 */
class Build(
    private val build: String
) {
    companion object {
        const val PATTERN = "[0-9a-zA-Z]+(\\.[0-9a-zA-Z]+)*"
        val REGEX = PATTERN.toRegex()
    }

    init {
        when {
            build.isEmpty() ->
                throw IllegalArgumentException("build is empty.")

            !build.matches(REGEX) ->
                throw IllegalArgumentException("illegal build format : build=$build, pattern=$$PATTERN")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Build

        return build == other.build
    }

    override fun hashCode() = build.hashCode()

    override fun toString() = build
}