package com.aemtools.completion.htl.smart

import com.aemtools.test.completion.CompletionBaseLightTest

/**
 * @author Dmytro Troynikov
 */
class DataSlyListSmartCompletionTest : CompletionBaseLightTest(true) {

  fun testUseWithList() = completionTest {
    addClass("TestModel.java", """
        package com.test;

        import java.util.List;

        public class TestModel {
            public List<String> stringList;
            public InnerModel innerModel;
        }
        class InnerModel {
            public List<String> getInnerModelList() {
                return null;
            }
        }
    """)

    addHtml("test.html", """
        <div data-sly-use.bean="com.test.TestModel">
            <div data-sly-list="$DOLLAR{$CARET}"></div>
        </div>
    """)

    smart()

    shouldContain(
        "bean.stringList",
        "bean.innerModel.innerModelList"
    )

    verify {

    }
  }

}
