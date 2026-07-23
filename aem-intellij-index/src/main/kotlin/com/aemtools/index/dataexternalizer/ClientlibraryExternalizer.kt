package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.BaseExternalizer
import com.aemtools.index.model.ClientlibraryModel
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
class ClientlibraryExternalizer : BaseExternalizer<ClientlibraryModel>() {

  override fun save(out: DataOutput, value: ClientlibraryModel) {
    out.writeStringList(value.channels)
    out.writeStringList(value.categories)
    out.writeStringList(value.dependencies)
    out.writeStringList(value.embed)
    out.writeString(value.filePath)
  }

  override fun read(input: DataInput): ClientlibraryModel {
    return ClientlibraryModel(
        channels = input.readStringList(),
        categories = input.readStringList(),
        dependencies = input.readStringList(),
        embed = input.readStringList(),
        filePath = input.readString()
    )
  }
}
