package com.aemtools.service.repository.inmemory

import com.aemtools.completion.model.WidgetDoc
import com.aemtools.service.repository.WidgetDocRepository
import com.aemtools.service.repository.const
import com.google.common.collect.Lists
import com.google.gson.Gson
import org.apache.sanselan.util.IOUtils

/**
 * [WidgetDocRepository] implementation that uses json files as the storage.
 *
 * @author Dmytro Troynikov
 */
object FileDocRepository : WidgetDocRepository {

  var documents: List<WidgetDoc> = ArrayList()

  var groupedByXtype: MutableMap<String, WidgetDoc> = HashMap()
  var groupedByClass: MutableMap<String, WidgetDoc> = HashMap()
  var xtypes: MutableList<String> = Lists.newArrayList()

  init {
    val bytes: ByteArray? = getDocumentationFromClasspath()

    val jsonString = java.lang.String(bytes as ByteArray, "UTF-8")
    val docs: Array<WidgetDoc> = Gson().fromJson(jsonString.toString(), emptyArray<WidgetDoc>().javaClass)
    documents += docs
    documents.filterNot { it.xtype.isNullOrBlank() }
        .forEach({
          groupedByXtype.put(it.xtype as String, it)
          xtypes.add(it.xtype as String)
        })
    documents.forEach { groupedByClass.put(it.className, it) }
  }

  private fun getDocumentationFromClasspath(): ByteArray? {
    val input = FileDocRepository::class.java.classLoader.getResourceAsStream(
        const.file.WIDGET_DOCUMENTATION)
    return IOUtils.getInputStreamBytes(input)
  }

  override fun findByXType(xtype: String): WidgetDoc? {
    return groupedByXtype[xtype]
  }

  override fun findByClass(className: String): WidgetDoc? {
    return groupedByClass[className]
  }

  override fun findXTypes(query: String?): List<String> {
    if (query == null || query.isEmpty()) {
      return xtypes
    }

    return xtypes.filter { it.startsWith(query) }
  }

}
