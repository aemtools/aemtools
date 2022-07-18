package com.aemtools.codeinsight.htl.annotator

import com.aemtools.lang.settings.model.HtlVersion

/**
 * Tests for [HtlVariablesAnnotator] to check data-sly-BLOCK scope in HTL v 1.4.
 *
 * @author Kostiantyn Diachenko
 */
class HtlVariablesScopeAnnotatorV14Test : HtlVariablesScopeAnnotatorTest() {

  override fun setUp() {
    super.setUp()
    myFixture.setHtlVersion(HtlVersion.V_1_4)
  }

  fun `test data-sly-list default identifiers usage with data-sly-set`() {
    myFixture.configureByText("test.html", """
        <nav <info descr="null">data-sly-set</info>.<weak_warning descr="null">vals</weak_warning>="$DOLLAR{['testval1', 'testval2']}">
          <ul <info descr="null">data-sly-list</info>="$DOLLAR{<weak_warning descr="null">vals</weak_warning>}">
            <li data-position="$DOLLAR{<weak_warning descr="null">itemList</weak_warning>.index}">
              $DOLLAR{<weak_warning descr="null">item</weak_warning>}
            </li>
            $DOLLAR{<weak_warning descr="null">itemList</weak_warning>.count}
            $DOLLAR{<weak_warning descr="null">itemList</weak_warning>.first}
            $DOLLAR{<weak_warning descr="null">itemList</weak_warning>.middle}
            $DOLLAR{<weak_warning descr="null">itemList</weak_warning>.last}
            $DOLLAR{<weak_warning descr="null">itemList</weak_warning>.odd}
            $DOLLAR{<weak_warning descr="null">itemList</weak_warning>.even}
          </ul>
        </nav>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test data-sly-list default identifiers usage out of declaration tag with data-sly-set`() {
    myFixture.configureByText("test.html", """
        <nav <info descr="null">data-sly-set</info>.<weak_warning descr="null">vals</weak_warning>="$DOLLAR{['testval1', 'testval2']}">
          <ul <info descr="null">data-sly-list</info>="$DOLLAR{<weak_warning descr="null">vals</weak_warning>}">
            <li data-position="$DOLLAR{<weak_warning descr="null">itemList</weak_warning>.index}">
              $DOLLAR{<weak_warning descr="null">item</weak_warning>}
            </li>
          </ul>
          $DOLLAR{<weak_warning descr="Cannot resolve symbol 'itemList'">itemList</weak_warning>.index}
          $DOLLAR{<weak_warning descr="Cannot resolve symbol 'item'">item</weak_warning>}
        </nav>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test data-sly-list custom identifier usage with data-sly-set`() {
    myFixture.configureByText("test.html", """
        <nav <info descr="null">data-sly-set</info>.<weak_warning descr="null">vals</weak_warning>="$DOLLAR{['testval1', 'testval2']}">
          <ul <info descr="null">data-sly-list</info>.<weak_warning descr="null">myVal</weak_warning>="$DOLLAR{<weak_warning descr="null">vals</weak_warning>}">
            <li data-position="$DOLLAR{<weak_warning descr="null">myValList</weak_warning>.index}">
              $DOLLAR{<weak_warning descr="null">myVal</weak_warning>}
            </li>
          </ul>
          $DOLLAR{<weak_warning descr="Cannot resolve symbol 'myValList'">myValList</weak_warning>.index}
          $DOLLAR{<weak_warning descr="Cannot resolve symbol 'myVal'">myVal</weak_warning>}
        </nav>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test variable usage after declaration in the tag with data-sly-set on the same level`() {
    myFixture.configureByText("test.html", """
      <div>
        <sly <info descr="null">data-sly-set</info>.<weak_warning descr="null">var1</weak_warning>=""/>
        $DOLLAR{<weak_warning descr="null">var1</weak_warning>}
      </div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test variable usage before declaration in the tag with data-sly-set on the same level`() {
    myFixture.configureByText("test.html", """
      <div>
        $DOLLAR{<weak_warning descr="Cannot resolve symbol 'var1'">var1</weak_warning>}
        <sly <info descr="null">data-sly-set</info>.<warning descr="null">var1</warning>=""/>
      </div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test variable usage after declaration in the tag with data-sly-unwrap`() {
    myFixture.configureByText("test.html", """
      <div>
        <sly <info descr="null">data-sly-unwrap</info>.<weak_warning descr="null">var1</weak_warning>=""/>
        $DOLLAR{<weak_warning descr="null">var1</weak_warning>}
      </div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test variable usage before declaration in the tag with data-sly-unwrap`() {
    myFixture.configureByText("test.html", """
      <div>
        $DOLLAR{<weak_warning descr="Cannot resolve symbol 'var1'">var1</weak_warning>}
        <sly <info descr="null">data-sly-unwrap</info>.<warning descr="null">var1</warning>=""/>
      </div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

}
