package kr.lul.version

import kotlin.test.Test
import kotlin.test.fail

class DummyTest {
    private val logger = Logger(DummyTest::class)

    @Test
    fun `failing test`() {
        // GIVEN
        logger.log("[GIVEN] ")

        // WHEN
        logger.log("[WHEN] ")

        // THEN
        fail("test fail.")
    }
}