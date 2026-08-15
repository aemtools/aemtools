package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.CodecExternalizer
import com.aemtools.common.index.IndexValueCodec
import com.aemtools.common.index.readNullableString
import com.aemtools.common.index.readString
import com.aemtools.common.index.writeNullableString
import com.aemtools.common.index.writeString
import com.aemtools.index.model.AemComponentDefinition
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
object AemComponentDeclarationExternalizer
  : CodecExternalizer<AemComponentDefinition>(AemComponentDefinitionCodec)

object AemComponentDefinitionCodec : IndexValueCodec<AemComponentDefinition> {

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
