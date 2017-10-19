package com.aemtools.blocks

import com.aemtools.blocks.fixture.TestClassesMixin
import com.aemtools.blocks.fixture.UberJarFixtureMixin
import com.aemtools.lang.htl.lexer.HtlTestCase
import com.intellij.codeInsight.completion.LightFixtureCompletionTestCase
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.ide.startup.impl.StartupManagerImpl
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
import org.assertj.core.api.Assertions.assertThat
import java.io.File

/**
 * @author Dmytro Troynikov.
 */
abstract class BaseVariantsCheckContributorTest(val dataPath: String)
  : LightFixtureCompletionTestCase(),
    HtlTestCase,
    TestClassesMixin,
    UberJarFixtureMixin {

  companion object {
    /**
     * Common [Object] variants
     */
    val OBJECT_VARIANTS = listOf("class", "hashCode", "toString")

    val DEFAULT_CONTEXT_OBJECTS = listOf(
        "component",
        "componentContext",
        "currentDesign",
        "currentNode",
        "currentPage",
        "currentSession",
        "currentStyle",
        "properties",
        "pageProperties",
        "inheritedPageProperties",
        "designer",
        "editContext",
        "log",
        "out",
        "pageManager",
        "reader",
        "request",
        "resource",
        "resourceDesign",
        "resourcePage",
        "response",
        "sling",
        "slyWcmHelper",
        "wcmmode",
        "xssAPI"
    ).sorted()
    /**
     * Values available as the 'context' option value (${@ context='<caret>'})
     */
    val CONTEXT_VALUES = listOf("text",
        "html", "attribute", "uri", "number", "attributeName",
        "elementName", "scriptToken", "scriptString", "scriptComment",
        "styleToken", "styleString", "styleComment", "unsafe")

    /**
     * Values available as options (${@ <caret>}
     */
    val CONTEXT_PARAMETERS = listOf("join",
        "i18n", "context", "format", "locale",
        "timezone", "scheme", "domain",
        "path", "prependPath", "appendPath",
        "selectors", "addSelectors", "removeSelectors",
        "extension", "suffix", "prependSuffix", "appendSuffix",
        "query", "addQuery", "removeQuery", "fragment")
    val DATA_SLY_SUITABLE_CLASSES = listOf(
        "com.aemtools.completion.htl.fixtures.classes.CustomSlingModel",
        "com.aemtools.completion.htl.fixtures.classes.IterableModel",
        "com.aemtools.completion.htl.fixtures.classes.CustomUseClass",
        "com.aemtools.completion.htl.fixtures.classes.CustomWcmUseClass"
    )

    val DEFAULT_LIST_VARIABLES = listOf("item", "itemList")
    val OVERRIDEN_LIST_VARIABLES = listOf("overriden", "overridenList")
    val LIST_VARIABLE_FIELDS = listOf("index", "count", "first",
        "middle", "last", "odd", "even")

    val PAGE_FIELDS = listOf(
        "path", "pageManager", "contentResource",
        "listChildren", "depth", "parent",
        "properties", "name", "title", "pageTitle",
        "navigationTitle", "hideInNav", "hasContent",
        "valid", "timeUntilValid", "onTime", "offTime",
        "lastModifiedBy", "lastModified", "vanityUrl",
        "tags", "locked", "lockOwner", "canUnlock",
        "description", "template"
    ) + OBJECT_VARIANTS

    val CUSTOM_MODEL_FIELDS = listOf(
        "booleanField",
        "isBooleanField",
        "publicBoolean",
        "publicString",
        "publicStringArray",
        "publicStringList",
        "publicStringMap",
        "stringArray",
        "stringField",
        "stringList",
        "stringMap",
        "modelList",
        "class",
        "hashCode",
        "toString")

  }

  override fun getTestDataPath(): String = "${HtlTestCase.testResourcesPath}/$dataPath"

  protected val fileName: String get() = "$testName.html"

  private val testName: String get() = getTestName(true)

  protected fun assertVariantsAreEmpty() = assertVariants(listOf())

  /**
   * In that assertion the result may contain variants not
   * listed in given variants list
   * @param variants the list of variants which should be present in completion resu.t
   */
  protected fun assertVariantsPresent(variants: List<String>) {
    assertThat(triggerCompletion().map { it.lookupString })
        .contains(*variants.toTypedArray())
  }

  /**
   * Will fail in case if result contains at least one of given variants.
   */
  protected fun assertVariantsAbsent(variants: List<String>) {
    assertThat(triggerCompletion().map { it.lookupString })
        .doesNotContain(*variants.toTypedArray())
  }

  /**
   * Check if completion result contains only given variants.
   * @param variants the list of variants which should be present in completion result
   */
  protected fun assertVariants(variants: List<String>) {
    val result = triggerCompletion()

    assertThat(result.map { it.lookupString })
        .containsOnly(*variants.toTypedArray())
  }

  private fun triggerCompletion(): List<LookupElement> {
    myFixture.configureByFile("${getTestName(true)}.html")
    return myFixture.completeBasic().toList()
  }

  /**
   * Runs basic completion in caret position in fileBefore. Implies that there is only one
   * completion variant and it was inserted automatically, and checks the result file text with fileAfter.
   *
   * User have to create file with input data and name have which corresponds to test method name (without test at start) and
   * Output result file must have name same input file name and with end "Result"
   *
   * For example:
   *  Test name: testHelloWorld;
   *  Input file: helloWorld.html;
   *  Output file: helloWorldResult.html;
   */
  protected fun checkAutoCompletion() {
    myFixture.testCompletion("${getTestName(true)}.html", "${getTestName(true)}Result.html")
  }

  override fun setUp() {
    super.setUp()
    VfsRootAccess.allowRootAccess(File("src/test").absolutePath)
    myFixture.addUberJar()
    myFixture.addClasses()
    (StartupManager.getInstance(project) as StartupManagerImpl).runPostStartupActivities()
  }

  override fun tearDown() {
    VfsRootAccess.disallowRootAccess(File("src/test").absolutePath)

    super.tearDown()
  }

  val fixture: JavaCodeInsightTestFixture
    get() = myFixture

  override fun getProjectDescriptor(): LightProjectDescriptor
      = LightCodeInsightFixtureTestCase.JAVA_8

}
