package com.aemtools.blocks.base.model.file

import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture

/**
 * @author Dmytro Troynikov
 */
abstract class BaseFileFixtureDescriptor(protected val _name: String,
                                         protected val _text: String,
                                         protected val fixture: JavaCodeInsightTestFixture)
  : IFileFixtureDescriptor {
  protected var initialized = false
  protected var psiFile: PsiFile? = null

  override fun containsCaret(): Boolean = _text.contains(CodeInsightTestFixture.CARET_MARKER)

  var text: String = _text
    get() = _text

}
