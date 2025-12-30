package com.aemtools.diagnostics.error.handler

/**
 * @author Kostiantyn Diachenko
 */
data class ErrorReportingConfig(
    val userId: String = "aem-tools-issue-tracker",
    val repoName: String = "aem-tools-issues",
    val tokenFile: String = "reporting/token",
)
