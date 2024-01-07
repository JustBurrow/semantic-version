package kr.lul.version

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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

    @Test
    fun `PreRelease - 사용할 수 없는 값`() {
        for (data in listOf(
            listOf("", "preRelease is empty"),
            listOf(" ", "illegal preRelease pattern"),
            listOf("\n", "illegal preRelease pattern"),
            listOf("\t", "illegal preRelease pattern"),
            listOf("a,b", "illegal preRelease pattern"),
            listOf(".1", "illegal preRelease pattern"),
            listOf(".b", "illegal preRelease pattern"),
            listOf("0.", "illegal preRelease pattern"),
            listOf("0.1.", "illegal preRelease pattern"),
            listOf("a.", "illegal preRelease pattern"),
            listOf("a.b.", "illegal preRelease pattern"),
        )) {
            // GIVEN
            val parts = data[0]
            val message = data[1]
            logger.log("[GIVEN] parts=$parts, message=$message")

            // WHEN
            val e = assertFailsWith<IllegalArgumentException> {
                PreRelease(parts)
            }
            logger.log("[WHEN] e=$e", e)

            // THEN
            assertNotNull(e)
            with(e.message) {
                assertNotNull(this)
                assertContains(this, message)
            }
            println()
        }
    }

    @Test
    fun `PreRelease - 사용할 수 있는 값`() {
        for (preRelease in listOf(
            "0",
            "a",
            "1.2.3",
            "1.2.3.beta",
            "beta",
            "beta.1",
            "beta.1.2.3.test",
            "alpha",
            "alpha.1",
            "alpha.beta",
            "beta",
            "beta.2"
        )) {
            logger.log("[GIVEN] preRelease=$preRelease")

            // WHEN
            val actual = PreRelease(preRelease)
            logger.log("[WHEN] actual=$actual")

            // THEN
            assertIs<PreRelease>(actual)
            assertEquals(preRelease, actual.toString())
            println()
        }
    }

    @Test
    fun `PreRelease - 인스턴스 비교`() {
        for (data in listOf(
            listOf("alpha", "alpha.1"),
            listOf("alpha", "alpha.beta"),
            listOf("alpha", "beta"),
            listOf("alpha", "beta.2"),
            listOf("alpha", "beta.11"),
            listOf("alpha", "rc.1"),
            listOf("alpha.1", "alpha.beta"),
            listOf("alpha.1", "beta"),
            listOf("alpha.1", "beta.2"),
            listOf("alpha.1", "beta.11"),
            listOf("alpha.1", "rc.1"),
            listOf("alpha.beta", "beta"),
            listOf("alpha.beta", "beta.2"),
            listOf("alpha.beta", "beta.11"),
            listOf("alpha.beta", "rc.1"),
            listOf("beta", "beta.2"),
            listOf("beta", "beta.11"),
            listOf("beta", "rc.1"),
            listOf("beta.2", "beta.11"),
            listOf("beta.2", "rc.1"),
        )) {
            // GIVEN
            logger.log("[GIVEN] data=$data")

            // WHEN
            val pr1 = PreRelease(data[0])
            val pr2 = PreRelease(data[1])
            logger.log("[WHEN]  pr1=$pr1, pr2=$pr2")

            // THEN
            assertTrue(pr1 < pr2)
            assertTrue(pr2 > pr1)
            println()
        }
    }
}
