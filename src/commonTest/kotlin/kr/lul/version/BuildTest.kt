package kr.lul.version

import kr.lul.logging.Logger
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

@Suppress("NonAsciiCharacters")
class BuildTest {
    companion object {
        val VALID_SAMPLE = listOf(
            Build("001"),
            Build("20130313144700"),
            Build("exp.sha.5114f85"),
            null
        )
    }

    private val logger = Logger(BuildTest::class.qualifiedName!!)
    private val validSample = listOf(
        "001",
        "20130313144700",
        "exp.sha.5114f85"
    )

    @Test
    fun `new - 빈 문자열`() {
        // WHEN
        val e = assertFailsWith<IllegalArgumentException> {
            Build("")
        }
        logger.i("[WHEN] e=$e", e)

        // THEN
        assertNotNull(e)
        with(e.message) {
            assertNotNull(this)
            assertEquals("build is empty.", this)
        }
    }

    @Test
    fun `new - 사용할 수 없는 문자열`() {
        for (build in listOf(
            "-",
            ".",
            "+",
            "ㅁ",
            ".a",
            "a.",
            ".1",
            "1.",
            "1.2ㅁ",
            "ㅠ1.2"
        )) {
            // GIVEN
            logger.i("[GIVEN] build=$build")

            // WHEN
            val e = assertFailsWith<IllegalArgumentException> { Build(build) }
            logger.i("[WHEN] e=$e", e)

            // THEN
            assertNotNull(e)
            with(e.message) {
                assertNotNull(this)
                assertContains(this, "illegal build format")
            }
            println()
        }
    }

    @Test
    fun `new - 사용할 수 있는 문자열`() {
        for (build in validSample) {
            // GIVEN
            logger.i("[GIVEN] build=$build")

            // WHEN
            val actual = Build(build)
            logger.i("[WHEN] actual=$actual")

            // THEN
            assertEquals(build, actual.toString())
            println()
        }
    }

    @Test
    fun `== - 동일 인스턴스`() {
        for (build in VALID_SAMPLE) {
            // WHEN
            logger.i("[WHEN] build=$build")

            // THEN
            assertEquals(build, build)
            println()
        }
    }

    @Test
    fun `== - 같은 값을 가진 서로 다른 인스턴스`() {
        for (build in validSample) {
            // GIVEN
            logger.i("[GIVEN] build=$build")

            // WHEN
            val b1 = Build(build)
            val b2 = Build(build)
            logger.i("[WHEN] b1=$b1, b2=$b2")

            // THEN
            assertEquals(b1, b2)
            assertEquals(b2, b1)
            println()
        }
    }

    @Test
    fun `== - 서로 다른 값을 가진 인스턴스`() {
        for (i in (1..50)) {
            // GIVEN
            val data = validSample.shuffled()
            val b1 = data[0]
            val b2 = data[1]
            logger.i("[GIVEN] b1=$b1, b2=$b2")

            // WHEN
            val build1 = Build(b1)
            val build2 = Build(b2)
            logger.i("[WHEN] build1=$build1, build2=$build2")

            // THEN
            assertNotEquals(build1, build2)
            assertNotEquals(build2, build1)
            println()
        }
    }
}
