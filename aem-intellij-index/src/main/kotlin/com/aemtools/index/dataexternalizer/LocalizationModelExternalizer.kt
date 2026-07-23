package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.BaseExternalizer
import com.aemtools.index.model.LocalizationModel
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
object LocalizationModelExternalizer
  : BaseExternalizer<LocalizationModel>() {

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
