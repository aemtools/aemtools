package com.aemtools.blocks.rename.model.fixture

import com.aemtools.blocks.base.BaseLightTest.Companion.CARET
import com.aemtools.blocks.base.model.assertion.AssertionContext
import com.aemtools.blocks.base.model.assertion.IAssertionContext
import com.aemtools.blocks.base.model.fixture.ITestFixtureInternal
import com.intellij.mock.MockVirtualFileSystem
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture

/**
 * @author Dmytro Troynikov
 */
class RenameFixture(val fixture: JavaCodeInsightTestFixture)
  : IRenameFixture, ITestFixtureInternal {
  var before: RenameState? = null
  var beforeInitializer: IRenameState.() -> Unit = {}

  var after: RenameState? = null
  var afterInitializer: IRenameState.() -> Unit = {}

  var newName: String? = null

  override fun init() {
    before = RenameState().apply(beforeInitializer)
    after = RenameState().apply(afterInitializer)
  }

  override fun test() {
    val _before = before
    val _after = after
    val _newName = newName

    if (_before == null) {
      throw IllegalArgumentException("""Please set the "before" state""")
    }
    if (_after == null) {
      throw IllegalArgumentException("""Please set the "after" state""")
    }
    if (_newName == null) {
      throw IllegalArgumentException("""Please call the "renameTo" method""")
    }

    for (file in _before.files) {
      val addedFile = fixture.addFileToProject(file.name, file.text)
      if (file.text.contains(CARET)) {
        fixture.configureFromExistingVirtualFile(addedFile.virtualFile)
      }
    }

    PsiDocumentManager.getInstance(fixture.project).commitAllDocuments()

    fixture.renameElementAtCaret(_newName)

    PsiDocumentManager.getInstance(fixture.project).commitAllDocuments()
    FileDocumentManager.getInstance().saveAllDocuments()

    val mock = MockVirtualFileSystem()
    _after.files.forEach { file ->
      mock.file(file.name, file.text)
    }

    val afterRoot = mock.root

    val beforeRoot = VirtualFileManager.getInstance().findFileByUrl("temp:///src")

    PlatformTestUtil.assertDirectoriesEqual(afterRoot, beforeRoot)
  }

  override fun assertionContext(): IAssertionContext {
    return AssertionContext(fixture)
  }

  override fun before(state: IRenameState.() -> Unit) {
    beforeInitializer = state
  }

  override fun renameTo(name: String) {
    newName = name
  }

  override fun after(state: IRenameState.() -> Unit) {
    afterInitializer = state
  }
}
