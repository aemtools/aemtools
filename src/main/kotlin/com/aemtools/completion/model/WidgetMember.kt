package com.aemtools.completion.model

import java.io.Serializable

/**
 * @author Dmytro_Troynikov
 */
data class WidgetMember(
    /**
     * Name of the member
     */
    var name: String,
    /**
     * Type of member (e.g. _Boolean_)
     */
    var type: String,
    var memberType: WidgetMember.MemberType,
    var description: String?,
    var definedBy: String
) : Serializable {

  /**
   * Enumeration for member types.
   */
  enum class MemberType {
    OPTION,
    PUBLIC_PROPERTY,
    PUBLIC_METHOD
  }
}

