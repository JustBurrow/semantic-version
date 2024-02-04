package kr.lul.version

import kr.lul.logging.Logger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("NonAsciiCharacters")
class CoreTest {
    companion object {
        /**
         * 낮은 버전의 인덱스가 낮게 정렬한 샘플.
         */
        val VALID_SAMPLE = listOf(
            Core(0, 0, 0),
            Core(0, 0, 3),
            Core(0, 0, Int.MAX_VALUE),
            Core(0, 2, 0),
            Core(0, Int.MAX_VALUE, 0),
            Core(1, 0, 0),
            Core(1, 2, 3),
            Core(Int.MAX_VALUE, 0, 0),
            Core(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
        )
    }

    private val logger = Logger(CoreTest::class.qualifiedName!!)

    @Test
    fun `new - 쓸 수 없는 숫자`() {
        for (data in listOf(
            listOf(-1, 0, 0),
            listOf(0, -1, 0),
            listOf(0, 0, -1),
            listOf(Int.MIN_VALUE, 2, 3),
            listOf(1, Int.MIN_VALUE, 3),
            listOf(1, 2, Int.MIN_VALUE)
        )) {
            // GIVEN
            val major = data[0]
            val minor = data[1]
            val patch = data[2]
            logger.i("[GIVEN] major=$major, minor=$minor, patch=$patch")

            // WHEN
            val e = assertFailsWith<IllegalArgumentException> {
                Core(major, minor, patch)
            }
            logger.i("[WHEN] e=$e", e)

            // THEN
            assertNotNull(e)
            println()
        }
    }

    @Test
    fun `compareTo - 자기 자신`() {
        for (core in VALID_SAMPLE) {
            // GIVEN
            logger.i("[GIVEN] core=$core")

            // WHEN
            val actual = core.compareTo(core)
            logger.i("[WHEN] actual=$actual")

            // THEN
            assertEquals(0, actual)
            println()
        }
    }

    @Test
    fun `compareTo - 버전은 같지만 다른 인스턴스`() {
        for (data in VALID_SAMPLE.map {
            listOf(it, Core(it.major, it.minor, it.patch))
        }) {
            // GIVEN
            val core1 = data[0]
            val core2 = data[1]
            logger.i("[GIVEN] core1=$core1, core2=$core2")

            // WHEN
            val actual1 = core1.compareTo(core2)
            val actual2 = core2.compareTo(core1)
            logger.i("[WHEN] actual1=$actual1, actual2=$actual2")

            // THEN
            assertEquals(0, actual1)
            assertEquals(0, actual2)
            println()
        }
    }

    @Test
    fun `compareTo - 서로 다른 버전`() {
        for (i in (0..<VALID_SAMPLE.size - 1)) {
            for (j in (i + 1..<VALID_SAMPLE.size)) {
                // GIVEN
                val core1 = VALID_SAMPLE[i]
                val core2 = VALID_SAMPLE[j]
                logger.i("[GIVEN] core1=$core1, core2=$core2")

                // WHEN
                val actual1 = core1.compareTo(core2)
                val actual2 = core2.compareTo(core1)
                logger.i("[WHEN] actual1=$actual1, actual2=$actual2")

                // THEN
                assertTrue(core1 < core2)
                assertNotEquals(core1, core2)
                assertTrue(0 > actual1)
                assertTrue(0 < actual2)
                println()
            }
        }
    }
}
