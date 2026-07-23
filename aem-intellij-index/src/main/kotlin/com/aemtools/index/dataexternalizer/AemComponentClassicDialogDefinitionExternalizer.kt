package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.BaseExternalizer
import com.aemtools.index.model.dialog.AemComponentClassicDialogDefinition
import com.aemtools.index.model.dialog.parameter.ClassicDialogParameterDeclaration
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
object AemComponentClassicDialogDefinitionExternalizer
  : BaseExternalizer<AemComponentClassicDialogDefinition>() {

  override fun save(out: DataOutput, value: AemComponentClassicDialogDefinition) {
    out.writeString(value.fullPath)
    out.writeString(value.resourceType)
    out.writeInt(value.myParameters.size)
    value.myParameters.forEach {
      out.writeString(it.xtype)
      out.writeString(it.name)
    }
  }

  override fun read(input: DataInput): AemComponentClassicDialogDefinition {
    return AemComponentClassicDialogDefinition(
        fullPath = input.readString(),
        resourceType = input.readString(),
        myParameters = List(input.readInt()) {
          ClassicDialogParameterDeclaration(
              xtype = input.readString(),
              name = input.readString()
          )
        }
    )
  }
}
