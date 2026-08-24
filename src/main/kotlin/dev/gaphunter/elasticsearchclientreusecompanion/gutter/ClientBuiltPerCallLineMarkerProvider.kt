package dev.gaphunter.elasticsearchclientreusecompanion.gutter

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import dev.gaphunter.elasticsearchclientreusecompanion.detect.JavaClientBuildFinder
import dev.gaphunter.elasticsearchclientreusecompanion.detect.KotlinClientBuildFinder
import dev.gaphunter.elasticsearchclientreusecompanion.model.ClientBuildHit
import dev.gaphunter.elasticsearchclientreusecompanion.review.ReviewPrompt

class ClientBuiltPerCallLineMarkerProvider : LineMarkerProviderDescriptor(), DumbAware {

    override fun getName(): String = "Elasticsearch client built inside a method"

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
        val file = elements.firstOrNull()?.containingFile ?: return
        val hits = when (file.language.id) {
            "JAVA" -> JavaClientBuildFinder.findAll(file)
            "kotlin" -> KotlinClientBuildFinder.findAll(file)
            else -> emptyList()
        }
        if (hits.isEmpty()) return

        val hitsByElement = hits.associateBy { it.callElement }
        for (element in elements) {
            val hit = hitsByElement[element] ?: continue
            result.add(buildMarker(hit))

            val path = file.virtualFile?.path ?: continue
            val lineNumber = file.viewProvider.document?.getLineNumber(element.textRange.startOffset) ?: -1
            ReviewPrompt.recordHit(file.project, "$path:$lineNumber")
        }
    }

    private fun buildMarker(hit: ClientBuildHit): LineMarkerInfo<PsiElement> {
        val tooltip = "This Elasticsearch client is built here inside a method -- Elastic's own docs say only " +
            "one single instance should be created (best practice: singleton); each instance creates its own " +
            "connection pool, so building one per call is expensive"
        return LineMarkerInfo(
            hit.callElement,
            hit.callElement.textRange,
            ClientReuseIcons.RISK,
            { _: PsiElement -> tooltip },
            null,
            GutterIconRenderer.Alignment.RIGHT,
            { tooltip },
        )
    }
}
