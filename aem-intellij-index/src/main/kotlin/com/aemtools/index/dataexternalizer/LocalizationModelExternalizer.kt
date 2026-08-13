package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.CodecExternalizer
import com.aemtools.common.index.IndexValueCodec
import com.aemtools.common.index.readString
import com.aemtools.common.index.writeString
import com.aemtools.index.model.LocalizationModel
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
object LocalizationModelExternalizer
  : CodecExternalizer<LocalizationModel>(LocalizationModelCodec)

object LocalizationModelCodec : IndexValueCodec<LocalizationModel> {

  override fun save(out: DataOutput, value: LocalizationModel) {
    out.writeString(value.fileName)
    out.writeString(value.language)
    out.writeString(value.key)
    out.writeString(value.message)
  }

  override fun read(input: DataInput): LocalizationModel {
    return LocalizationModel(
        fileName = input.readString(),
        language = input.readString(),
        key = input.readString(),
        message = input.readString()
    )
  }
}
