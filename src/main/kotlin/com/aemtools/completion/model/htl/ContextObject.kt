package com.aemtools.completion.model.htl

import com.aemtools.completion.htl.predefined.PredefinedCompletion

/**
 * @author Dmytro_Troynikov
 */
data class ContextObject(
        val name : String,
        val className : String,
        /**
         * How the predefined values should be processed:
         * "disabled" - predefined suggestions are ignored
         * "merge" - predefined suggestions should be mixed with suggestion derived from PsiClass
         * "override" - no PsiClass suggestions will be used only from 'predefined' list.
         */
        val predefinedMode: String = "disabled",
        val predefined: List<PredefinedCompletion>? = listOf()
)