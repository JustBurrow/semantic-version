package kr.lul.version

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("NonAsciiCharacters")
class VersionCoreTest {
    private val logger = Logger(VersionCoreTest::class)

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
            logger.log("[GIVEN] major=$major, minor=$minor, patch=$patch")

            // WHEN
            val e = assertFailsWith<IllegalArgumentException> {
                VersionCore(major, minor, patch)
            }
            logger.log("[WHEN] e=$e", e)

            // THEN
            assertNotNull(e)
            println()
        }
    }

    @Test
    fun `compareTo - 자기 자신`() {
        for (core in listOf(
            VersionCore(0, 0, 0),
            VersionCore(1, 0, 0),
            VersionCore(0, 2, 0),
            VersionCore(0, 0, 3),
            VersionCore(1, 2, 3)
        )) {
            // GIVEN
            logger.log("[GIVEN] core=$core")

            // WHEN
            val actual = core.compareTo(core)
            logger.log("[WHEN] actual=$actual")

            // THEN
            assertEquals(0, actual)
            println()
        }
    }

    @Test
    fun `compareTo - 버전은 같지만 다른 인스턴스`() {
        for (data in listOf(
            listOf(VersionCore(0, 0, 0), VersionCore(0, 0, 0)),
            listOf(VersionCore(1, 0, 0), VersionCore(1, 0, 0)),
            listOf(VersionCore(0, 2, 0), VersionCore(0, 2, 0)),
            listOf(VersionCore(0, 0, 3), VersionCore(0, 0, 3)),
            listOf(VersionCore(1, 2, 3), VersionCore(1, 2, 3))
        )) {
            // GIVEN
            val core1 = data[0]
            val core2 = data[1]
            logger.log("[GIVEN] core1=$core1, core2=$core2")

            // WHEN
            val actual1 = core1.compareTo(core2)
            val actual2 = core2.compareTo(core1)
            logger.log("[WHEN] actual1=$actual1, actual2=$actual2")

            // THEN
            assertEquals(0, actual1)
            assertEquals(0, actual2)
            println()
        }
    }

    @Test
    fun `compareTo - 서로 다른 버전`() {
        for (data in listOf(
            listOf(VersionCore(0, 0, 0), VersionCore(0, 0, 3)),
            listOf(VersionCore(0, 0, 0), VersionCore(0, 2, 0)),
            listOf(VersionCore(0, 0, 0), VersionCore(1, 0, 0)),
        )) {
            // GIVEN
            val core1 = data[0]
            val core2 = data[1]
            logger.log("[GIVEN] core1=$core1, core2=$core2")

            // WHEN
            val actual1 = core1.compareTo(core2)
            val actual2 = core2.compareTo(core1)
            logger.log("[WHEN] actual1=$actual1, actual2=$actual2")

            // THEN
            assertTrue(core1 < core2)
            assertNotEquals(core1, core2)
            assertTrue(0 > actual1)
            assertTrue(0 < actual2)
            println()
        }
    }
}
