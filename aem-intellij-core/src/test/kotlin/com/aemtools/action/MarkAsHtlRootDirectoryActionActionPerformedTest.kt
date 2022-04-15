package com.aemtools.action

import com.aemtools.lang.settings.HtlRootDirectories
import com.intellij.ide.plugins.PluginUtil
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiManager
import com.intellij.util.indexing.FileBasedIndex
import org.junit.Test
import org.mockito.Mockito.*

/**
 * @author Dmytro Troynikov
 */
class MarkAsHtlRootDirectoryActionActionPerformedTest
  : MarkAsHtlRootDirectoryActionBaseTest() {

  var targetAction: MarkAsHtlRootDirectoryAction = MarkAsHtlRootDirectoryAction()

  //FIXME test
  /*@Test
  fun `actionPerformed should ignore if presentation is disabled`() {
    `when`(actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))
      .thenReturn(arrayOf(virtualFile));
    `when`(actionEvent.project).thenReturn(mockProject)
    `when`(actionEvent.presentation).thenReturn(presentation)
    `when`(presentation.isEnabledAndVisible).thenReturn(false)

    targetAction.actionPerformed(actionEvent)

    verify(rootDirectories, never()).removeRoot(anyString())
    verify(rootDirectories, never()).addRoot(anyString())
  }

  @Test
  fun `actionPerformed should ignore if project is null`() {
    `when`(actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))
      .thenReturn(arrayOf(virtualFile));
    `when`(actionEvent.project).thenReturn(null)

    targetAction.actionPerformed(actionEvent)

    verify(rootDirectories, never()).removeRoot(anyString())
    verify(rootDirectories, never()).addRoot(anyString())
  }

  @Test
  fun `actionPerformed should return if HtlRootDirectories is not available`() {
    `when`(actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))
      .thenReturn(arrayOf(virtualFile));
    `when`(actionEvent.project).thenReturn(mockProject)
    `when`(actionEvent.presentation).thenReturn(presentation)
    `when`(presentation.isEnabledAndVisible).thenReturn(true)
    `when`(mockProject.getService(HtlRootDirectories::class.java))
      .thenReturn(null)
    `when`(virtualFile.path).thenReturn("/jcr_root/directory");

    targetAction.actionPerformed(actionEvent)

    verify(rootDirectories, never()).removeRoot(anyString())
    verify(rootDirectories, never()).addRoot(anyString())
  }

  @Test
  fun `actionPerformed should add root if action is available`() {
    `when`(actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))
      .thenReturn(arrayOf(virtualFile));
    `when`(actionEvent.project).thenReturn(mockProject)
    `when`(actionEvent.presentation).thenReturn(presentation)
    `when`(presentation.isEnabledAndVisible).thenReturn(true)
    `when`(presentation.text).thenReturn("HTL Root")
    `when`(virtualFile.path).thenReturn("/jcr_root/directory");
    var fbi = mock(FileBasedIndex::class.java)
    `when`(application.getService(FileBasedIndex::class.java))
      .thenReturn(fbi);
    `when`(mockProject.getService(HtlRootDirectories::class.java))
      .thenReturn(rootDirectories)
    `when`(mockProject.getService(PsiManager::class.java))
      .thenReturn(psiManager);
    `when`(application.getService(PluginUtil::class.java))
      .thenReturn(pluginUtil)
    `when`(pluginUtil.getCallerPlugin(anyInt())).thenReturn(pluginId)

    targetAction.actionPerformed(actionEvent)

    verify(rootDirectories, never()).removeRoot("/jcr_root/directory")
    verify(rootDirectories).addRoot("/jcr_root/directory")
  }

  @Test
  fun `actionPerformed should remove root if action is available`() {
    `when`(actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))
      .thenReturn(arrayOf(virtualFile));
    `when`(actionEvent.project).thenReturn(mockProject)
    `when`(actionEvent.presentation).thenReturn(presentation)
    `when`(presentation.isEnabledAndVisible).thenReturn(true)
    `when`(presentation.text).thenReturn("Unmark as HTL Root")
    `when`(virtualFile.path).thenReturn("/jcr_root/directory");
    var fbi = mock(FileBasedIndex::class.java)
    `when`(application.getService(FileBasedIndex::class.java))
      .thenReturn(fbi);
    `when`(mockProject.getService(HtlRootDirectories::class.java))
      .thenReturn(rootDirectories)
    `when`(mockProject.getService(PsiManager::class.java))
      .thenReturn(psiManager);
    `when`(application.getService(PluginUtil::class.java))
      .thenReturn(pluginUtil)
    `when`(pluginUtil.getCallerPlugin(anyInt())).thenReturn(pluginId)

    targetAction.actionPerformed(actionEvent)

    verify(rootDirectories).removeRoot("/jcr_root/directory")
    verify(rootDirectories, never()).addRoot("/jcr_root/directory")
  }*/

}
