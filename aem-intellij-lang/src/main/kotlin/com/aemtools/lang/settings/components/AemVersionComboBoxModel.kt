package com.aemtools.lang.settings.components

import com.intellij.ui.CollectionComboBoxModel

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
