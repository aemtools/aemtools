package com.aemtools.diagnostics.error.handler.provider

import com.aemtools.diagnostics.error.handler.model.GitHubIssue
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.extensions.PluginDescriptor

/**
 * @author DeusBit
 */
interface IssueInfoFactory {
  /**
   * Create detail information about an issue.
   */
  fun create(event: IdeaLoggingEvent,
             pluginDescriptor: PluginDescriptor,
             additionalInfo: String?): GitHubIssue
}
