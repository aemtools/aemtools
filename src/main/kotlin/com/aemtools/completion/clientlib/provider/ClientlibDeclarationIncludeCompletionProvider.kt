package com.aemtools.completion.clientlib.provider

import com.aemtools.completion.util.findParentByType
import com.aemtools.completion.util.relativeTo
import com.aemtools.lang.clientlib.psi.CdBasePath
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

/**
 * @author Dmytro_Troynikov
 */
object ClientlibDeclarationIncludeCompletionProvider : CompletionProvider<CompletionParameters>(), DumbAware {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext?, result: CompletionResultSet) {
        if (result.isStopped) {
            return
        }

        val currentElement = parameters.position
        val basePath = currentElement.basePathElement()?.include?.text
        val startDir = findStartDirectory(parameters.originalFile.containingDirectory, basePath)
                ?: return

        val rawVariants = collectRawVariants(parameters, startDir)

        result.addAllElements(rawVariants.map {
            LookupElementBuilder.create(it.relativeTo(startDir.virtualFile.path))
        })
        result.stopHere()
    }

    private fun collectRawVariants(parameters: CompletionParameters, startDir: PsiDirectory): List<String> {
        val currentFile = parameters.originalFile

        fun variantsCollector(dir: PsiDirectory): List<String> {
            val result = ArrayList<String>()
            dir.files.filter { accept(currentFile.name, it.name) }
                    .forEach { result.add(it.virtualFile.path) }
            result.addAll(dir.subdirectories.flatMap(::variantsCollector))
            return result
        }
        return variantsCollector(startDir)
    }

    private fun findStartDirectory(directory: PsiDirectory, basePath: String?): PsiDirectory? {
        if (basePath == null) {
            return directory
        }

        val parts = basePath.split("\\", "/")

        var result: PsiDirectory? = directory
        parts.forEach {
            when (it) {
                ".." -> result = result?.parentDirectory
                "." -> {}
                else -> result = result?.subdirectories?.find { subdirectory -> subdirectory.name == it }
            }
        }

        return result
    }

    private fun accept(currentFileName: String, fileToCheck: String): Boolean = if (currentFileName == "js.txt") {
        fileToCheck.endsWith(".js")
    } else {
        fileToCheck.endsWith(".css") || fileToCheck.endsWith(".less")
    }

    private fun PsiElement.basePathElement(): CdBasePath? {
        val myInclude = this.findParentByType(com.aemtools.lang.clientlib.psi.CdInclude::class.java)
                ?: return null

        var prevSibling: PsiElement? = myInclude
        while (prevSibling != null && prevSibling.prevSibling != null) {
            prevSibling = prevSibling.prevSibling
            if (prevSibling is CdBasePath) {
                return prevSibling
            }
        }
        return null
    }

}