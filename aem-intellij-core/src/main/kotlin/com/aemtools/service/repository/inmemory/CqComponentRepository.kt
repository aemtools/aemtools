package com.aemtools.service.repository.inmemory

import com.aemtools.service.repository.const.file.CQ_COMPONENT_VALUES
import com.aemtools.service.repository.inmemory.util.readJson

object CqComponentRepository {
  data class CqComponentProperty(
      val name: String,
      val type: String,
      val description: String
  )

  private val cqComponentProperties: List<CqComponentProperty> = readJson(CQ_COMPONENT_VALUES)

  fun getNodeProperties() = cqComponentProperties

}
