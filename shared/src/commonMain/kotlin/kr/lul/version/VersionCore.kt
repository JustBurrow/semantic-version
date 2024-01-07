package kr.lul.version


/**
 * [Semantic Versioning 2.0.0](https://semver.org) 구현.
 */
class VersionCore {
    companion object {
        /**
         * `<version core>` 부분.
         *
         *
         * 완전한 정규표현식으로 표한할 경우 지나치게 복잡해지기 때문에 일부분만 기술한다.
         */
        const val PATTERN = "(0|([1-9][0-9]*))\\.(0|([1-9][0-9]*))\\.(0|([1-9][0-9]*))"

        val REGEX = PATTERN.toRegex()
    }
}
