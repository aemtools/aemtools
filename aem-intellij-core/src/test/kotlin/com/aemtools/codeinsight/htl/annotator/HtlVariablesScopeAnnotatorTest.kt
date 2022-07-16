package com.aemtools.codeinsight.htl.annotator

import com.aemtools.test.base.BaseLightTest

/**
 * Tests for [HtlVariablesAnnotator] to check data-sly-BLOCK scope.
 *
 * @author Kostiantyn Diachenko
 */
abstract class HtlVariablesScopeAnnotatorTest : BaseLightTest() {
  fun `test variable usage before declaration in the tag with data-sly-test`() {
    myFixture.configureByText("test.html", """
        $DOLLAR{<weak_warning descr="Cannot resolve symbol 'customVar'">customVar</weak_warning>}
        <sly <info descr="null">data-sly-test</info>.<warning descr="null">customVar</warning>="$DOLLAR{'value'}"/>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test variable usage after declaration in the tag with data-sly-test`() {
    myFixture.configureByText("test.html", """
        <sly <info descr="null">data-sly-test</info>.<weak_warning descr="null">var1</weak_warning>=""/>
        $DOLLAR{<weak_warning descr="null">var1</weak_warning>}
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test variable usage after declaration in the tag with data-sly-test on the different level`() {
    myFixture.configureByText("test.html", """
        <div>
          <sly <info descr="null">data-sly-test</info>.<weak_warning descr="null">var1</weak_warning>=""/>
        </div>
        $DOLLAR{<weak_warning descr="null">var1</weak_warning>}
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test variable usage before declaration in the tag with data-sly-test on the different level`() {
    myFixture.configureByText("test.html", """
        $DOLLAR{<weak_warning descr="Cannot resolve symbol 'var1'">var1</weak_warning>}
        <div>
          <sly <info descr="null">data-sly-test</info>.<warning descr="null">var1</warning>=""/>
        </div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test data-sly-list default identifiers usage`() {
    myFixture.configureByText("test.html", """
        <nav <info descr="null">data-sly-test</info>.<weak_warning descr="null">vals</weak_warning>="$DOLLAR{['testval1', 'testval2']}">
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

  fun `test data-sly-list default identifiers usage out of declaration tag`() {
    myFixture.configureByText("test.html", """
        <nav <info descr="null">data-sly-test</info>.<weak_warning descr="null">vals</weak_warning>="$DOLLAR{['testval1', 'testval2']}">
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

  fun `test data-sly-list custom identifier usage`() {
    myFixture.configureByText("test.html", """
        <nav <info descr="null">data-sly-test</info>.<weak_warning descr="null">vals</weak_warning>="$DOLLAR{['testval1', 'testval2']}">
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

  fun `test data-sly-repeat default identifiers usage`() {
    myFixture.configureByText("test.html", """
        <nav <info descr="null">data-sly-test</info>.<weak_warning descr="null">vals</weak_warning>="$DOLLAR{['testval1', 'testval2']}">
          <ul <info descr="null">data-sly-repeat</info>="$DOLLAR{<weak_warning descr="null">vals</weak_warning>}">
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

  fun `test data-sly-repeat default identifiers usage out of declaration tag`() {
    myFixture.configureByText("test.html", """
        <nav <info descr="null">data-sly-test</info>.<weak_warning descr="null">vals</weak_warning>="$DOLLAR{['testval1', 'testval2']}">
          <ul <info descr="null">data-sly-repeat</info>="$DOLLAR{<weak_warning descr="null">vals</weak_warning>}">
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

  fun `test data-sly-repeat custom identifier usage`() {
    myFixture.configureByText("test.html", """
        <nav <info descr="null">data-sly-test</info>.<weak_warning descr="null">vals</weak_warning>="$DOLLAR{['testval1', 'testval2']}">
          <ul <info descr="null">data-sly-repeat</info>.<weak_warning descr="null">myVal</weak_warning>="$DOLLAR{<weak_warning descr="null">vals</weak_warning>}">
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

  fun `test data-sly-template visibility after declaration`() {
    myFixture.configureByText("test.html", """
        <template <info descr="null">data-sly-template</info>.<info descr="null">one</info>>blah</template>
        <div <info descr="null">data-sly-call</info>="$DOLLAR{<weak_warning descr="null">one</weak_warning>}"></div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test data-sly-template visibility before declaration`() {
    myFixture.configureByText("test.html", """
        <div <info descr="null">data-sly-call</info>="$DOLLAR{<weak_warning descr="null">one</weak_warning>}"></div>
        <template <info descr="null">data-sly-template</info>.<info descr="null">one</info>>blah</template>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test variable usage after declaration in the same tag`() {
    myFixture.configureByText("test.html", """
      <div>
        <sly <info descr="null">data-sly-test</info>.<weak_warning descr="null">var1</weak_warning>="" 
        <info descr="null">data-sly-attribute</info>.data="$DOLLAR{<weak_warning descr="null">var1</weak_warning>}"/>
      </div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test variable usage before declaration in the same tag`() {
    myFixture.configureByText("test.html", """
      <div>
        <sly <info descr="null">data-sly-attribute</info>.data="$DOLLAR{<weak_warning descr="null">var1</weak_warning>}" 
        <info descr="null">data-sly-test</info>.<weak_warning descr="null">var1</weak_warning>=""/>
      </div>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test unresolved variable in the declaration attribute`() {
    myFixture.configureByText("test.html", """
      <div data-sly-test.<warning descr="null">var</warning>="$DOLLAR{<weak_warning descr="Cannot resolve symbol 'var'">var</weak_warning>}"></div>
    """.trimIndent())
    myFixture.testHighlighting()
  }
}
