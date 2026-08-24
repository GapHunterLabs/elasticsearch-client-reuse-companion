package dev.gaphunter.elasticsearchclientreusecompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavaClientBuildFinderTest : BasePlatformTestCase() {

    fun `test new RestHighLevelClient inside a regular method is flagged`() {
        val file = myFixture.configureByText(
            "SearchRepository.java",
            """
            class SearchRepository {
                Object search(String query) {
                    RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host));
                    return client.search(query);
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, JavaClientBuildFinder.findAll(file).size)
    }

    fun `test new ElasticsearchClient inside a regular method is flagged`() {
        val file = myFixture.configureByText(
            "SearchRepository.java",
            """
            class SearchRepository {
                Object search(String query) {
                    ElasticsearchClient client = new ElasticsearchClient(transport);
                    return client.search(query);
                }
            }
            """.trimIndent(),
        )
        assertEquals(1, JavaClientBuildFinder.findAll(file).size)
    }

    fun `test construction inside a constructor is not flagged`() {
        val file = myFixture.configureByText(
            "SearchRepository.java",
            """
            class SearchRepository {
                private final RestHighLevelClient client;

                SearchRepository() {
                    this.client = new RestHighLevelClient(RestClient.builder(host));
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaClientBuildFinder.findAll(file).isEmpty())
    }

    fun `test unrelated class construction is not flagged`() {
        val file = myFixture.configureByText(
            "SearchRepository.java",
            """
            class SearchRepository {
                Object build() {
                    return new StringBuilder();
                }
            }
            """.trimIndent(),
        )
        assertTrue(JavaClientBuildFinder.findAll(file).isEmpty())
    }
}
