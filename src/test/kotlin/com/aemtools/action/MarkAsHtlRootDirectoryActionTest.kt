package com.aemtools.action

import com.aemtools.blocks.util.mock
import com.aemtools.lang.htl.icons.HtlIcons
import com.aemtools.settings.HtlRootDirectories
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.picocontainer.PicoContainer

/**
 * @author Dmytro Troynikov
 */
class MarkAsHtlRootDirectoryActionTest {

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

    @Test
    fun `should disable if file is null`() {
        `when`(actionEvent.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY))
                .thenReturn(emptyArray())

        action.update(actionEvent)

        verify(presentation)
                .isEnabledAndVisible = false
    }

    @Test
    fun `should disable if project is null`() {
        `when`(actionEvent.project)
                .thenReturn(null)

        action.update(actionEvent)

        verify(presentation)
                .isEnabledAndVisible = false
    }

    @Test
    fun `should disable if triggered not on directory`() {
        `when`(virtualFile.isDirectory)
                .thenReturn(false)

        action.update(actionEvent)

        verify(presentation)
                .isEnabledAndVisible = false
    }

    @Test
    fun `should disable if triggered in unknown place`() {
        `when`(actionEvent.place)
                .thenReturn(ActionPlaces.ANT_EXPLORER_POPUP)

        action.update(actionEvent)

        verify(presentation)
                .isEnabledAndVisible = false
    }

    @Test
    fun `should disable if directory is under htl root`() {
        `when`(virtualFile.path)
                .thenReturn("/jcr_root/directory")

        action.update(actionEvent)

        verify(presentation)
                .isEnabledAndVisible = false
    }

    @Test
    fun `should change presentation text for htl root directory`() {
        `when`(virtualFile.path)
                .thenReturn("/marked/directory")
        `when`(picoContainer.getComponentInstance("com.aemtools.settings.HtlRootDirectories"))
                .thenReturn(rootDirectories)
        `when`(rootDirectories.directories)
                .thenReturn(mutableListOf("/marked/directory"))

        action.update(actionEvent)

        verify(presentation)
                .text = "Unmark as HTL Root"
        verify(presentation, never())
                .icon = HtlIcons.HTL_ROOT
    }

    @Test
    fun `should set icon if directory is eligible to mark as htl root`() {
        `when`(virtualFile.path)
                .thenReturn("/not_jcr_root/directory")

        action.update(actionEvent)

        verify(presentation)
                .icon = HtlIcons.HTL_ROOT
    }

    @Test
    fun `should ignore if presentation is disabled`() {
        `when`(presentation.isEnabledAndVisible)
                .thenReturn(false)

        action.actionPerformed(actionEvent)

        verify(rootDirectories, never())
                .addRoot(anyString())

        verify(rootDirectories, never())
                .removeRoot(anyString())
    }

    @Test
    fun `should ignore if project is null`() {
        `when`(actionEvent.project)
                .thenReturn(null)

        action.actionPerformed(actionEvent)

        verify(rootDirectories, never())
                .addRoot(anyString())

        verify(rootDirectories, never())
                .removeRoot(anyString())
    }

}
