package com.aemtools.completion.htl.lists

import com.aemtools.blocks.BaseVariantsWithJdkContributorTest

/**
 * @author Dmytro_Troynikov
 */
class HtlListCommonCompletionTest
  : BaseVariantsWithJdkContributorTest("com/aemtools/completion/htl/fixtures/noafter/list-common") {

  fun testLcResolveArray() = assertVariants(INNER_MODEL_FIELDS)
  fun testLcResolveArrayDeclaredInAttribute() = assertVariants(INNER_MODEL_FIELDS)
  fun testLcResolveArrayRecursive() = assertVariants(INNER_MODEL_FIELDS)

  fun testLcResolveList() = assertVariants(INNER_MODEL_FIELDS)
  fun testLcResolveListDeclaredInAttribute() = assertVariants(INNER_MODEL_FIELDS)
  fun testLcResolveListRecursive() = assertVariants(INNER_MODEL_FIELDS)

  fun testLcResolveMap() = assertVariants(INNER_MODEL_FIELDS)
  fun testLcResolveMapDeclaredInAttribute() = assertVariants(INNER_MODEL_FIELDS)
  fun testLcResolveMapRecursive() = assertVariants(INNER_MODEL_FIELDS)

  fun testLcResolveMapValue() = assertVariants(MAP_VALUE_FIELDS)

  fun testLcTwoLists() = assertVariants(PAGE_FIELDS)

  fun testLcTwoListsNested() = assertVariants(PAGE_FIELDS)

  companion object {
    val INNER_MODEL_FIELDS = listOf(
        "modelFieldString",
        "modelFieldBoolean",
        "methodModelString",
        "methodModelBoolean",
        "class", "hashCode", "toString"
    )

    val MAP_VALUE_FIELDS = listOf(
        "mapModelField",
        "mapModelBooleanField",
        "methodMapModelString",
        "methodMapModelBoolean",
        "class", "hashCode", "toString"
    )
  }

}
