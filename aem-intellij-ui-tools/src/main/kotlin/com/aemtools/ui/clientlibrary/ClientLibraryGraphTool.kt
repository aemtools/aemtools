package com.aemtools.ui.clientlibrary

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*


class ClientLibraryGraphTool : ToolWindowFactory, DumbAware {

  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

    val graphPanel = initGraphPanel()

    val toolPanel = initToolPanel(graphPanel, project)

    val mainPanel = MainPanel(toolPanel, graphPanel)

    toolWindow.component.add(mainPanel)
  }

  private fun initToolPanel(graphPanel: JPanel, project: Project): JPanel {
    val toolPanel = JPanel()
    toolPanel.isVisible = true
    toolPanel.maximumSize = Dimension(10000, 40)
    toolPanel.layout = BoxLayout(toolPanel, BoxLayout.X_AXIS)
    toolPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

    val buildGraphBtn = JButton("Build graph")
    buildGraphBtn.isVisible = true
    buildGraphBtn.addActionListener(BuildGraphActionListener(project, graphPanel))
    toolPanel.add(buildGraphBtn)
    return toolPanel
  }

  private fun initGraphPanel(): JPanel {
    val graphPanel = JPanel()
    graphPanel.layout = BorderLayout()
    graphPanel.preferredSize = Dimension(200, 200)
    graphPanel.minimumSize = Dimension(25, 25)
    graphPanel.maximumSize = Dimension(10000, 10000)
    graphPanel.isVisible = true
    return graphPanel
  }
}
