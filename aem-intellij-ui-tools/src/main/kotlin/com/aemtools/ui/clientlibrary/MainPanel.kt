package com.aemtools.ui.clientlibrary

import javax.swing.BoxLayout
import javax.swing.JPanel


class MainPanel(toolPanel: JPanel, graphPanel: JPanel) : JPanel() {

  init {
    isVisible = true
    layout = BoxLayout(this, BoxLayout.Y_AXIS)
    add(toolPanel)
    add(graphPanel)
  }
}
