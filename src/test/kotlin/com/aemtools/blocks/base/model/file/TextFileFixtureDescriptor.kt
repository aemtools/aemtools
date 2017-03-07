package com.aemtools.blocks.base.model.file

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.LightPlatformTestCase
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl
import org.junit.Assert
import java.io.File

class TextFileFixtureDescriptor(name: String,
                                text: String,
                                fixture: JavaCodeInsightTestFixture)
    : BaseFileFixtureDescriptor(name, text, fixture) {

    override fun initialize() {
        this.initialized = true
//        psiFile = if (this.containsCaret()) {
//
           // fixture.configureByText(_name, text)
//        } else {
//            fixture.addFileToProject(_name, text)
//        }

        val file = fixture.addFileToProject(_name, text)
                ?: throw AssertionError("Unable to add file to project:\nname: $_name\ntext: $text")


        psiFile = file
        if (this.containsCaret()) {
//        fixture.configureByFile(file.virtualFile.canonicalPath as String)

//            fixture.openFileInEditor(file.virtualFile)
        }


    }

}
