package com.aemtools.index.dataexternalizer

import com.aemtools.common.util.ObjectSerializer
import com.aemtools.common.util.serializeToByteArray
import com.aemtools.index.model.ClientlibraryModel
import com.intellij.util.io.DataExternalizer
import java.io.ByteArrayOutputStream
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro Primshyts
 */
class ClientlibraryExternalizer : DataExternalizer<ClientlibraryModel>{
  companion object {
    val MARKER_BYTES: ByteArray =
        byteArrayOf(Byte.MAX_VALUE, Byte.MAX_VALUE)
  }

  override fun save(out: DataOutput, value: ClientlibraryModel?) {
    if (value != null) {
      val byteArray = value.serializeToByteArray() + MARKER_BYTES
      out.write(byteArray)
    }
  }

  override fun read(input: DataInput): ClientlibraryModel? {
    val baos = ByteArrayOutputStream()

    var nextByte: Byte
    while (true) {
      nextByte = input.readByte()
      baos.write(byteArrayOf(nextByte))

      val currentSequence = baos.toByteArray()
      if (currentSequence.endsWith(MARKER_BYTES)) {
        break
      }
    }
    val result = baos.toByteArray()
    val resultString = result.copyOfRange(0, result.size - 2).toString(charset("ISO-8859-1"))
    return ObjectSerializer.deserialize(resultString)
  }

}


/**
 * Check if current [ByteArray] ends with given byte array sequence.
 * @param other the array to check against
 * @return __true__ if current array ends with given sequence
 */
fun ByteArray.endsWith(other: ByteArray): Boolean {
  if (other.size > this.size || other.isEmpty()) {
    return false
  }

  return (0..other.size - 1).none { this[this.lastIndex - it] != other[other.lastIndex - it] }
}
