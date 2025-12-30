package com.aemtools.completion.model.psi

import com.aemtools.common.constant.Const

/**
 * Represent String value of Lexeme under user cursor
 *
 * @author Dmytro Primshyts
 */
data class SelectedString constructor(
    val value: String,
    val cursorPosition: Int
) {

    companion object {

        /**
         * Builder method for [SelectedString].
         *
         * @param value incoming string
         * @return selected string instance, *null* for null input
         */
        fun create(value: String?): SelectedString? {
            if (value == null) {
                return null
            }

            return SelectedString(
                value.replace(Const.IDEA_STRING_CARET_PLACEHOLDER, ""),
                value.indexOf(Const.IDEA_STRING_CARET_PLACEHOLDER)
            )
        }
    }
}
