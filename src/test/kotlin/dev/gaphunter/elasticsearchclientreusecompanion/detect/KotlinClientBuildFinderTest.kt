package dev.gaphunter.elasticsearchclientreusecompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class KotlinClientBuildFinderTest : BasePlatformTestCase() {

    fun `test new RestHighLevelClient inside a regular method is flagged`() {
        val file = myFixture.configureByText(
            "SearchRepository.kt",
            """
            class SearchRepository {
                fun search(query: String): Any {
                    val client = RestHighLevelClient(RestClient.builder(host))
                    return client.search(query)
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, KotlinClientBuildFinder.findAll(file).size)
    }

    fun `test new ElasticsearchClient inside a regular method is flagged`() {
        val file = myFixture.configureByText(
            "SearchRepository.kt",
            """
            class SearchRepository {
                fun search(query: String): Any {
                    val client = ElasticsearchClient(transport)
                    return client.search(query)
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, KotlinClientBuildFinder.findAll(file).size)
    }

    fun `test construction inside a class initializer is not flagged`() {
        val file = myFixture.configureByText(
            "SearchRepository.kt",
            """
            class SearchRepository {
                private val client = RestHighLevelClient(RestClient.builder(host))
            }
            """.trimIndent(),
        )
        assertTrue(KotlinClientBuildFinder.findAll(file).isEmpty())
    }

    fun `test unrelated class construction is not flagged`() {
        val file = myFixture.configureByText(
            "SearchRepository.kt",
            """
            class SearchRepository {
                fun build(): Any {
                    return StringBuilder()
                }
            }
            """.trimIndent(),
        )
        assertTrue(KotlinClientBuildFinder.findAll(file).isEmpty())
    }
}
