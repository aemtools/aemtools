package com.aemtools.inspection.java.constants

/**
 * Constant descriptor model.
 *
 * @property containerClass the fqn of class-container of a constant
 * @property name name of constant in container class
 * @property value value of constant
 *
 * @author Dmytro Troynikov
 */
data class ConstantDescriptor(
    val containerClass: String,
    val name: String,
    val value: String
)
