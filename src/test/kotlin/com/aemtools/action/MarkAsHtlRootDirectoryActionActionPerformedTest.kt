package com.aemtools.action

import com.aemtools.index.HtlTemplateIndex
import com.aemtools.settings.HtlRootDirectories
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.application.Application
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.util.indexing.FileBasedIndex
import mockit.Expectations
import mockit.Mocked
import mockit.Tested
import mockit.Verifications
import mockit.integration.junit4.JMockit
import org.junit.Test
import org.junit.runner.RunWith
import org.picocontainer.PicoContainer

/**
 * @author Dmytro Troynikov
 */
@RunWith(JMockit::class)
class MarkAsHtlRootDirectoryActionActionPerformedTest {

  @Mocked lateinit var actionEvent: AnActionEvent

  @Mocked lateinit var presentation: Presentation

  @Mocked lateinit var virtualFile: VirtualFile

  @Mocked lateinit var mockProject: Project

  @Mocked lateinit var picoContainer: PicoContainer

  @Mocked lateinit var rootDirectories: HtlRootDirectories

  @Mocked lateinit var psiManager: PsiManager

  @Mocked lateinit var application: Application

  @Mocked lateinit var projectManager: ProjectManager

  @Mocked lateinit var fileEditorManager: FileEditorManager

  @Mocked lateinit var fileBasedIndex: FileBasedIndex

  @Tested
  private var action: MarkAsHtlRootDirectoryAction = MarkAsHtlRootDirectoryAction()

  @Test
  fun `actionPerformed should ignore if presentation is disabled`(
      @Mocked serviceManager: ServiceManager
  ) {
    object : Expectations(serviceManager) {init {
      actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
      result = arrayOf(virtualFile)

      actionEvent.project; result = mockProject
      actionEvent.presentation; result = presentation
      presentation.isEnabledAndVisible; result = false
    }
    }

    action.actionPerformed(actionEvent)

    object : Verifications() {init {
      rootDirectories.removeRoot(anyString); maxTimes = 0
      rootDirectories.addRoot(anyString); maxTimes = 0
    }
    }
  }

  @Test
  fun `actionPerformed should ignore if project is null`() {
    object : Expectations() {init {
      actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
      result = arrayOf(virtualFile)

      actionEvent.project; result = null
    }
    }

    action.actionPerformed(actionEvent)

    object : Verifications() {init {
      rootDirectories.removeRoot(anyString); maxTimes = 0
      rootDirectories.addRoot(anyString); maxTimes = 0
    }
    }
  }

  @Test
  fun `actionPerformed should return if HtlRootDirectories is not available`(
      @Mocked serviceManager: ServiceManager
  ) {
    object : Expectations() {init {
      actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
      result = arrayOf(virtualFile)

      actionEvent.project; result = mockProject
      actionEvent.presentation; result = presentation
      presentation.isEnabledAndVisible; result = true
      ServiceManager.getService(mockProject, HtlRootDirectories::class.java); result = null
      virtualFile.path; result = "/jcr_root/directory"
    }
    }

    action.actionPerformed(actionEvent)

    object : Verifications() {init {
      rootDirectories.removeRoot(anyString); maxTimes = 0
      rootDirectories.addRoot(anyString); maxTimes = 0
    }
    }
  }

  @Test
  fun `actionPerformed should add root if action is available`(
      @Mocked serviceManager: ServiceManager,
      @Mocked psiManager: PsiManager,
      @Mocked fbi: FileBasedIndex
  ) {
    object : Expectations() {init {
      actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
      result = arrayOf(virtualFile)

      actionEvent.project; result = mockProject
      actionEvent.presentation; result = presentation
      presentation.isEnabledAndVisible; result = true

      presentation.text; result = "HTL Root"
      virtualFile.path; result = "/jcr_root/directory"
      FileBasedIndex.getInstance(); result = fbi
    }
    }

    action.actionPerformed(actionEvent)

    object : Verifications() {init {
      rootDirectories.addRoot("/jcr_root/directory"); times = 1
      rootDirectories.removeRoot("/jcr_root/directory"); times = 0
      fbi.requestRebuild(HtlTemplateIndex.HTL_TEMPLATE_ID)
    }
    }
  }

  @Test
  fun `actionPerformed should remove root if action is available`(
      @Mocked serviceManager: ServiceManager,
      @Mocked psiManager: PsiManager,
      @Mocked fbi: FileBasedIndex
  ) {
    object : Expectations() {init {
      actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
      result = arrayOf(virtualFile)

      actionEvent.project; result = mockProject
      actionEvent.presentation; result = presentation
      presentation.isEnabledAndVisible; result = true

      presentation.text; result = "Unmark as HTL Root"
      virtualFile.path; result = "/jcr_root/directory"
      FileBasedIndex.getInstance(); result = fbi
    }
    }

    action.actionPerformed(actionEvent)

    object : Verifications() {init {
      rootDirectories.addRoot("/jcr_root/directory"); times = 0
      rootDirectories.removeRoot("/jcr_root/directory"); times = 1
      fbi.requestRebuild(HtlTemplateIndex.HTL_TEMPLATE_ID)
    }
    }
  }

}
