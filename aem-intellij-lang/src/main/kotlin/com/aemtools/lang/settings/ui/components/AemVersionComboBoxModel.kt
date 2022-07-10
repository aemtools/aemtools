package com.aemtools.lang.settings.ui.components

import com.intellij.ui.CollectionComboBoxModel

/**
 * @author Kostiantyn Diachenko
 */
class AemVersionComboBoxModel(
    items: List<String>,
    selection: String,
    var changeValueCallback: (version: String) -> Unit
) : CollectionComboBoxModel<String>(items, selection) {
  override fun setSelectedItem(item: Any?) {
    super.setSelectedItem(item)
    changeValueCallback(item as String)
  }
}
