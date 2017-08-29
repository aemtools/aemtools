package com.aemtools.completion.model.psi

import com.aemtools.constant.const

/**
 * Represent String value of Lexeme under user cursor
 * @author Dmytro_Troynikov
 */
data class SelectedString private constructor(
        val value: String,
        val cursorPosition: Int
    ) {

    fun isSelected() : Boolean {
        return cursorPosition != -1
    }

    companion object {

        operator fun invoke(value : String?) : SelectedString? {
            if (value == null) {
                return null
            }

            return SelectedString(value.replace(const.IDEA_STRING_CARET_PLACEHOLDER, ""), value.indexOf(const.IDEA_STRING_CARET_PLACEHOLDER))
        }

    }

}