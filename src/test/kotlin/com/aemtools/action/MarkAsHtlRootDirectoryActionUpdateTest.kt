package com.aemtools.action

import com.aemtools.lang.htl.icons.HtlIcons
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

/**
 * @author Dmytro Troynikov
 */
class MarkAsHtlRootDirectoryActionUpdateTest
  : MarkAsHtlRootDirectoryActionBaseTest() {

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

}
