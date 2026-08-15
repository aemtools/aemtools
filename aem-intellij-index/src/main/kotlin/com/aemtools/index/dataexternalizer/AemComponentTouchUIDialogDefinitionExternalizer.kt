package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.CodecExternalizer
import com.aemtools.common.index.IndexValueCodec
import com.aemtools.common.index.readString
import com.aemtools.common.index.writeString
import com.aemtools.index.model.dialog.AemComponentTouchUIDialogDefinition
import com.aemtools.index.model.dialog.parameter.TouchUIDialogParameterDeclaration
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
object AemComponentTouchUIDialogDefinitionExternalizer
  : CodecExternalizer<AemComponentTouchUIDialogDefinition>(AemComponentTouchUIDialogDefinitionCodec)

object AemComponentTouchUIDialogDefinitionCodec
  : IndexValueCodec<AemComponentTouchUIDialogDefinition> {

  override fun save(out: DataOutput, value: AemComponentTouchUIDialogDefinition) {
    out.writeString(value.fullPath)
    out.writeString(value.resourceType)
    out.writeInt(value.myParameters.size)
    value.myParameters.forEach {
      out.writeString(it.slingResourceType)
      out.writeString(it.name)
    }
  }

  override fun read(input: DataInput): AemComponentTouchUIDialogDefinition {
    return AemComponentTouchUIDialogDefinition(
        fullPath = input.readString(),
        resourceType = input.readString(),
        myParameters = List(input.readInt()) {
          TouchUIDialogParameterDeclaration(
              slingResourceType = input.readString(),
              name = input.readString()
          )
        }
    )
  }
}
