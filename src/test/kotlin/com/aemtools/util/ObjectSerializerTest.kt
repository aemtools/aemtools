package com.aemtools.util

import com.aemtools.index.model.TemplateDefinition
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Dmytro_Troynikov
 */
class ObjectSerializerTest {

  val fixture = TemplateDefinition("path", "name", listOf("param1", "param2"))
  @Test
  fun serializeAndDeserializeShouldReturnSameObject() {
    val serialized = fixture.serialize()
    val deserialized = ObjectSerializer.deserialize<TemplateDefinition>(serialized)

    assertEquals(fixture, deserialized)
  }

  @Test
  fun serializeAndDeserializeShouldReturnSameObject2() {
    val serialized = fixture.serializeToByteArray()
    val deserialized = ObjectSerializer.deserialize<TemplateDefinition>(serialized)

    assertEquals(fixture, deserialized)
  }

}
