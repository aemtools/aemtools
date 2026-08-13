@file:JvmName("BaseExternalizerKt")

package com.aemtools.common.index

import com.intellij.util.io.DataExternalizer
import java.io.DataInput
import java.io.DataOutput
import java.nio.charset.StandardCharsets

/**
 * Stable binary codec for IntelliJ index values.
 *
 * @author Dmytro Primshyts
 */
interface IndexValueCodec<T> {

  fun save(out: DataOutput, value: T)

  fun read(input: DataInput): T
}

/**
 * DataExternalizer adapter for reusable index value codecs.
 */
open class CodecExternalizer<T>(
    private val codec: IndexValueCodec<T>
) : DataExternalizer<T> {

  override fun save(out: DataOutput, value: T) = codec.save(out, value)

  override fun read(input: DataInput): T = codec.read(input)
}

fun DataOutput.writeString(value: String) {
  val bytes = value.toByteArray(StandardCharsets.UTF_8)
  writeInt(bytes.size)
  write(bytes)
}

fun DataInput.readString(): String {
  val bytes = ByteArray(readInt())
  readFully(bytes)
  return String(bytes, StandardCharsets.UTF_8)
}

fun DataOutput.writeNullableString(value: String?) {
  writeBoolean(value != null)
  if (value != null) {
    writeString(value)
  }
}

fun DataInput.readNullableString(): String? {
  return if (readBoolean()) {
    readString()
  } else {
    null
  }
}

fun DataOutput.writeStringList(values: List<String>) {
  writeInt(values.size)
  values.forEach { writeString(it) }
}

fun DataInput.readStringList(): List<String> {
  return List(readInt()) { readString() }
}

fun DataOutput.writeNullableStringMap(values: Map<String, String?>) {
  writeInt(values.size)
  values.toSortedMap().forEach { (key, value) ->
    writeString(key)
    writeNullableString(value)
  }
}

fun DataInput.readNullableStringMap(): Map<String, String?> {
  return List(readInt()) {
    readString() to readNullableString()
  }.toMap()
}
