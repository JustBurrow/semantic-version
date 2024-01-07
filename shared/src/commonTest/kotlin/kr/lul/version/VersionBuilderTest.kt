package kr.lul.version

import kotlin.test.Test
import kotlin.test.assertContains
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
        assertEquals("core is empty.", e.message)
    }

    @Test
    fun `VersionCore - 쓸 수 없는 문자열을 사용했을 때`() {
        for (core in listOf(
            "-1.0.0",
            "0.-1.0",
            "0.0.-1",
            "${Int.MIN_VALUE}.2.3",
            "1.${Int.MIN_VALUE}.3",
            "1.2.${Int.MIN_VALUE}",
            " ",
            " . ",
            " . . ",
            "00.0.0",
            "0.00.0",
            "0.0.00",
            "a.0.0",
            "0.b.0",
            "0.0.c",
            "1",
            "1.0",
            "a",
            "a.b",
            "a.b.c",
            "ab",
            "abc"
        )) {
            // GIVEN
            logger.log("[GIVEN] core=$core")

            // WHEN
            val e = assertFailsWith<IllegalArgumentException> {
                VersionCore(core)
            }
            logger.log("[WHEN] e=$e", e)

            // THEN
            assertNotNull(e)
            with(e.message) {
                assertNotNull(this)
                assertContains(this, "illegal core format")
                assertContains(this, core)
                assertContains(this, VersionCore.PATTERN)
            }
            println()
        }
    }

    @Test
    fun `VersionCore - 문자열을 사용했을 때`() {
        for (data in listOf(
            listOf("0.0.0", 0, 0, 0),
            listOf("0.0.1", 0, 0, 1),
            listOf("0.1.0", 0, 1, 0),
            listOf("1.0.0", 1, 0, 0),
            listOf("1.9.0", 1, 9, 0),
            listOf("1.10.0", 1, 10, 0),
            listOf("2.0.0", 2, 0, 0),
            listOf("2.1.0", 2, 1, 0),
            listOf("2.1.1", 2, 1, 1)
        )) {
            // GIVEN
            val core = data[0] as String
            val major = data[1] as Int
            val minor = data[2] as Int
            val patch = data[3] as Int
            logger.log("[GIVEN] core=$core, major=$major, minor=$minor, patch=$patch")

            // WHEN
            val versionCore = VersionCore(core)
            logger.log("[WHEN] versionCore=$versionCore")

            // THEN
            assertNotNull(versionCore)
            assertEquals(major, versionCore.major)
            assertEquals(minor, versionCore.minor)
            assertEquals(patch, versionCore.patch)
            assertEquals(core, versionCore.toString())
            println()
        }
    }
}
