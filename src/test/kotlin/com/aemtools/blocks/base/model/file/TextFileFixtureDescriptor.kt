package com.aemtools.blocks.base.model.file

import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture

class TextFileFixtureDescriptor(name: String,
                                text: String,
                                fixture: JavaCodeInsightTestFixture)
    : BaseFileFixtureDescriptor(name, text, fixture) {

    override fun initialize() {
        this.initialized = true
        psiFile = if (this.containsCaret()) {
            fixture.configureByText(_name, text)
        } else {
            fixture.addFileToProject(_name, text)
        }

//        val file = fixture.addFileToProject(_name, text)
//                ?: throw AssertionError("Unable to add file to project:\nname: $_name\ntext: $text")
//
//        psiFile = file
//        fixture.openFileInEditor(file.virtualFile)

    }

}
