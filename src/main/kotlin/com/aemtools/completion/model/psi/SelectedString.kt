package com.aemtools.completion.model.psi

import com.aemtools.constant.const

/**
 * Represent String value of Lexeme under user cursor
 *
 * @author Dmytro Troynikov
 */
data class SelectedString constructor(
        val value: String,
        val cursorPosition: Int
) {

    companion object {

        operator fun invoke(value: String?): SelectedString? {
            if (value == null) {
                return null
            }

            return SelectedString(value.replace(const.IDEA_STRING_CARET_PLACEHOLDER, ""),
                    value.indexOf(const.IDEA_STRING_CARET_PLACEHOLDER))
        }

    }

}
