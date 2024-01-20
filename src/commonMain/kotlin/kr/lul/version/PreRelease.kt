package kr.lul.version

import kotlin.math.min

/**
 * [Semantic Versioning 2.0.0](https://semver.org)의 `<pre-release>` 구현.
 */
class PreRelease(
    private val parts: List<Part>
) : Comparable<PreRelease> {
    /**
     * `<pre-release>`의 `.`으로 구분되는 각 부분.
     */
    class Part(
        val number: Int? = null,
        val text: String? = null
    ) : Comparable<Part> {
        companion object {
            const val NUMBER_PATTERN = "0|([1-9][0-9]*)"
            const val TEXT_PATTERN = "[0-9a-zA-Z]+"

            private val DIGIT_REGEX = "[0-9]+".toRegex()
            val NUMBER_REGEX = NUMBER_PATTERN.toRegex()
            val TEXT_REGEX = TEXT_PATTERN.toRegex()
        }

        init {
            when {
                null == number && null == text ->
                    throw IllegalArgumentException("both number and text are null.")

                null != number && null != text ->
                    throw IllegalArgumentException("both number and text can not set : number=$number, text=$text")

                null != number -> if (0 > number) {
                    throw IllegalArgumentException("number is negative : number=$number")
                }

                null == text -> // 여기는 이미 `null != text`가 보장되지만 이후의 조건에서 `?.`를 쓰지 않도록 추가.
                    throw IllegalArgumentException("both number and text are null.")

                text.isEmpty() ->
                    throw IllegalArgumentException("text is empty.")

                text.matches(DIGIT_REGEX) ->
                    throw IllegalArgumentException("text is number literal : text=$text")

                !text.matches(TEXT_REGEX) ->
                    throw IllegalArgumentException("illegal text format : text=$text, pattern=$TEXT_PATTERN")
            }
        }

        override fun compareTo(other: Part) = when {
            null != number && null != other.number ->
                number - other.number

            null != number && null == other.number ->
                -1

            null == number && null != other.number ->
                1

            else ->
                text!!.compareTo(other.text!!)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Part

            if (number != other.number) return false
            return text == other.text
        }

        override fun hashCode(): Int {
            var result = number ?: 0
            result = 31 * result + (text?.hashCode() ?: 0)
            return result
        }

        override fun toString() = if (null != number) {
            "$number"
        } else {
            text!!
        }
    }

    companion object {
        const val PATTERN = "[0-9a-zA-Z]+(\\.[0-9a-zA-Z]+)*"

        val REGEX = PATTERN.toRegex()
    }

    init {
        if (parts.isEmpty()) {
            throw IllegalArgumentException("parts is empty.")
        }
    }

    override fun compareTo(other: PreRelease): Int {
        for (i in (0..<min(parts.size, other.parts.size))) {
            val compare = parts[i].compareTo(other.parts[i])
            if (0 != compare) {
                return compare
            }
        }

        return parts.size - other.parts.size
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PreRelease

        return parts == other.parts
    }

    override fun hashCode(): Int {
        return parts.hashCode()
    }

    override fun toString(): String = parts.joinToString(".", "", "")
}
