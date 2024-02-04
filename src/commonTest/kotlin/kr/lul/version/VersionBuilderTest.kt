package kr.lul.version

import kr.lul.logging.Logger
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("NonAsciiCharacters")
class VersionBuilderTest {
    private val logger = Logger(VersionBuilderTest::class.qualifiedName!!)

    @Test
    fun `Core - 빈 문자열로 만들기`() {
        // WHEN
        val e = assertFailsWith<IllegalArgumentException> {
            Core("")
        }
        logger.i("[WHEN] e=$e", e)

        // THEN
        assertNotNull(e)
        assertEquals("core is empty.", e.message)
    }

    @Test
    fun `Core - 쓸 수 없는 문자열을 사용했을 때`() {
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
            logger.i("[GIVEN] core=$core")

            // WHEN
            val e = assertFailsWith<IllegalArgumentException> {
                Core(core)
            }
            logger.i("[WHEN] e=$e", e)

            // THEN
            assertNotNull(e)
            with(e.message) {
                assertNotNull(this)
                assertContains(this, "illegal core format")
                assertContains(this, core)
                assertContains(this, Core.PATTERN)
            }
            println()
        }
    }

    @Test
    fun `Core - 문자열을 사용했을 때`() {
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
            logger.i("[GIVEN] core=$core, major=$major, minor=$minor, patch=$patch")

            // WHEN
            val versionCore = Core(core)
            logger.i("[WHEN] versionCore=$versionCore")

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
            listOf("01", "text is number literal"),
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
            logger.i("[GIVEN] parts=$parts, message=$message")

            // WHEN
            val e = assertFailsWith<IllegalArgumentException> {
                PreRelease(parts)
            }
            logger.i("[WHEN] e=$e", e)

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
            logger.i("[GIVEN] preRelease=$preRelease")

            // WHEN
            val actual = PreRelease(preRelease)
            logger.i("[WHEN] actual=$actual")

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
            logger.i("[GIVEN] data=$data")

            // WHEN
            val pr1 = PreRelease(data[0])
            val pr2 = PreRelease(data[1])
            logger.i("[WHEN]  pr1=$pr1, pr2=$pr2")

            // THEN
            assertTrue(pr1 < pr2)
            assertTrue(pr2 > pr1)
            println()
        }
    }

    @Test
    fun `Version - 빈 문자열`() {
        // WHEN
        val e = assertFailsWith<IllegalArgumentException> {
            Version("")
        }
        logger.i("[WHEN] e=$e", e)

        // THEN
        assertNotNull(e)
        assertEquals("version is empty.", e.message)
    }

    @Test
    fun `Version - 사용할 수 없는 버전`() {
        for (version in listOf(
            "1",
            ".",
            "-",
            "+",
            "?",
            "ㅁ",
            "🎉",
            "1.",
            "1.2",
            "1.2.",
            "1.2.3.",
            "1--",
            "1-+",
            "1++",
            "1.--",
            "1.-+",
            "1.++",
            "1.2--",
            "1.2-+",
            "1.2++",
            "1.2.--",
            "1.2.-+",
            "1.2.++",
            "1.2.3--",
            "1.2.3-+",
            "1.2.3.++",
            "1.2.3.--",
            "1.2.3.-+",
            "1.2.3.++",
            "1.0.0--alpha",
            "1.0.0-01",
            "1.0.0-001",
            "1.0.0-alpha-Z",
            "1.0.0-023++a",
            "1.0.0++a",
            "001",
            "abc",
        )) {
            // GIVEN
            logger.i("[GIVEN] version=$version")

            // WHEN
            val e = assertFailsWith<IllegalArgumentException> { Version(version) }
            logger.i("[WHEN] e=$e", e)

            // THEN
            assertNotNull(e)
            with(e.message) {
                assertNotNull(this)
                assertContains(this, "illegal version pattern")
                assertContains(this, "version=$version")
                assertContains(this, "pattern=${Version.PATTERN}")
            }
            println()
        }
    }

    @Test
    fun `Version - 사용할 수 있는 문자열`() {
        for (data in listOf(
            listOf("1.0.0", 1, 0, 0, null, null),
            listOf("1.9.0-alpha", 1, 9, 0, "alpha", null),
            listOf("1.10.0-alpha.1", 1, 10, 0, "alpha.1", null),
            listOf("2.0.0-alpha.beta", 2, 0, 0, "alpha.beta", null),
            listOf("2.0.1-0a", 2, 0, 1, "0a", null),
            listOf("2.1.0-0a+000", 2, 1, 0, "0a", "000"),
            listOf("2.3.4+001", 2, 3, 4, null, "001"),
            listOf("0.0.1+20130313144700", 0, 0, 1, null, "20130313144700"),
            listOf("0.1.0+exp.sha.5114f85", 0, 1, 0, null, "exp.sha.5114f85"),
            listOf("10.100.1000-rc.1+001.29.aaa", 10, 100, 1000, "rc.1", "001.29.aaa")
        )) {
            // GIVEN
            val version = data[0] as String
            val major = data[1] as Int
            val minor = data[2] as Int
            val patch = data[3] as Int
            val preRelease = data[4] as String?
            val build = data[5] as String?
            logger.i("[GIVEN] version=$version, major=$major, minor=$minor, patch=$patch, preRelease=$preRelease, build=$build")

            // WHEN
            val actual = Version(version)
            logger.i("[WHEN] actual=$actual")

            // THEN
            assertEquals(major, actual.major)
            assertEquals(minor, actual.minor)
            assertEquals(patch, actual.patch)
            assertEquals(preRelease, actual.preRelease?.toString())
            assertEquals(build, actual.build?.toString())
            assertEquals(version, actual.toString())
            println()
        }
    }
}
