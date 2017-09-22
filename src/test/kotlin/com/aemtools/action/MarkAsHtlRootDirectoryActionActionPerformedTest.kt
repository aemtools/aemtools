package com.aemtools.action

import com.aemtools.blocks.util.mock
import com.aemtools.index.HtlTemplateIndex
import com.intellij.psi.PsiManager
import com.intellij.util.FileContentUtil
import com.intellij.util.indexing.FileBasedIndex
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

/**
 * @author Dmytro Troynikov
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(
    FileContentUtil::class,
    FileBasedIndex::class
)
class MarkAsHtlRootDirectoryActionActionPerformedTest
  : MarkAsHtlRootDirectoryActionBaseTest() {

  @Test
  fun `actionPerformed should ignore if presentation is disabled`() {
    `when`(presentation.isEnabledAndVisible)
        .thenReturn(false)

    action.actionPerformed(actionEvent)

    verify(rootDirectories, never())
        .addRoot(anyString())

    verify(rootDirectories, never())
        .removeRoot(anyString())
  }

  @Test
  fun `actionPerformed should ignore if project is null`() {
    `when`(actionEvent.project)
        .thenReturn(null)

    action.actionPerformed(actionEvent)

    verify(rootDirectories, never())
        .addRoot(anyString())

    verify(rootDirectories, never())
        .removeRoot(anyString())
  }

  @Test
  fun `actionPerformed should return if HtlRootDirectories is not available`() {
    `when`(presentation.isEnabledAndVisible)
        .thenReturn(true)

    action.actionPerformed(actionEvent)

    verify(rootDirectories, never())
        .addRoot(ArgumentMatchers.anyString())
    verify(rootDirectories, never())
        .removeRoot(ArgumentMatchers.anyString())
  }

  @Test
  fun `actionPerformed should add root if action is available`() {
    `when`(presentation.isEnabledAndVisible)
        .thenReturn(true)
    `when`(picoContainer.getComponentInstance("com.aemtools.settings.HtlRootDirectories"))
        .thenReturn(rootDirectories)
    `when`(presentation.text)
        .thenReturn("HTL Root")
    `when`(mockProject.getComponent(PsiManager::class.java))
        .thenReturn(psiManager)

    val fbi: FileBasedIndex = mock()
    PowerMockito.mockStatic(FileBasedIndex::class.java)
    `when`(FileBasedIndex.getInstance())
        .thenReturn(fbi)

    PowerMockito.mockStatic(FileContentUtil::class.java)

    action.actionPerformed(actionEvent)

    PowerMockito.verifyStatic(FileContentUtil::class.java)
    FileContentUtil.reparseOpenedFiles()

    PowerMockito.verifyStatic(FileBasedIndex::class.java)
    FileBasedIndex.getInstance()

    verify(fbi)
        .requestRebuild(HtlTemplateIndex.HTL_TEMPLATE_ID)

    verify(rootDirectories)
        .addRoot("/jcr_root/directory")
    verify(rootDirectories, never())
        .removeRoot("/jcr_root/directory")
  }

  @Test
  fun `actionPerformed should remove root if action is available`() {
    `when`(presentation.isEnabledAndVisible)
        .thenReturn(true)
    `when`(picoContainer.getComponentInstance("com.aemtools.settings.HtlRootDirectories"))
        .thenReturn(rootDirectories)
    `when`(presentation.text)
        .thenReturn("Unmark as HTL Root")
    `when`(mockProject.getComponent(PsiManager::class.java))
        .thenReturn(psiManager)

    val fbi: FileBasedIndex = mock()
    PowerMockito.mockStatic(FileBasedIndex::class.java)
    `when`(FileBasedIndex.getInstance())
        .thenReturn(fbi)

    PowerMockito.mockStatic(FileContentUtil::class.java)

    action.actionPerformed(actionEvent)

    PowerMockito.verifyStatic(FileContentUtil::class.java)
    FileContentUtil.reparseOpenedFiles()

    PowerMockito.verifyStatic(FileBasedIndex::class.java)
    FileBasedIndex.getInstance()

    verify(fbi)
        .requestRebuild(HtlTemplateIndex.HTL_TEMPLATE_ID)

    verify(rootDirectories, never())
        .addRoot("/jcr_root/directory")
    verify(rootDirectories)
        .removeRoot("/jcr_root/directory")
  }

}
