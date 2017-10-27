package com.aemtools.blocks.base.model.file

import com.intellij.psi.PsiClass
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture

/**
 * @author Dmytro Troynikov
 */
class JavaClassFileFixtureDescriptor(
    name: String,
    text: String,
    fixture: JavaCodeInsightTestFixture)
  : BaseFileFixtureDescriptor(name, text, fixture) {

  private var psiClass: PsiClass? = null

  override fun initialize() {
    this.psiClass = fixture.addClass(text)
    val file = psiClass?.containingFile
        ?: throw AssertionError("Unable to get PsiFile from $psiClass")

    this.psiFile = file
    if (this.containsCaret()) {
      fixture.configureFromExistingVirtualFile(file.virtualFile)
    }
    initialized = true
  }

  private val CLASS_NAME_REGEX
      = Regex("class (\\w+)", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

  private val PACKAGE_REGEX
      = Regex("package ([\\w.]+)", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

  fun qualifiedName(): String = "${packageName()}.${className()}"

  fun packageName(): String = PACKAGE_REGEX
      .find(text)
      ?.groupValues?.get(1)
      ?: throw AssertionError("Unable get package name from $text")

  fun className(): String = CLASS_NAME_REGEX
      .find(text)
      ?.groupValues?.get(1)
      ?: throw AssertionError("Unable to get class name from $text")

  override fun toString(): String = _text

}
