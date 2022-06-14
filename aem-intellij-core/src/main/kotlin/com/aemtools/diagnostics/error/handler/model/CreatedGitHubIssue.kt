package com.aemtools.diagnostics.error.handler.model

import com.google.gson.annotations.SerializedName

/**
 * @author Kostiantyn Diachenko
 */
data class CreatedGitHubIssue(
    @SerializedName("html_url")
    val htmlUrl: String
)
