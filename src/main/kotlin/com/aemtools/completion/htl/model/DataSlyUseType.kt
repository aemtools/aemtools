package com.aemtools.completion.htl.model

/**
 * The type of `data-sly-use`
 *
 * ```
 *     data-sly-use.bean="com.package.BeanName" -> JAVA
 *     data-sly-use.bean="logic.js" -> JAVASCRIPT
 *     data-sly-use.template="template.html" -> HTL_TEMPLATE
 *     data-sly-use="" -> UNKNOWN
 * ```
 *
 * @author Dmytro_Troynikov
 */
enum class DataSlyUseType {
    JAVA,
    JAVASCRIPT,
    HTL_TEMPLATE,
    UNKNOWN
}