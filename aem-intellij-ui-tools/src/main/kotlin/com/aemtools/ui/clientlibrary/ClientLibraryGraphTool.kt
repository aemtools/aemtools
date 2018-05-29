package com.aemtools.ui.clientlibrary

import com.aemtools.index.ClientLibraryIndexFacade
import com.aemtools.index.model.ClientlibraryModel
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import org.graphstream.graph.Edge
import org.graphstream.graph.Graph
import org.graphstream.graph.Node
import org.graphstream.graph.implementations.MultiGraph


class ClientLibraryGraphTool : ToolWindowFactory {

  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    DumbService.getInstance(project).runWhenSmart { initializeWindow(project, toolWindow) }
  }

  private fun initializeWindow(project: Project, myToolWindow: ToolWindow) {
    val allClientLibraryModels = ClientLibraryIndexFacade.getAllClientLibraryModels(project)

    ApplicationManager.getApplication().runWriteAction { buildGraph(allClientLibraryModels, myToolWindow) }
  }

  private fun buildGraph(allClientLibraryModels: List<ClientlibraryModel>, myToolWindow: ToolWindow) {
    if (allClientLibraryModels.isEmpty()) {
      return
    }
    val graph = MultiGraph("Client library graph")

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

    myToolWindow.component.add(viewPanel)
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
