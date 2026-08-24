package com.acmecorp.search;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Demo data for Elasticsearch Client Reuse Companion — used with
 * `./gradlew runIde` to capture the real Marketplace screenshot. Open
 * this file, the warning icon should appear on the call inside
 * `search`.
 */
public class SearchRepository {

    private final RestHighLevelClient sharedClient;

    public SearchRepository() {
        // Built once, in the constructor -- NOT flagged.
        this.sharedClient = new RestHighLevelClient(RestClient.builder(host));
    }

    public Object search(String query) {
        // Built here on every call -- a fresh connection pool each
        // time. FLAGGED.
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(host));
        return client.search(query);
    }
}
