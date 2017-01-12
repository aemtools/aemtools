package com.aemtools.completion.htl.model

import com.intellij.psi.PsiMember

/**
 * The class represents single element from access chain
 *
 * e.g.
 *
 * ${properties.field.inner}
 *
 * In this example the chain will contain three elements:
 * `[properties, field, inner]`
 *
 * @author Dmytro_Troynikov
 */
class PropertyAccessChainUnit(
        /**
         * The name of variable.
         */
        val variableName: String,
        /**
         * The name of original variable or field in from class definition (before name normalization).
         */
        val originalVariableName: String?,
        /**
         * Full qualified class name of underlying element.
         */
        val className: String?,
        /**
         * The resolution result.
         */
        val resolutionResult: ResolutionResult,
        /**
         * Corresponding PsiMember.
         */
        val psiMember: PsiMember?)