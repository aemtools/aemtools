package com.aemtools.index.model.dialog.parameter

import java.io.Serializable

/**
 * Base dialog parameter declaration class.
 *
 * @property name The name of parameter.
 *
 * @author Dmytro Troynikov
 */
abstract class BaseParameterDeclaration(
        open val name: String
) : Serializable {

    /**
     * Normalize dialog parameter name.
     * In case if parameter has "./" prefix it will be removed.
     *
     * @receiver [String]
     * @return normalized parameter name
     */
    protected fun String.normalize() = if (this.startsWith("./")) {
        this.substringAfter("./")
    } else {
        this
    }

}