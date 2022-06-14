package com.aemtools.diagnostics.error.handler.model

/**
 * @author Kostiantyn Diachenko
 */
data class GitHubIssue(
    val title: String,
    val body: String,
    val labels: List<String> = listOf()
)
