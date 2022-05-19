package com.aemtools.documentation.clientlibs.generator

import com.aemtools.common.util.normalizeToJcrRoot
import com.aemtools.index.ClientLibraryIndexFacade
import com.intellij.psi.PsiElement

/**
 * Generates clientlib category documentation.
 *
 * @author Kostiantyn Diachenko
 */
object ClientlibDocumentationGenerator {

  fun generateDoc(originalElement: PsiElement, category: String): String? {
    val clientlibs = ClientLibraryIndexFacade
        .findClientlibsByCategory(originalElement.project, category)

    if (clientlibs.isEmpty()) {
      return null
    }

    return buildString {
      append("<html><head></head><body>")
      append("<h2>Category: ${category}</h2>")

      val dependencies = clientlibs.flatMap { it.dependencies }
      val embeds = clientlibs.flatMap { it.embed }

      append("<h2>Declared in:</h2>")
      clientlibs.forEach { model ->
        append("<a href=\"${model.filePath}\">${model.filePath.normalizeToJcrRoot()}</a><br/>")
      }

      if (dependencies.isNotEmpty()) {
        append("<h2>Depends on:</h2>")
        append("<ul>")
        dependencies.forEach { dependency ->
          append("<li>$dependency</li>")
        }
        append("</ul>")
      }

      if (embeds.isNotEmpty()) {
        append("<h2>Embeds:</h2>")
        append("<ul>")
        embeds.forEach { embed ->
          append("<li>$embed</li>")
        }
        append("</ul>")
      }

      append("</body></html>")
    }
  }
}
