package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.BaseExternalizer
import com.aemtools.index.model.AemComponentDefinition
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
object AemComponentDeclarationExternalizer
  : BaseExternalizer<AemComponentDefinition>() {

  override fun save(out: DataOutput, value: AemComponentDefinition) {
    out.writeNullableString(value.title)
    out.writeNullableString(value.description)
    out.writeString(value.fullPath)
    out.writeNullableString(value.resourceSuperType)
    out.writeNullableString(value.componentGroup)
    out.writeBoolean(value.isContainer)
    out.writeNullableString(value.cqIcon)
  }

  override fun read(input: DataInput): AemComponentDefinition {
    return AemComponentDefinition(
        title = input.readNullableString(),
        description = input.readNullableString(),
        fullPath = input.readString(),
        resourceSuperType = input.readNullableString(),
        componentGroup = input.readNullableString(),
        isContainer = input.readBoolean(),
        cqIcon = input.readNullableString()
    )
  }
}
