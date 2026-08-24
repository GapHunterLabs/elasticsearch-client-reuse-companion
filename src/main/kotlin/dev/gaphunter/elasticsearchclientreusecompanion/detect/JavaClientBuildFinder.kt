package dev.gaphunter.elasticsearchclientreusecompanion.detect

import com.intellij.psi.JavaRecursiveElementWalkingVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNewExpression
import com.intellij.psi.util.PsiTreeUtil
import dev.gaphunter.elasticsearchclientreusecompanion.model.ClientBuildHit

/**
 * Finds `new RestHighLevelClient(...)` (legacy client) or
 * `new ElasticsearchClient(...)` (modern Java client) constructions
 * written inside a non-constructor method body -- Elastic's own
 * documentation states "only one single instance should be created"
 * and that it's "best practice to create your RestHighLevelClient
 * instance as singleton" since "each instance creates its own
 * connection pool" and creating multiple is expensive. The same
 * singleton guidance is documented for the newer `ElasticsearchClient`.
 *
 * **v0.1 scope, stated honestly:** only the direct `new` construction
 * shape is flagged -- a client obtained by reference from an existing
 * shared instance/dependency injection is never flagged (correctly,
 * since it isn't the anti-pattern this plugin targets).
 */
object JavaClientBuildFinder {

    fun findAll(file: PsiFile): List<ClientBuildHit> {
        val hits = mutableListOf<ClientBuildHit>()
        file.accept(object : JavaRecursiveElementWalkingVisitor() {
            override fun visitNewExpression(expression: PsiNewExpression) {
                super.visitNewExpression(expression)
                hitForDirectNew(expression)?.let { hits += it }
            }
        })
        return hits
    }

    private fun hitForDirectNew(newExpr: PsiNewExpression): ClientBuildHit? {
        val className = newExpr.classReference?.referenceName ?: return null
        if (className !in ElasticClientSignals.CLIENT_CLASS_NAMES) return null
        return hitIfNotInConstructor(newExpr)
    }

    private fun hitIfNotInConstructor(element: PsiElement): ClientBuildHit? {
        val containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java) ?: return null
        if (containingMethod.isConstructor) return null
        return ClientBuildHit(leafOf(element))
    }

    private fun leafOf(element: PsiElement): PsiElement {
        var current = element
        while (current.firstChild != null) current = current.firstChild
        return current
    }
}
