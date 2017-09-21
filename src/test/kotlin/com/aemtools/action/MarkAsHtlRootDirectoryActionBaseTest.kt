package com.aemtools.action

import com.aemtools.blocks.util.mock
import com.aemtools.settings.HtlRootDirectories
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.application.Application
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.picocontainer.PicoContainer

/**
 * @author Dmytro Troynikov
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
    `when`(mockProject.getComponent(PsiManager::class.java))
        .thenReturn(psiManager)
    `when`(application.getComponent(ProjectManager::class.java))
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
    `when`(actionEvent.place)
        .thenReturn(ActionPlaces.PROJECT_VIEW_POPUP)
    `when`(actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))
        .thenReturn(arrayOf(virtualFile))
    `when`(actionEvent.project)
        .thenReturn(mockProject)
    `when`(mockProject.picoContainer)
        .thenReturn(picoContainer)
    `when`(picoContainer.getComponentInstance("HtlRootDirectories"))
        .thenReturn(null)
    `when`(virtualFile.isDirectory)
        .thenReturn(true)
    `when`(virtualFile.path)
        .thenReturn("/jcr_root/directory")
  }
}
