package kr.lul.version

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class VersionBuilderTest : BehaviorSpec() {
    private val logger = Logger(VersionBuilderTest::class)

    override fun isolationMode() = IsolationMode.InstancePerLeaf

    init {
        given("VersionCore를") {
            `when`("빈 문자열로 호출하면") {
                val e = shouldThrow<IllegalArgumentException> {
                    VersionCore("")
                }
                logger.log("[WHEN] e=$e")

                then("에러를 던진다.") {
                    e shouldNotBe null
                    e.message shouldBe "core is empty."
                }
            }
        }
    }
}
