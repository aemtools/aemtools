package com.aemtools.inspection.xml

import com.aemtools.common.util.findChildrenByType
import com.aemtools.inspection.common.AemIntellijInspection
import com.aemtools.inspection.xml.fix.AddMissedNamespaceAction
import com.aemtools.lang.util.isFileWithCqNamespace
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlToken

class AemMissedCqNamespaceInspection : AemIntellijInspection(
    name = "Add missed 'cq' namespace",
    groupName = "AEM",
    description = """
      Adds missed 'cq' namespace for _cq_dialog.xml or _cq_editConfig.xml
    """.trimIndent()
) {
  override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
    return object : XmlElementVisitor() {
      override fun visitXmlTag(tag: XmlTag) {
        if (tag.containingFile !is XmlFile
            || tag.name != "jcr:root"
            || !tag.localNamespaceDeclarations["cq"].isNullOrEmpty()
            || !tag.isFileWithCqNamespace()) {
          return
        }

        val xmlToken = tag.findChildrenByType(XmlToken::class.java)
            .find { it.text == "jcr:root" } ?: return

        holder.registerProblem(xmlToken, "Missed 'cq' namespace",
            ProblemHighlightType.ERROR,
            AddMissedNamespaceAction(tag, "cq"))
      }
    }
  }
}
