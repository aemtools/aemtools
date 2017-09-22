package com.aemtools.blocks.base.model.file

import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture

class TextFileFixtureDescriptor(name: String,
                                text: String,
                                fixture: JavaCodeInsightTestFixture)
  : BaseFileFixtureDescriptor(name, text, fixture) {

  override fun initialize() {
    val file = fixture.addFileToProject(_name, text)
        ?: throw AssertionError("Unable to add file to project:\nname: $_name\ntext: $text")

    psiFile = file
    if (this.containsCaret()) {
      fixture.configureFromExistingVirtualFile(file.virtualFile)
    }
    this.initialized = true
  }

}
