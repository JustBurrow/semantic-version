package kr.lul.version

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@Suppress("NonAsciiCharacters")
class VersionBuilderTest {
    private val logger = Logger(VersionBuilderTest::class)

    @Test
    fun `VersionCore - 빈 문자열로 만들기`() {
        // WHEN
        val e = assertFailsWith<IllegalArgumentException> {
            VersionCore("")
        }
        logger.log("[WHEN] e=$e", e)

        // THEN
        assertNotNull(e)
        assertEquals("versionCore is empty.", e.message)
    }
}
