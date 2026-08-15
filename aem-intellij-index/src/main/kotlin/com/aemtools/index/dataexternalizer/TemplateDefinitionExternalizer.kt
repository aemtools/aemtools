package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.CodecExternalizer
import com.aemtools.common.index.IndexValueCodec
import com.aemtools.common.index.readNullableString
import com.aemtools.common.index.readString
import com.aemtools.common.index.readStringList
import com.aemtools.common.index.writeNullableString
import com.aemtools.common.index.writeString
import com.aemtools.common.index.writeStringList
import com.aemtools.index.model.TemplateDefinition
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
object TemplateDefinitionExternalizer : CodecExternalizer<TemplateDefinition>(TemplateDefinitionCodec)

object TemplateDefinitionCodec : IndexValueCodec<TemplateDefinition> {

  override fun save(out: DataOutput, value: TemplateDefinition) {
    out.writeNullableString(value.fullName)
    out.writeString(value.name)
    out.writeStringList(value.parameters)
  }

  override fun read(input: DataInput): TemplateDefinition {
    return TemplateDefinition(
        fullName = input.readNullableString(),
        name = input.readString(),
        parameters = input.readStringList()
    )
  }
}
