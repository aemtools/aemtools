package com.aemtools.documentation.html

import com.aemtools.common.constant.const
import com.aemtools.lang.settings.model.HtlVersion

/**
 * Tests for HTL attributes documentation for HTL v 1.4.
 *
 * @author Kostiantyn Diachenko
 */
class HtlAttributesDocumentationProviderV14Test: HtlAttributesDocumentationProviderTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_4)
  }

  fun testDataSlyUseDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_USE)
    documentation("""
            <h2>data-sly-use</h2>
            <b>Description:</b> Exposes logic to the template<br>
            <b>Element:</b> always shown<br>
            <b>Attribute value:</b><br>
             - required: true<br>
             - type: String<br>
             - description: the object to instantiate.<br>
            <b>Attribute identifier:</b><br>
             - required: false<br>
             - description: Customised identifier name to access the instantiated logic<br>
            <b>Scope:</b> The identifier set by the <strong>data-sly-use</strong> block element is global to the script and can be used anywhere after its declaration.<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#221-use">Htl Specification</a>
        """)
  }

  fun testDataSlyTestDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_TEST)
    documentation("""
            <h2>data-sly-test</h2>
            <b>Description:</b> Keeps, or removes the element depending on the attribute value.<br>
            <b>Element:</b> shown if test evaluates to <strong>true</strong>.<br>
            <b>Content of element:</b> shown if test evaluates to <strong>true</strong>.<br>
            <b>Attribute value:</b><br>
             - required: false<br>
             - type: Boolean<br>
             - description: The attribute is evaluated as <strong>Boolean</strong> (but not type-casted to <strong>Boolean</strong> when exposed in a variable). Evaluates to <strong>false</strong> if the value is omitted.<br>
            <b>Attribute identifier:</b><br>
             - required: false<br>
             - description: Identifier name to access the result of the test.<br>
            <b>Scope:</b> The identifier set by the <strong>data-sly-test</strong> block element is global to the script and can be used anywhere after its declaration.<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#225-test">Htl Specification</a>
        """)
  }

  fun testDataSlySetDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_SET)
    documentation("""
            <h2>data-sly-set</h2>
            <b>Description:</b> Declares an HTML block, naming it with an identifier and defining the parameters it can get.<br>
            <b>Element:</b> never shown.<br>
            <b>Content of element:</b> shown upon calling the template with <strong>data-sly-call</strong>.<br>
            <b>Attribute value:</b><br>
             - required: false<br>
             - description: The value to store in the provided identifier.<br>
            <b>Attribute identifier:</b><br>
             - required: true<br>
             - description: The identifier name to access the stored value.<br>
            <b>Scope:</b> The identifier set by the <strong>data-sly-set</strong> block element is global to the script and can be used anywhere after its declaration.<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#2212-set">Htl Specification</a>
        """)
  }

  fun testDataSlyRepeatDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_REPEAT)
    documentation("""
            <h2>data-sly-repeat</h2>
            <b>Description:</b> Iterates over the content of each item in the attribute value and displays the containing element as many times as items in the attribute value.<br>
            <b>Element:</b> shown only if the number of items from the attribute value is greater than 0, or if the attribute value is a string or number.<br>
            <b>Content of element:</b> repeated as many times as there are items in the attribute value.<br>
            <b>Attribute value:</b><br>
             - required: false<br>
             - type: Iterable<br>
             - description: If omitted the content will not be shown.<br>
            <b>Attribute identifier:</b><br>
             - required: false<br>
             - description: Customised identifier name to access the item within the repeat element; if an identifier is not provided, the block element will implicitly make available an item identifier to access the element of the current iteration.<br>
            <b>Options:</b><br>
             - <strong>begin</strong>: iteration begins at the item located at the specified index; first item of the collection has index 0<br>
             - <strong>step</strong>: iteration will only process every step items of the collection, starting with the first one<br>
             - <strong>end</strong>: iteration ends at the item located at the specified index (inclusive)<br>
            <b>Scope:</b> The identifier set by the <strong>data-sly-repeat</strong> block element is available only in the element's scope. The identifier will override other identifiers with the same name available in the scope, however their values will be restored once outside of the element's scope.<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#227-repeat">Htl Specification</a>
        """)
  }

  fun testDataSlyListDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_LIST)
    documentation("""
            <h2>data-sly-list</h2>
            <b>Description:</b> Iterates over the content of each item in the attribute value.<br>
            <b>Element:</b> shown only if the number of items from the attribute value is greater than 0, or if the attribute value is a string or number; when the begin value is used the element will be shown only if the begin value is smaller than the collection's size.<br>
            <b>Content of element:</b> repeated as many times as there are items in the attribute value.<br>
            <b>Attribute value:</b><br>
             - required: false<br>
             - type: Iterable<br>
             - description: If omitted the content will not be shown.<br>
            <b>Attribute identifier:</b><br>
             - required: false<br>
             - description: Customised identifier name to access the item within the list element; if an identifier is not provided, the block element will implicitly make available an item identifier to access the element of the current iteration.<br>
            <b>Options:</b><br>
             - <strong>begin</strong>: iteration begins at the item located at the specified index; first item of the collection has index 0<br>
             - <strong>step</strong>: iteration will only process every step items of the collection, starting with the first one<br>
             - <strong>end</strong>: iteration ends at the item located at the specified index (inclusive)<br>
            <b>Scope:</b> The identifier set by the <strong>data-sly-list</strong> block element is available only in the element's content scope. The identifier will override other identifiers with the same name available in the scope, however their values will be restored once outside of the element's scope.<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#226-list">Htl Specification</a>
        """)
  }

  fun testDataSlyTemplateDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_TEMPLATE)
    documentation("""
            <h2>data-sly-template</h2>
            <b>Description:</b> Declares an HTML block, naming it with an identifier and defining the parameters it can get.<br>
            <b>Element:</b> never shown.<br>
            <b>Content of element:</b> shown upon calling the template with <strong>data-sly-call</strong>.<br>
            <b>Attribute value:</b><br>
             - required: false<br>
             - description: An expression with only options, defining the parameters it can get.<br>
            <b>Attribute identifier:</b><br>
             - required: true<br>
             - description: The template identifier to declare.<br>
            <b>Scope:</b> The identifier set by the <strong>data-sly-template</strong> block element is global and available no matter if it's accessed before or after the template's definition. An identically named identifier created with the help of another block element can override the value of the identifier set by <strong>data-sly-template</strong>.<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#22101-template">Htl Specification</a>
        """)
  }

  fun testDataSlyAttributeDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_ATTRIBUTE)
    documentation("""
            <h2>data-sly-attribute</h2>
            <b>Description:</b> Sets an attribute or group of attributes on the current element.<br>
            <b>Element:</b> always shown<br>
            <b>Content of element:</b> always shown<br>
            <b>Attribute value:</b><br>
             - required: false<br>
             - type:  [String, Boolean, Object] <br>
             - description: <strong>String</strong> for setting attribute content, or <strong>Boolean</strong> for setting boolean attributes, or <strong>Object</strong> for setting multiple attributes; removes the attribute if the value is omitted.<br>
            <b>Attribute identifier:</b><br>
             - required: false<br>
             - description: the attribute name; must be omitted only if attribute value is an <strong>Object</strong><br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#223-attribute">Htl Specification</a>
        """)
  }

  fun testDataSlyElementDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_ELEMENT)
    documentation("""
            <h2>data-sly-element</h2>
            <b>Description:</b> Replaces the element's tag name<br>
            <b>Element:</b> always shown<br>
            <b>Content of element:</b> always shown<br>
            <b>Attribute value:</b><br>
             - required: true<br>
             - type: String<br>
             - description: The element's tag name.<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#224-element">Htl Specification</a>
        """)
  }

  fun testDataSlyCallDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_CALL)
    documentation("""
            <h2>data-sly-call</h2>
            <b>Description:</b> Calls a declared HTML block, passing parameters to it.<br>
            <b>Element:</b> always shown.<br>
            <b>Content of element:</b> replaced with the content of the called <strong>data-sly-template</strong> element.<br>
            <b>Attribute value:</b><br>
             - required: false<br>
             - description: An expression defining the template identifier and the parameters to pass.<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#22102-call">Htl Specification</a>
        """)
  }

  fun testDataSlyIncludeDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_INCLUDE)
    documentation("""
            <h2>data-sly-include</h2>
            <b>Description:</b> Includes the output of rendering script run with the current context.<br>
            <b>Element:</b> always shown<br>
            <b>Content of element:</b> replaced with the content of the included script.<br>
            <b>Attribute value:</b><br>
             - required: true<br>
             - type: String<br>
             - description: The file to include.<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#228-include">Htl Specification</a>
        """)
  }

  fun testDataSlyUnwrapDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_UNWRAP)
    documentation("""
            <h2>data-sly-unwrap</h2>
            <b>Description:</b> Unwraps the element.<br>
            <b>Element:</b> shown if expression evaluates to false.<br>
            <b>Content of element:</b> always shown.<br>
            <b>Attribute value:</b><br>
             - required: false<br>
             - description: An expression evaluated as Boolean; defaults to true if the value is omitted.<br>
            <b>Attribute identifier:</b><br>
             - required: false<br>
             - description: The identifier name to access the result of the test.<br>
            <b>Scope:</b> The identifier set by the <strong>data-sly-unwrap</strong> block element is global to the script and can be used anywhere after its declaration.<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#2211-unwrap">Htl Specification</a>
        """)
  }

  fun testDataSlyTextDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_TEXT)
    documentation("""
            <h2>data-sly-text</h2>
            <b>Description:</b> Sets the content for the current element.<br>
            <b>Element:</b> always shown<br>
            <b>Content of element:</b> replaced with evaluated result.<br>
            <b>Attribute value:</b><br>
             - required: true<br>
             - type: String<br>
             - description: The element content<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#222-text">Htl Specification</a>
        """)
  }

  fun testDataSlyResourceDocumentation() = docCase {
    htlAttribute(const.htl.DATA_SLY_RESOURCE)
    documentation("""
            <h2>data-sly-resource</h2>
            <b>Description:</b> Includes a rendered resource.<br>
            <b>Element:</b> always shown.<br>
            <b>Content of element:</b> replaced with the content of the resource.<br>
            <b>Attribute value:</b><br>
             - required: true<br>
             - type: String<br>
             - description: The path to include.<br>
            <b>Options:</b><br>
             - <strong>resourceType</strong>: forces the type of the rendered resource<br>
             - <strong>wcmmode</strong>: changes the WCM mode<br>
             - <strong>decorationTagName</strong>: wraps included resources with tag<br>
             - <strong>cssClassName</strong>: adds CSS class to the element<br><br><br>
            See also: <a href="https://github.com/adobe/htl-spec/blob/master/SPECIFICATION.md#229-resource">Htl Specification</a>
        """)
  }
}
