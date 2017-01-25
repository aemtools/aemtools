package com.aemtools.completion.html.inserthandler

import com.aemtools.completion.blocks.BaseVariantsCheckContributorTest

class HtlTextInsertHandler : BaseVariantsCheckContributorTest("com/aemtools/completion/html/inserthandler") {

    fun testHtlAttributesInsertionExpressionDataSlyTest() = checkAutoCompletion()

    fun testHtlAttributesInsertionExpressionDataSlyList() = checkAutoCompletion()

    fun testHtlAttributesInsertionExpressionDataSlyRepeat() = checkAutoCompletion()

}