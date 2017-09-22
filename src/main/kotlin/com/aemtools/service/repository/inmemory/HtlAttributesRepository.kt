package com.aemtools.service.repository.inmemory

import com.aemtools.completion.model.htl.ContextObject
import com.aemtools.completion.model.htl.HtlAttributeMetaInfo
import com.aemtools.completion.model.htl.HtlOption
import com.aemtools.service.repository.const
import com.aemtools.service.repository.inmemory.util.readJson

/**
 * @author Dmytro_Troynikov
 */
object HtlAttributesRepository {

  /**
   * Htl context value data holder.
   */
  data class HtlContextValue(val name: String,
                             val description: String,
                             val additionalDescription: String?)

  private val attributesData: List<HtlAttributeMetaInfo> = readJson(const.file.SIGHTLY_ATTRIBUTES_DOCUMENTATION)
  private val contextObjects: List<ContextObject> = readJson(const.file.CONTEXT_OBJECTS)
  private val htlOptions: List<HtlOption> = readJson(const.file.HTL_OPTIONS)
  private val htlContextValues: List<HtlContextValue> = readJson(const.file.HTL_CONTEXT_VALUES)

  /**
   * Getter for attributes data.
   *
   * @return list of htl attribute meta info objects
   */
  fun getAttributesData(): List<HtlAttributeMetaInfo> = attributesData

  /**
   * Getter for context objects.
   *
   * @return list of context objects
   */
  fun getContextObjects(): List<ContextObject> = contextObjects

  /**
   * Getter for htl options.
   *
   * @return list of htl option objects
   */
  fun getHtlOptions(): List<HtlOption> = htlOptions

  /**
   * Getter for htl context values.
   *
   * @return list of htl context value objects
   */
  fun getContextValues(): List<HtlContextValue> = htlContextValues

  /**
   * Find context object by name.
   *
   * @param name the name
   * @return context object, *null* if no object found
   * by given name
   */
  fun findContextObject(name: String): ContextObject? =
      contextObjects.find { it.name == name }

}
