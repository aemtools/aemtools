package com.aemtools.blocks.base.model

import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture

/**
 * @author Dmytro Troynikov
 */
interface IFileFixtureDescriptor {

    /**
     * Check if current file contains [com.intellij.testFramework.fixtures.CodeInsightTestFixture.CARET_MARKER]
     */
    fun containsCaret(): Boolean

    /**
     * Add current file fixture to actual fixture.
     */
    fun initialize()

}

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
    }

}
