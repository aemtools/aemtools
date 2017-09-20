package com.aemtools.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * @author Dmytro_Troynikov
 */
object ObjectSerializer {

  /**
   * Serialize given object into [String] using [ObjectOutputStream].
   * @param obj object to serialize
   * @see ObjectInputStream
   * @return the serialization result, empty string for _null_ input
   */
  fun <T : Serializable> serialize(obj: T?): String {
    if (obj == null) {
      return ""
    }

    return obj.serializeToByteArray().toString(charset("ISO-8859-1"))
  }

  /**
   *  Serialize given [Serializable] object into [ByteArray] using [ObjectOutputStream].
   *  @param obj object to serialize
   *  @return byte array
   */
  fun <T : Serializable> serializeToByteArray(obj: T?): ByteArray {
    if (obj == null) {
      return ByteArray(0)
    }

    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(obj)
    oos.close()

    return baos.toByteArray()
  }

  /**
   * Deserialize given [String] using [ObjectInputStream].
   * @param string the string to deserialize
   * @return deserialized object, _null_ in case of error.
   */
  @Suppress("UNCHECKED_CAST")
  fun <T : Serializable> deserialize(string: String): T? =
      deserialize(string.toByteArray(charset("ISO-8859-1")))

  /**
   * Deserialize given [ByteArray] using [ObjectInputStream].
   * @param byteArray the array to deserialize
   * @return deserialized object, _null_ in case of error.
   */
  fun <T : Serializable> deserialize(byteArray: ByteArray): T? {
    if (byteArray.isEmpty()) {
      return null
    }

    var bais = ByteArrayInputStream(byteArray)
    var ois = ObjectInputStream(bais)

    return ois.readObject() as T
  }

}

/**
 * Serialize current [Serializable].
 * The shortcut for [ObjectSerializer.serialize] method.
 * @receiver [Serializable]
 * @see ObjectSerializer.serialize
 */
fun Serializable.serialize(): String = ObjectSerializer.serialize(this)

/**
 * Serialize current [Serializable] into [ByteArray].
 * The shortcut for [ObjectSerializer.serializeToByteArray] method.
 * @receiver [Serializable]
 * @see ObjectSerializer.serializeToByteArray
 */
fun Serializable.serializeToByteArray(): ByteArray = ObjectSerializer.serializeToByteArray(this)
