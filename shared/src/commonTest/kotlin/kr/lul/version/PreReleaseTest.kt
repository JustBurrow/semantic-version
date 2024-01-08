package kr.lul.version

import kr.lul.version.PreRelease.Part
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("NonAsciiCharacters")
class PreReleaseTest {
    companion object {
        /**
         * 낮은 버전의 인덱스가 낮게 정렬한 샘플.
         */
        val VALID_SAMPLE = listOf(
            PreRelease("0"),
            PreRelease("1.2.3"),
            PreRelease("1.2.3.beta"),
            PreRelease("a"),
            PreRelease("alpha"),
            PreRelease("alpha.1"),
            PreRelease("alpha.beta"),
            PreRelease("beta"),
            PreRelease("beta.1"),
            PreRelease("beta.1.2.3.test"),
            PreRelease("beta.2")
        )
    }

    private val logger = Logger(PreReleaseTest::class)

    @Test
    fun `Part new - 사용할 수 없는 값`() {
        for (data in listOf(
            listOf(null, null, "both number and text are null"),
            listOf(-1, null, "number is negative"),
            listOf(null, "", "text is empty"),
            listOf(null, ".", "illegal text format"),
            listOf(null, "-", "illegal text format"),
            listOf(null, "+", "illegal text format"),
            listOf(null, "?", "illegal text format"),
            listOf(null, "아", "illegal text format"),
            listOf(null, "0", "text is number literal"),
            listOf(null, "12", "text is number literal"),
            listOf(0, "", "both number and text can not set"),
            listOf(0, "a", "both number and text can not set"),
            listOf(1, "어", "both number and text can not set"),
            listOf(2, "?", "both number and text can not set"),
            listOf(2, ".", "both number and text can not set")
        )) {
            // GIVEN
            val number = data[0] as Int?
            val text = data[1] as String?
            val message = data[2] as String
            logger.log("[GIVEN] number=$number, text=$text, message=$message")

            // WHEN
            val e = assertFailsWith<IllegalArgumentException> {
                Part(number, text)
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
    fun `Part new - 사용할 수 있는 값`() {
        for (data in listOf(
            listOf(0, null, "0"),
            listOf(null, "a", "a"),
            listOf(1, null, "1"),
            listOf(null, "b01", "b01")
        )) {
            // GIVEN
            val number = data[0] as Int?
            val text = data[1] as String?
            val string = data[2] as String
            logger.log("[GIVEN] number=$number, text=$text, string=$string")

            // WHEN
            val part = Part(number, text)
            logger.log("[WHEN] part=$part")

            // THEN
            assertEquals(number, part.number)
            assertEquals(text, part.text)
            assertEquals(string, part.toString())
            assertEquals(part, part)
            println()
        }
    }

    @Test
    fun `Part compareTo - 동일 인스턴스`() {
        for (part in listOf(
            Part(0),
            Part(1),
            Part(text = "a"),
            Part(text = "beta"),
            Part(text = "b001")
        )) {
            // GIVEN
            logger.log("[GIVEN] part=$part")

            // WHEN
            val actual = part.compareTo(part)
            logger.log("[WHEN] actual=$actual")

            // THEN
            assertEquals(0, actual)
            println()
        }
    }

    @Test
    fun `Part compareTo - 값이 같은 인스턴스`() {
        for (data in listOf(
            listOf(0, null),
            listOf(1, null),
            listOf(null, "a"),
            listOf(null, "abcd"),
            listOf(null, "beta"),
            listOf(null, "b001")
        )) {
            // GIVEN
            val part1 = Part(data[0] as Int?, data[1] as String?)
            val part2 = Part(data[0] as Int?, data[1] as String?)
            logger.log("[GIVEN] part1=$part1, part2=$part2")

            // WHEN
            val compare1 = part1.compareTo(part2)
            val compare2 = part2.compareTo(part1)
            logger.log("[WHEN] compare1=$compare1, compare2=$compare2")

            // THEN
            assertEquals(0, compare1)
            assertEquals(0, compare2)
            assertEquals(part1, part2)
            assertEquals(part2, part1)
            println()
        }
    }

    @Test
    fun `Part compareTo - 값이 다른 인스턴스`() {
        for (data in listOf(
            listOf(Part(0), Part(1)),
            listOf(Part(1), Part(2)),
            listOf(Part(text = "a"), Part(text = "b")),
            listOf(Part(text = "a"), Part(text = "aa")),
            listOf(Part(text = "a0"), Part(text = "a1")),
            listOf(Part(Int.MAX_VALUE), Part(text = "a"))
        )) {
            // GIVEN
            val part1 = data[0]
            val part2 = data[1]
            logger.log("[GIVEN] part1=$part1, part2=$part2")

            // WHEN
            val compare1 = part1.compareTo(part2)
            val compare2 = part2.compareTo(part1)
            logger.log("[WHEN] compare1=$compare1, compare2=$compare2")

            // THEN
            assertTrue(0 > compare1)
            assertTrue(0 < compare2)
            println()
        }
    }

    @Test
    fun `new - 빈 배열`() {
        // WHEN
        val e = assertFailsWith<IllegalArgumentException> {
            PreRelease(listOf())
        }
        logger.log("[WHEN] ")

        // THEN
        assertNotNull(e)
        assertEquals("parts is empty.", e.message)
    }

    @Test
    fun `compareTo - 단일 인스턴스`() {
        for (parts in listOf(
            listOf(Part(0)),
            listOf(Part(1), Part(2)),
            listOf(Part(1), Part(2), Part(3)),
            listOf(Part(text = "a"), Part(text = "b")),
            listOf(Part(1), Part(text = "b"))
        )) {
            // GIVEN
            val preRelease = PreRelease(parts)
            logger.log("[GIVEN] preRelease=$preRelease")

            // WHEN
            val compare = preRelease.compareTo(preRelease)
            logger.log("[WHEN] compare=$compare")

            // THEN
            assertEquals(0, compare)
            assertEquals(compare, compare)
            println()
        }
    }

    @Test
    fun `compareTo - 값이 같은 인스턴스`() {
        for (parts in listOf(
            listOf(Part(0)),
            listOf(Part(1), Part(2)),
            listOf(Part(1), Part(2), Part(3)),
            listOf(Part(text = "a"), Part(text = "b")),
            listOf(Part(1), Part(text = "b"))
        )) {
            // GIVEN
            val preRelease1 = PreRelease(parts)
            val preRelease2 = PreRelease(parts)
            logger.log("[GIVEN] preRelease1=$preRelease1, preRelease2=$preRelease2")

            // WHEN
            val compare1 = preRelease1.compareTo(preRelease2)
            val compare2 = preRelease2.compareTo(preRelease1)
            logger.log("[WHEN] compare1=$compare1, compare2=$compare2")

            // THEN
            assertEquals(0, compare1)
            assertEquals(0, compare2)
            assertEquals(compare1, compare2)
            println()
        }
    }

    @Test
    fun `compareTo - 값이 다른 인스턴스`() {
        for (data in listOf(
            listOf(
                listOf(Part(0)),
                listOf(Part(1))
            ),
            listOf(
                listOf(Part(0), Part(0)),
                listOf(Part(0), Part(1))
            ),
            listOf(
                listOf(Part(0), Part(Int.MAX_VALUE)),
                listOf(Part(1), Part(0))
            ),
            listOf(
                listOf(Part(0), Part(0), Part(0)),
                listOf(Part(0), Part(1), Part(0))
            ),
            listOf(
                listOf(Part(text = "a")),
                listOf(Part(text = "b"))
            ),
            listOf(
                listOf(Part(text = "a"), Part(text = "b"), Part(text = "c")),
                listOf(Part(text = "a"), Part(text = "z"), Part(text = "c"))
            ),
            listOf(
                listOf(Part(0), Part(text = "a")),
                listOf(Part(0), Part(text = "b"))
            ),
            listOf(
                listOf(Part(0), Part(0)),
                listOf(Part(0), Part(0), Part(0))
            ),
            listOf(
                listOf(Part(Int.MAX_VALUE)),
                listOf(Part(text = "a"))
            ),
            listOf(
                listOf(Part(Int.MAX_VALUE), Part(0)),
                listOf(Part(text = "a"))
            ),
            listOf(
                listOf(Part(Int.MAX_VALUE), Part(text = "a")),
                listOf(Part(text = "a"))
            ),
            listOf(
                listOf(Part(0), Part(Int.MAX_VALUE)),
                listOf(Part(0), Part(text = "a"))
            )
        )) {
            // GIVEN
            val pr1 = PreRelease(data[0])
            val pr2 = PreRelease(data[1])
            logger.log("[GIVEN] pr1=$pr1, pr2=$pr2")

            // WHEN
            val compare1 = pr1.compareTo(pr2)
            val compare2 = pr2.compareTo(pr1)
            logger.log("[WHEN] compare1=$compare1, compare2=$compare2")

            // THEN
            assertTrue(0 > compare1)
            assertTrue(0 < compare2)
            assertTrue(pr1 < pr2)
            assertTrue(pr2 > pr1)
            println()
        }
    }
}