package com.aemtools.index.dataexternalizer

import com.aemtools.common.index.CodecExternalizer
import com.aemtools.common.index.IndexValueCodec
import com.aemtools.common.index.readString
import com.aemtools.common.index.readStringList
import com.aemtools.common.index.writeString
import com.aemtools.common.index.writeStringList
import com.aemtools.index.model.ClientlibraryModel
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
class ClientlibraryExternalizer : CodecExternalizer<ClientlibraryModel>(ClientlibraryCodec)

object ClientlibraryCodec : IndexValueCodec<ClientlibraryModel> {

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
