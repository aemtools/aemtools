package com.aemtools.completion.htl.model

/**
 * Types of `data-sly-use` declaration.
 * @author Dmytro_Troynikov
 */
enum class UseType {
    /**
     * Either `Use` implementor or `Sling` model.
     */
    BEAN,
    /**
     * Javascript file.
     */
    JAVASCRIPT,
    /**
     * The HTL file.
     */
    HTL
}