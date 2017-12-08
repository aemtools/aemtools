package com.aemtools.inspection.java.constants

/**
 * @author Dmytro Troynikov
 */
object ConstantClasses {

  val JACKRABIT_JCR_CONSTANTS: String = "org.apache.jackrabbit.JcrConstants"
  val CQ_COMMONS_JCR_JCR_CONSTANTS: String = "com.day.cq.commons.jcr.JcrConstants"
  val DAY_CRX_JCR_CONSTANTS: String = "com.day.crx.JcrConstants"

  val ALL: List<String> = listOf(
      JACKRABIT_JCR_CONSTANTS,
      CQ_COMMONS_JCR_JCR_CONSTANTS,
      DAY_CRX_JCR_CONSTANTS
  )

}
