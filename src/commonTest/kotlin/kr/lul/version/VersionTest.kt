package kr.lul.version

import kr.lul.logging.Logger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("NonAsciiCharacters")
class VersionTest {
    companion object {
        val SORTED_VERSIONS = listOf(
            "0.0.1-rc1",
            "0.0.1-rc2",
            "0.0.1-rc3+abc",
            "0.0.1",
            "1.0.0-0.3.7",
            "1.0.0-alpha",
            "1.0.0-alpha.1",
            "1.0.0-alpha.beta",
            "1.0.0-beta",
            "1.0.0-beta.2",
            "1.0.0-beta.11",
            "1.0.0-rc.1",
            "1.0.0-x.7.z.92",
            "1.0.0",
            "2.0.0-alpha",
            "2.0.0-alpha.1",
            "2.0.0",
            "2.1.0",
            "2.1.1"
        ).map { Version(it) }
    }

    private val logger = Logger(VersionTest::class.qualifiedName!!)

    @Test
    fun `new - core만 설정`() {
        for (core in CoreTest.VALID_SAMPLE) {
            // GIVEN
            logger.i("[GIVEN] core=$core")

            // WHEN
            val version = Version(core)
            logger.i("[WHEN] version=$version")

            // THEN
            assertEquals(core, version.core)
            assertNull(version.preRelease)
            assertNull(version.build)
            assertEquals(core.toString(), version.toString())
            println()
        }
    }

