package com.aemtools.completion.model

import java.io.Serializable
import java.util.ArrayList

/**
 * @author Dmytro_Troynikov
 */
data class WidgetDoc(
    /**
     * The name of package where the class is declared
     */
    var packageName: String,
    /**
     * The name of class
     */
    var className: String,
    /**
     * Full name of class
     */
    var fullClassName: String,
    /**
     * The name of super class
     */
    var extendsName: String,
    /**
     * The _xtype_ of current class (may be _null_)
     */
    var xtype: String?,
    /**
     * Name of clientlib
     */
    var clientlibName: String,
    /**
     * Description of current class (String of *HTML* format)
     */
    var description: String,
    /**
     * List of sublasses (_empty_ if the class has no subclasses)
     */
    var subclasses: List<String> = ArrayList(),
    /**
     * List of members of the class
     * @see WidgetMember
     */
    var members: List<WidgetMember>
) : Serializable {

  /**
   * Get psi member by name.
   *
   * @param name the name
   * @return widget member with given name, *null* if no such member available
   */
  fun getMember(name: String): WidgetMember? {
    return this.members.firstOrNull { (widgetName) ->
      widgetName == name
    }
  }

}
