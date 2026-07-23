package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.BaseExternalizer
import com.aemtools.index.model.OSGiConfigurationIndexModel
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
object OSGiConfigurationExternalizer : BaseExternalizer<OSGiConfigurationIndexModel>() {

  override fun save(out: DataOutput, value: OSGiConfigurationIndexModel) {
    out.writeString(value.path)
    out.writeNullableStringMap(value.parameters)
  }

  override fun read(input: DataInput): OSGiConfigurationIndexModel {
    return OSGiConfigurationIndexModel(
        path = input.readString(),
        parameters = input.readNullableStringMap()
    )
  }
}
