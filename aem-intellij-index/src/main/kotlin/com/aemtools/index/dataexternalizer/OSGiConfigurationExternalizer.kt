package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.CodecExternalizer
import com.aemtools.common.index.IndexValueCodec
import com.aemtools.common.index.readNullableStringMap
import com.aemtools.common.index.readString
import com.aemtools.common.index.writeNullableStringMap
import com.aemtools.common.index.writeString
import com.aemtools.index.model.OSGiConfigurationIndexModel
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
object OSGiConfigurationExternalizer : CodecExternalizer<OSGiConfigurationIndexModel>(
    OSGiConfigurationCodec
)

object OSGiConfigurationCodec : IndexValueCodec<OSGiConfigurationIndexModel> {

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
