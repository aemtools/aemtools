package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.BaseExternalizer
import com.aemtools.index.model.TemplateDefinition
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
object TemplateDefinitionExternalizer : BaseExternalizer<TemplateDefinition>() {

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
