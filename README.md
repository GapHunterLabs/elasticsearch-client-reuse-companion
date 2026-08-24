# Elasticsearch Client Reuse Companion

Warning icon on a `new RestHighLevelClient(...)` (legacy client) or
`new ElasticsearchClient(...)` (modern Java client) construction
written inside a regular method body — Elastic's own documentation
states "only one single instance should be created" and that it's
"best practice to create your RestHighLevelClient instance as
singleton" since "each instance creates its own connection pool".
Building one inside a regular method means a brand new connection
pool on every call.

## Why it exists

`new RestHighLevelClient(RestClient.builder(host))` compiles fine and
returns a working client — call it once per request handler and each
call quietly opens a brand new connection pool instead of reusing the
one the application actually needs.

## Why built this way

- **100% static text/PSI analysis** — matches the class name by simple
  text, so it works whether the real Elasticsearch client jar is on
  the classpath or not. Java and Kotlin.

## v0.1 scope — stated honestly, not exhaustively

Only flags the direct `new` construction shape — a client obtained by
reference from an existing shared instance/dependency injection is
never flagged (correctly, since it isn't the anti-pattern this plugin
targets).

## Usage

Open any Java/Kotlin file using the Elasticsearch client. A client
built inside a regular method shows a warning icon.

## Enterprise / Team Licensing

Need enterprise features, custom rules, or team licensing? Contact us at
**gaphunterlabs@gmail.com**.

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
