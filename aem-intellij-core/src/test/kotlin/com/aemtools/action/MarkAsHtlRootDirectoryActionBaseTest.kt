package com.aemtools.action

import com.aemtools.index.HtlTemplateIndex
import com.aemtools.lang.settings.HtlRootDirectories
import com.aemtools.test.util.mock
import com.intellij.ide.plugins.PluginUtil
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.util.indexing.FileBasedIndex
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.picocontainer.PicoContainer

/**
 * @author Dmytro Primshyts
 */
abstract class MarkAsHtlRootDirectoryActionBaseTest {

  @Mock
  lateinit var actionEvent: AnActionEvent
  @Mock
  lateinit var presentation: Presentation
  @Mock
  lateinit var virtualFile: VirtualFile
  @Mock
  lateinit var mockProject: Project
  @Mock
  lateinit var picoContainer: PicoContainer
  @Mock
  lateinit var rootDirectories: HtlRootDirectories
  @Mock
  lateinit var psiManager: PsiManager
  @Mock
  lateinit var application: Application
  @Mock
  lateinit var projectManager: ProjectManager
  @Mock
  lateinit var fileEditorManager: FileEditorManager
  @Mock
  lateinit var htlTemplateIndexCompanion: HtlTemplateIndex.Companion
  @Mock
  lateinit var pluginUtil: PluginUtil
  @Mock
  lateinit var pluginId: PluginId

  var action: MarkAsHtlRootDirectoryAction = MarkAsHtlRootDirectoryAction()

  @Before
  fun init() {
    actionEvent = mock()
    presentation = mock()
    virtualFile = mock()
    mockProject = mock()
    picoContainer = mock()
    rootDirectories = mock()
    psiManager = mock()
    application = mock()
    projectManager = mock()
    fileEditorManager = mock()
    htlTemplateIndexCompanion = mock()
    pluginId = mock()
    pluginUtil = mock()
    ApplicationManager.setApplication(application, mock())
    `when`(mockProject.getService(PsiManager::class.java))
        .thenReturn(psiManager)
    `when`(application.getService(ProjectManager::class.java))
        .thenReturn(projectManager)
    `when`(projectManager.openProjects)
        .thenReturn(arrayOf(mockProject))
    `when`(mockProject.getComponent(FileEditorManager::class.java))
        .thenReturn(fileEditorManager)
    `when`(fileEditorManager.openFiles)
        .thenReturn(emptyArray())
    `when`(actionEvent.presentation)
        .thenReturn(presentation)
    `when`(presentation.isEnabledAndVisible)
        .thenReturn(true)
    `when`(actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))
        .thenReturn(arrayOf(virtualFile))
    `when`(actionEvent.project)
        .thenReturn(mockProject)
  }
}
