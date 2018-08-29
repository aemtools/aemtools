package com.aemtools.diagnostics.error.handler

import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.extensions.PluginDescriptor

/**
 * @author DeusBit
 */
interface IssueInfoHolder {
  fun getIssueDetails(event: IdeaLoggingEvent, pluginDescriptor: PluginDescriptor, additionalInfo: String?): String
}
