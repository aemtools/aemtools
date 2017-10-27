package com.aemtools.index.dataexternalizer

import com.aemtools.index.dataexternalizer.BaseExternalizer.Companion.MARKER_BYTES
import com.aemtools.index.model.TemplateDefinition
import com.aemtools.util.serializeToByteArray
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.only
import org.mockito.Mockito.verify
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro_Troynikov
 */
class TemplateDefinitionExternalizerTest {

  val fixture = TemplateDefinition("path", "name", listOf("item1, item2"))

  @Test
  fun saveShouldAddMarkerBytes() {
    val out = mock(DataOutput::class.java)

    TemplateDefinitionExternalizer.save(out, fixture)

    verify(out).write(fixture.serializeToByteArray() + MARKER_BYTES)
  }

  @Test
  fun saveAndRead() {
    val out = mock(DataOutput::class.java)
    val input = mock(DataInput::class.java)

    val outputCaptor = ArgumentCaptor.forClass<ByteArray, ByteArray>(ByteArray::class.java)

    TemplateDefinitionExternalizer.save(out, fixture)

    verify(out, only()).write(outputCaptor.capture())

    val bytes = outputCaptor.value

    Assert.assertNotNull(bytes)

    `when`(input.readByte()).then(object : Answer<Byte> {
      val bytes = outputCaptor.value
      var pointer = 0
      override fun answer(invocation: InvocationOnMock?): Byte {
        return bytes[pointer++]
      }
    })

    val result = TemplateDefinitionExternalizer.read(input)

    Assert.assertEquals(fixture, result)
  }

}