    @Test
    fun `new - core + pre-release 설정`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (preRelease in PreReleaseTest.VALID_SAMPLE) {
                // GIVEN
                logger.i("[GIVEN] core=$core, preRelease=$preRelease")

                // WHEN
                val version = Version(core, preRelease)
                logger.i("[WHEN] version=$version")

                // THEN
                assertEquals(core, version.core)
                assertEquals(preRelease, version.preRelease)
                assertNull(version.build)
                assertEquals("$core-$preRelease", version.toString())
                println()
            }
        }
    }

    @Test
    fun `new - core + build`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (build in BuildTest.VALID_SAMPLE) {
                // GIVEN
                logger.i("[GIVEN] core=$core, build=$build")

                // WHEN
                val version = Version(core = core, build = build)
                logger.i("[WHEN] version=$version")

                // THEN
                assertEquals(core, version.core)
                assertNull(version.preRelease)
                assertEquals(build, version.build)
                assertEquals("$core+$build", version.toString())
                println()
            }
        }
    }

    @Test
    fun `new - core + pre-release + build`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (preRelease in PreReleaseTest.VALID_SAMPLE) {
                for (build in BuildTest.VALID_SAMPLE) {
                    // GIVEN
                    logger.i("[GIVEN] core=$core, preRelease=$preRelease, build=$build")

                    // WHEN
                    val version = Version(core, preRelease, build)
                    logger.i("[WHEN] version=$version")

                    // THEN
                    assertEquals(core, version.core)
                    assertEquals(preRelease, version.preRelease)
                    assertEquals(build, version.build)
                    assertEquals("$core-$preRelease+$build", version.toString())
                    println()
                }
            }
        }
    }

    @Test
    fun `compareTo - core만 있을 때 동일 인스턴스`() {
        for (core in CoreTest.VALID_SAMPLE) {
            // GIVEN
            val version = Version(core)
            logger.i("[GIVEN] version=$version")

            // WHEN
            val compare = version.compareTo(version)
            logger.i("[WHEN] compare=$compare")

            // THEN
            assertEquals(0, compare)
            assertEquals(version, version)
            println()
        }
    }

    @Test
    fun `compareTo - core만 있을 때 값이 같은 인스턴스`() {
        for (core in CoreTest.VALID_SAMPLE) {
            // GIVEN
            val version1 = Version(core)
            val version2 = Version(Core(core.major, core.minor, core.patch))
            logger.i("[GIVEN] version1=$version1")

            // WHEN
            val compare1 = version1.compareTo(version2)
            val compare2 = version2.compareTo(version1)
            logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

            // THEN
            assertEquals(0, compare1)
            assertEquals(0, compare2)
            assertEquals(version1, version2)
            assertEquals(version2, version1)
            println()
        }
    }

    @Test
    fun `compareTo - 서로 다른 core만 있을 때`() {
        for (i in 0..<(CoreTest.VALID_SAMPLE.size - 1)) {
            // GIVEN
            val version1 = Version(CoreTest.VALID_SAMPLE[i])
            val version2 = Version(CoreTest.VALID_SAMPLE[i + 1])
            logger.i("[GIVEN] version1=$version1, version2=$version2")

            // WHEN
            val compare1 = version1.compareTo(version2)
            val compare2 = version2.compareTo(version1)
            logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

            // THEN
            assertTrue(0 > compare1)
            assertTrue(0 < compare2)
            assertTrue(version1 < version2)
            assertTrue(version2 > version1)
            assertNotEquals(version1, version2)
            assertNotEquals(version2, version1)
            println()
        }
    }

    @Test
    fun `compareTo - core + pre-release 동일 인스턴스`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (pr in PreReleaseTest.VALID_SAMPLE) {
                // GIVEN
                val version = Version(core, pr)
                logger.i("[GIVEN] version=$version")

                // WHEN
                val compare = version.compareTo(version)
                logger.i("[WHEN] compare=$compare")

                // THEN
                assertEquals(0, compare)
                assertEquals(version, version)
                println()
            }
        }
    }

    @Test
    fun `compareTo - core + pre-release 값은 같고 서로 다른 인스턴스`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (pr in PreReleaseTest.VALID_SAMPLE) {
                // GIVEN
                val version1 = Version(core, pr)
                val version2 =
                    Version(Core(core.major, core.minor, core.patch), PreRelease(pr.toString()))
                logger.i("[GIVEN] version=$version1, version2=$version2")

                // WHEN
                val compare1 = version1.compareTo(version2)
                val compare2 = version2.compareTo(version1)
                logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

                // THEN
                assertEquals(0, compare1)
                assertEquals(0, compare2)
                assertEquals(version1, version2)
                assertEquals(version2, version1)
                println()
            }
        }
    }

    @Test
    fun `compareTo - core 같고 pre-release 서로 다를 때`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (i in (1..<PreReleaseTest.VALID_SAMPLE.size)) {
                for (j in (0..<i)) {
                    // GIVEN
                    val version1 = Version(core, PreReleaseTest.VALID_SAMPLE[i])
                    val version2 = Version(core, PreReleaseTest.VALID_SAMPLE[j])
                    logger.i("[GIVEN] version1=$version1, version2=$version2")

                    // WHEN
                    val compare1 = version1.compareTo(version2)
                    val compare2 = version2.compareTo(version1)
                    logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

                    // THEN
                    assertTrue(0 < compare1)
                    assertTrue(0 > compare2)
                    assertTrue(version1 > version2)
                    assertTrue(version2 < version1)
                    assertNotEquals(version1, version2)
                    assertNotEquals(version2, version1)
                    println()
                }
            }
        }
    }

    @Test
    fun `compareTo - core 서로 다른 값 + pre-release 같은 값`() {
        for (i in (1..<CoreTest.VALID_SAMPLE.size)) {
            for (j in (0..<i)) {
                for (pr in PreReleaseTest.VALID_SAMPLE) {
                    // GIVEN
                    val version1 = Version(CoreTest.VALID_SAMPLE[i], pr)
                    val version2 = Version(CoreTest.VALID_SAMPLE[j], pr)
                    logger.i("[GIVEN] version1=$version1, version2=$version2")

                    // WHEN
                    val compare1 = version1.compareTo(version2)
                    val compare2 = version2.compareTo(version1)
                    logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

                    // THEN
                    assertTrue(0 < compare1)
                    assertTrue(0 > compare2)
                    assertTrue(version1 > version2)
                    assertTrue(version2 < version1)
                    assertNotEquals(version1, version2)
                    assertNotEquals(version2, version1)
                    println()
                }
            }
        }
    }

    @Test
    fun `compareTo - core + build 동일 인스턴스`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (build in BuildTest.VALID_SAMPLE) {
                // GIVEN
                val version = Version(core, build = build)
                logger.i("[GIVEN] version=$version")

                // WHEN
                val compare = version.compareTo(version)
                logger.i("[WHEN] compare=$compare")

                // THEN
                assertEquals(0, compare)
                assertEquals(version, version)
                println()
            }
        }
    }

    @Test
    fun `compareTo - core + build 값은 같고 서로 다른 인스턴스`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (build in BuildTest.VALID_SAMPLE) {
                // GIVEN
                val version1 = Version(core, build = build)
                val version2 = Version(core, build = build)
                logger.i("[GIVEN] version=$version1, version2=$version2")

                // WHEN
                val compare1 = version1.compareTo(version2)
                val compare2 = version2.compareTo(version1)
                logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

                // THEN
                assertEquals(0, compare1)
                assertEquals(0, compare2)
                assertEquals(version1, version2)
                assertEquals(version2, version1)
                assertFalse(version1 < version2)
                assertFalse(version2 > version1)
                assertEquals(version1, version2)
                assertEquals(version2, version1)
                println()
            }
        }
    }

    @Test
    fun `compareTo - core 같고 build는 서로 다를 때`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (i in (0..<BuildTest.VALID_SAMPLE.size - 1)) {
                for (j in (i + 1..<BuildTest.VALID_SAMPLE.size)) {
                    // GIVEN
                    val version1 = Version(core, build = BuildTest.VALID_SAMPLE[i])
                    val version2 = Version(core, build = BuildTest.VALID_SAMPLE[j])
                    logger.i("[GIVEN] version1=$version1, version2=$version2")

                    // WHEN
                    val compare1 = version1.compareTo(version2)
                    val compare2 = version2.compareTo(version1)
                    logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

                    // THEN
                    assertEquals(0, compare1)
                    assertEquals(0, compare2)
                    assertFalse(version1 < version2)
                    assertFalse(version1 > version2)
                    assertFalse(version2 < version1)
                    assertFalse(version2 > version1)
                    assertNotEquals(version1, version2)
                    assertNotEquals(version2, version1)
                    println()
                }
            }
        }
    }

    @Test
    fun `compareTo - core + pre-release + build 동일 인스턴스`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (preRelease in PreReleaseTest.VALID_SAMPLE) {
                for (build in BuildTest.VALID_SAMPLE) {
                    // GIVEN
                    val version = Version(core, preRelease, build)
                    logger.i("[GIVEN] version=$version")

                    // WHEN
                    val compare = version.compareTo(version)
                    logger.i("[WHEN] compare=$compare")

                    // THEN
                    assertEquals(0, compare)
                    assertFalse(version < version)
                    assertFalse(version > version)
                    assertEquals(version, version)
                    println()
                }
            }
        }
    }

    @Test
    fun `compareTo - core + pre-release + build 서로 다른 인스턴스`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (preRelease in PreReleaseTest.VALID_SAMPLE) {
                for (build in BuildTest.VALID_SAMPLE) {
                    // GIVEN
                    val version1 = Version(core, preRelease, build)
                    val version2 = Version(core, preRelease, build)
                    logger.i("[GIVEN] version=$version1, version2=$version2")

                    // WHEN
                    val compare1 = version1.compareTo(version1)
                    val compare2 = version2.compareTo(version1)
                    logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

                    // THEN
                    assertEquals(0, compare1)
                    assertEquals(0, compare2)
                    assertFalse(version1 < version2)
                    assertFalse(version1 > version2)
                    assertFalse(version2 < version1)
                    assertFalse(version2 > version1)
                    assertEquals(version1, version2)
                    assertEquals(version2, version1)
                    println()
                }
            }
        }
    }

    @Test
    fun `compareTo - 같은 core + 같은 pre-release + 다른 build`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (preRelease in PreReleaseTest.VALID_SAMPLE) {
                for (i in (0..<BuildTest.VALID_SAMPLE.size - 1)) {
                    for (j in (i + 1..<BuildTest.VALID_SAMPLE.size)) {
                        // GIVEN
                        val version1 = Version(core, preRelease, BuildTest.VALID_SAMPLE[i])
                        val version2 = Version(core, preRelease, BuildTest.VALID_SAMPLE[j])
                        logger.i("[GIVEN] version1=$version1, version2=$version2")

                        // WHEN
                        val compare1 = version1.compareTo(version2)
                        val compare2 = version2.compareTo(version1)
                        logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

                        // THEN
                        assertEquals(0, compare1)
                        assertEquals(0, compare2)
                        assertFalse(version1 < version2)
                        assertFalse(version1 > version2)
                        assertNotEquals(version1, version2)
                        assertNotEquals(version2, version1)
                        println()
                    }
                }
            }
        }
    }

    @Test
    fun `compareTo - 같은 core + 다른 pre-release + 같거나 다른 build`() {
        for (core in CoreTest.VALID_SAMPLE) {
            for (i in 0..<PreReleaseTest.VALID_SAMPLE.size - 1) {
                for (j in i + 1..<PreReleaseTest.VALID_SAMPLE.size) {
                    for (build1 in BuildTest.VALID_SAMPLE) {
                        for (build2 in BuildTest.VALID_SAMPLE) {
                            // GIVEN
                            val version1 = Version(core, PreReleaseTest.VALID_SAMPLE[i], build1)
                            val version2 = Version(core, PreReleaseTest.VALID_SAMPLE[j], build2)
                            logger.i("[GIVEN] version1=$version1, version2=$version2")

                            // WHEN
                            val compare1 = version1.compareTo(version2)
                            val compare2 = version2.compareTo(version1)
                            logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

                            // THEN
                            assertTrue(0 > compare1)
                            assertTrue(0 < compare2)
                            assertTrue(version1 < version2)
                            assertTrue(version2 > version1)
                            assertNotEquals(version1, version2)
                            assertNotEquals(version2, version1)
                            println()
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `compareTo - 다른 core + 같은 pre-release + 같거나 다른 build`() {
        for (ci in (0..<CoreTest.VALID_SAMPLE.size - 1)) {
            for (cj in (ci + 1..<CoreTest.VALID_SAMPLE.size)) {
                for (preRelease in PreReleaseTest.VALID_SAMPLE) {
                    for (build1 in BuildTest.VALID_SAMPLE) {
                        for (build2 in BuildTest.VALID_SAMPLE) {
                            // GIVEN
                            val version1 = Version(CoreTest.VALID_SAMPLE[ci], preRelease, build1)
                            val version2 = Version(CoreTest.VALID_SAMPLE[cj], preRelease, build2)
                            logger.i("[GIVEN] ")

                            // WHEN
                            val compare1 = version1.compareTo(version2)
                            val compare2 = version2.compareTo(version1)
                            logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

                            // THEN
                            assertTrue(0 > compare1)
                            assertTrue(0 < compare2)
                            assertTrue(version1 < version2)
                            assertTrue(version2 > version1)
                            assertNotEquals(version1, version2)
                            assertNotEquals(version2, version1)
                            println()
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `compareTo - 다른 core + 다른 pre-release + 같거나 다른 build`() {
        for (ci in (0..<CoreTest.VALID_SAMPLE.size - 1)) {
            for (cj in (ci + 1..<CoreTest.VALID_SAMPLE.size)) {
                for (pri in (0..<PreReleaseTest.VALID_SAMPLE.size - 1)) {
                    for (prj in (pri + 1..<PreReleaseTest.VALID_SAMPLE.size)) {
                        for (build1 in BuildTest.VALID_SAMPLE) {
                            for (build2 in BuildTest.VALID_SAMPLE) {
                                // GIVEN
                                val version1 = Version(
                                    CoreTest.VALID_SAMPLE[ci],
                                    PreReleaseTest.VALID_SAMPLE[pri],
                                    build1
                                )
                                val version2 = Version(
                                    CoreTest.VALID_SAMPLE[cj],
                                    PreReleaseTest.VALID_SAMPLE[prj],
                                    build2
                                )
                                logger.i("[GIVEN] version1=$version1, version2=$version2")

                                // WHEN
                                val compare1 = version1.compareTo(version2)
                                val compare2 = version2.compareTo(version1)
                                logger.i("[WHEN] compare1=$compare1, compare2=$compare2")

                                // THEN
                                assertTrue(0 > compare1)
                                assertTrue(0 < compare2)
                                assertTrue(version1 < version2)
                                assertTrue(version2 > version1)
                                assertNotEquals(version1, version2)
                                assertNotEquals(version2, version1)
                                println()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `compareTo - 버전 목록으로 순서 비교하기`() {
        for (l in 0..<(SORTED_VERSIONS.size - 1)) {
            for (h in (l + 1)..<SORTED_VERSIONS.size) {
                // GIVEN
                val low = SORTED_VERSIONS[l]
                val high = SORTED_VERSIONS[h]
                println("[GIVEN] low=$low, high=$high")

                // WHEN
                val compare1 = low.compareTo(high)
                val compare2 = high.compareTo(low)
                println("[WHEN] compare1=$compare1, compare2=$compare2")

                // THEN
                assertTrue(0 > compare1)
                assertTrue(0 < compare2)
                println()
            }
        }
    }

    @Test
    fun `리스트 정렬하기 테스트`() {
        // GIVEN
        println("[GIVEN] SORTED_VERSIONS=$SORTED_VERSIONS")

        val shuffled = SORTED_VERSIONS.shuffled()
        println("[GIVEN] shuffled=$shuffled")

        // WHEN
        val sorted = shuffled.sorted()
        println("[WHEN] sorted=$sorted")

        // THEN
        assertEquals(SORTED_VERSIONS, sorted)
    }
}
