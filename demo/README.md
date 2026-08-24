# Demo data — Elasticsearch Client Reuse Companion

For capturing the real Marketplace screenshot:

1. `./gradlew runIde`
2. Open `demo/src/main/java/com/acmecorp/search/SearchRepository.java`
   as a scratch/standalone file (or drop it into any sandbox project)
   inside the sandbox IDE.
3. The `new RestHighLevelClient(...)` call inside `search` shows the
   gutter warning icon — hover it for the tooltip. The constructor's
   client build stays clean, for contrast.
4. Enter Full Screen (`View > Appearance > Enter Full Screen`), capture
   with `Win+Shift+S`, save directly to `docs/screenshots/` in this
   repo.
