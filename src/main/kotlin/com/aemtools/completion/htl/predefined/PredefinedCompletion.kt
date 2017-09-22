package com.aemtools.completion.htl.predefined

import com.aemtools.analysis.htl.callchain.typedescriptor.predefined.PredefinedDescriptionTypeDescriptor
import com.google.gson.annotations.SerializedName
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import javax.swing.Icon

/**
 * @author Dmytro Troynikov
 */
data class PredefinedCompletion(
    @SerializedName(value = "name")
    val completionText: String,
    val type: String? = null,
    @SerializedName(value = "description")
    val documentation: String? = null,
    val typeText: String? = null,
    val icon: Icon? = AllIcons.Nodes.Parameter
) {
  /**
   * Convert current predefined completion into lookup element.
   *
   * @return lookup element
   */
  fun toLookupElement(): LookupElement {
    var result = LookupElementBuilder.create(completionText)
        .withIcon(icon ?: AllIcons.Nodes.Parameter)
    if (type != null) {
      result = result.withTypeText(type)
    }
    return result
  }

  /**
   * Convert current predefined completion into type descriptor.
   *
   * @return instance of [PredefinedDescriptionTypeDescriptor]
   */
  fun asTypeDescriptor(): PredefinedDescriptionTypeDescriptor =
      PredefinedDescriptionTypeDescriptor(this)
}
