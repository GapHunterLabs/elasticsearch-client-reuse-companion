<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Elasticsearch Client Reuse Companion Changelog

## [Unreleased]

## [0.1.0]

### Added

- Warning icon on `new RestHighLevelClient(...)`/
  `new ElasticsearchClient(...)` built inside a regular method instead
  of reused as a singleton.
- 100% static text/PSI analysis, Java and Kotlin, no network calls,
  no telemetry. Free.

[Unreleased]: https://github.com/GapHunterLabs/elasticsearch-client-reuse-companion/compare/0.1.0...HEAD
[0.1.0]: https://github.com/GapHunterLabs/elasticsearch-client-reuse-companion/commits/0.1.0
