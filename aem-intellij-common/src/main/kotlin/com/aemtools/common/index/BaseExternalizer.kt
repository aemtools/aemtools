package com.aemtools.common.index

import com.intellij.util.io.DataExternalizer
import java.io.DataInput
import java.io.DataOutput
import java.nio.charset.StandardCharsets

/**
 * Base class for stable IntelliJ index value externalizers.
 *
 * @author Dmytro Primshyts
 */
abstract class BaseExternalizer<T> : DataExternalizer<T> {

  protected fun DataOutput.writeString(value: String) {
    val bytes = value.toByteArray(StandardCharsets.UTF_8)
    writeInt(bytes.size)
    write(bytes)
  }

  protected fun DataInput.readString(): String {
    val bytes = ByteArray(readInt())
    readFully(bytes)
    return String(bytes, StandardCharsets.UTF_8)
  }

  protected fun DataOutput.writeNullableString(value: String?) {
    writeBoolean(value != null)
    if (value != null) {
      writeString(value)
    }
  }

  protected fun DataInput.readNullableString(): String? {
    return if (readBoolean()) {
      readString()
    } else {
      null
    }
  }

  protected fun DataOutput.writeStringList(values: List<String>) {
    writeInt(values.size)
    values.forEach { writeString(it) }
  }

  protected fun DataInput.readStringList(): List<String> {
    return List(readInt()) { readString() }
  }

  protected fun DataOutput.writeNullableStringMap(values: Map<String, String?>) {
    writeInt(values.size)
    values.toSortedMap().forEach { (key, value) ->
      writeString(key)
      writeNullableString(value)
    }
  }

  protected fun DataInput.readNullableStringMap(): Map<String, String?> {
    return List(readInt()) {
      readString() to readNullableString()
    }.toMap()
  }
}
