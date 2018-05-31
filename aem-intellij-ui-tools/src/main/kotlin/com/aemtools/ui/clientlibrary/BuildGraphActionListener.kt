package com.aemtools.ui.clientlibrary

import com.aemtools.index.ClientLibraryIndexFacade
import com.aemtools.index.model.ClientlibraryModel
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import org.graphstream.graph.Edge
import org.graphstream.graph.Graph
import org.graphstream.graph.Node
import org.graphstream.graph.implementations.MultiGraph
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JComponent


class BuildGraphActionListener(
    val project: Project,
    val container: JComponent) : ActionListener {
  private val taskIsStarted = AtomicBoolean(false)
  private val graph = MultiGraph("Client library graph")

  override fun actionPerformed(e: ActionEvent?) {
    if (!taskIsStarted.get()) {
      taskIsStarted.set(true)
      DumbService.getInstance(project).runWhenSmart { initializeWindow(project, container) }
    }
  }

  private fun initializeWindow(project: Project, container: JComponent) {
    val allClientLibraryModels = ClientLibraryIndexFacade.getAllClientLibraryModels(project)


//    ProgressManager.getInstance().run(object : Task.Backgroundable(project, "", false) {
//      override fun run(indicator: ProgressIndicator) {
//        indicator.setText("Building graph")
//        indicator.setFraction(0.5)
//        buildGraph(allClientLibraryModels, container)
//      }
//    })
    ApplicationManager.getApplication().runWriteAction { buildGraph(allClientLibraryModels, container) }
  }

  private fun buildGraph(allClientLibraryModels: List<ClientlibraryModel>, container: JComponent) {
    if (allClientLibraryModels.isEmpty()) {
      return
    }
    graph.clear()

    for (cl in allClientLibraryModels) {
      for (cat in cl.categories) {
        val catNode = addNode(graph, cat)
        for (dep in cl.dependencies) {
          val depNode = addNode(graph, dep)
          val edge = addEdge(graph, "${catNode.id}-${depNode.id}-dependencies", catNode, depNode)
          edge.addAttribute("ui.label", "dependencies")
        }
        for (emb in cl.embed) {
          val embNode = addNode(graph, emb)

          val edge = addEdge(graph, "${catNode.id}-${embNode.id}-embed", catNode, embNode)
          edge.addAttribute("ui.label", "embed")
        }
      }
    }

    val viewer = graph.display()
    val viewPanel = viewer.addDefaultView(false)
    viewPanel.isVisible = true

    container.add(viewPanel)

    Notifications.Bus
        .notify(Notification("Graph building status", "Success", "Graph was built successfully", NotificationType.INFORMATION), project)
    taskIsStarted.set(false)
  }

  private fun addNode(graph: Graph, category: String): Node {
    var node = graph.getNode(category) as Node?
    if (node == null) {
      node = graph.addNode(category) as Node
    }
    node.addAttribute("ui.label", category)
    return node
  }

  private fun addEdge(graph: Graph, id: String, first: Node, second: Node): Edge {
    var edge = graph.getEdge(id) as Edge?
    if (edge == null) {
      edge = graph.addEdge(id, first, second, true) as Edge
    }
    return edge
  }
}
