package com.aemtools.test.typedescriptor

import com.aemtools.analysis.htl.callchain.typedescriptor.base.TypeDescriptor
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder

/**
 * @author Dmytro Troynikov
 */
class MockTypeDescriptor(
    val name: String,
    val nameInParent: String?,
    val array: Boolean,
    val map: Boolean,
    val iterable: Boolean,
    val subtypes: List<MockTypeDescriptor>
) : TypeDescriptor {
  override fun myVariants(): List<LookupElement> = subtypes.map {
    LookupElementBuilder.create(it.nameInParent ?: it.name)
  }

  override fun subtype(identifier: String): TypeDescriptor {
    return subtypes.find {
      it.nameInParent == identifier
    } ?: TypeDescriptor.empty()
  }

  override fun isArray(): Boolean = array
  override fun isIterable(): Boolean = iterable
  override fun isMap(): Boolean = map
}

fun typeDescriptor(typeDescriptorDsl: TypeDescriptorDsl.() -> Unit)
    : TypeDescriptor = TypeDescriptorDsl().run {
  typeDescriptorDsl()
  build()
}

class TypeDescriptorDsl {
  var array: Boolean = false
  var map: Boolean = false
  var iterable: Boolean = false
  var name: String = ""
  var nameInParent: String? = null

  private val subtypes: ArrayList<TypeDescriptorDsl.() -> Unit> = ArrayList()
  fun subtype(subtypes: TypeDescriptorDsl.() -> Unit) {
    this.subtypes.add(subtypes)
  }

  fun build(): MockTypeDescriptor {
    val subtypes = this.subtypes.map {
      TypeDescriptorDsl().apply { it() }
    }.map {
      MockTypeDescriptor(
          it.name,
          it.nameInParent,
          it.array,
          it.map,
          it.iterable,
          it.subtypes.map {
            TypeDescriptorDsl().apply { it() }
                .build()
          }
      )
    }
    return MockTypeDescriptor(
        name,
        nameInParent,
        array,
        map,
        iterable,
        subtypes
    )
  }
}
