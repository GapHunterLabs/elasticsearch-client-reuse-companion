package dev.gaphunter.elasticsearchclientreusecompanion.model

import com.intellij.psi.PsiElement

/** One `new RestHighLevelClient(...)`/`new ElasticsearchClient(...)` call site built inside a non-constructor method. */
data class ClientBuildHit(val callElement: PsiElement)
