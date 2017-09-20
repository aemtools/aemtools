package com.aemtools.completion.htl.common

import com.aemtools.blocks.BaseVariantsWithJdkContributorTest

/**
 * Tests for proper collections variables resolution.
 * @author Dmytro_Troynikov
 */
class CollectionsTest : BaseVariantsWithJdkContributorTest("com/aemtools/completion/htl/fixtures/noafter/collections") {

  fun testCollectionsResolveArrayWithLiteral() = assertVariants(MY_MODEL_VARIANTS)
  fun testCollectionsResolveArrayWithVariable() = assertVariants(MY_MODEL_VARIANTS)

  fun testCollectionsResolveListWithLiteral() = assertVariants(MY_MODEL_VARIANTS)
  fun testCollectionsResolveListWithVariable() = assertVariants(MY_MODEL_VARIANTS)

  fun testCollectionsResolveMapWithLiteral() = assertVariants(MY_MAP_MODEL_VARIANTS)
  fun testCollectionsResolveMapWithVariable() = assertVariants(MY_MAP_MODEL_VARIANTS)

  companion object {
    val MY_MODEL_VARIANTS = listOf("modelFieldString", "modelFieldBoolean",
        "methodModelString", "methodModelBoolean") + OBJECT_VARIANTS
    val MY_MAP_MODEL_VARIANTS = listOf("mapModelField", "mapModelBooleanField",
        "methodMapModelString", "methodMapModelBoolean") + OBJECT_VARIANTS
  }

}
