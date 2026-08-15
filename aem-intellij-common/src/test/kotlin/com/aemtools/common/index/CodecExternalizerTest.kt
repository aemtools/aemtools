package com.aemtools.common.index

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInput
import java.io.DataInputStream
import java.io.DataOutput
import java.io.DataOutputStream

class CodecExternalizerTest {

  @Test
  fun `codec externalizer delegates value persistence to codec`() {
    val externalizer = CodecExternalizer(StringPairCodec)
    val fixture = StringPair("left", "right")

    assertEquals(fixture, roundTrip(fixture, externalizer))
  }

  private fun <T> roundTrip(value: T, externalizer: CodecExternalizer<T>): T {
    val bytes = ByteArrayOutputStream()
    DataOutputStream(bytes).use { externalizer.save(it, value) }
    return DataInputStream(ByteArrayInputStream(bytes.toByteArray())).use { externalizer.read(it) }
  }

  private data class StringPair(
      val left: String,
      val right: String
  )

  private object StringPairCodec : IndexValueCodec<StringPair> {
    override fun save(out: DataOutput, value: StringPair) {
      out.writeString(value.left)
      out.writeString(value.right)
    }

    override fun read(input: DataInput): StringPair {
      return StringPair(
          left = input.readString(),
          right = input.readString()
      )
    }
  }
}
