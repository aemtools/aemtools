package com.aemtools.ide.refactoring

import com.aemtools.blocks.rename.BaseRenameTest

/**
 * @author Dmytro Troynikov
 */
class JavaToHtlRenameTest : BaseRenameTest() {

  fun testMethodRenameFromJava() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean {
                    public String ${CARET}getField() { return ""; }
                }
            """)

      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.field}
                </div>
            """)
    }
    renameTo("getRenamed")
    after {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean {
                    public String getRenamed() { return ""; }
                }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.renamed}
                </div>
            """)
    }
  }

  fun testMethodRenameFromJavaReferencedByFullName() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String ${CARET}getField() { return ""; } }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.getField}
                </div>
            """)
    }
    renameTo("getRenamed")
    after {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String getRenamed() { return ""; } }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.getRenamed}
                </div>
            """)
    }
  }

  fun testMethodRenameFromJavaReferencedByArrayLikeSinglequoted() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String ${CARET}getField() { return ""; } }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean['field']}
                </div>
            """)
    }
    renameTo("getRenamed")
    after {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String getRenamed() { return ""; } }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean['renamed']}
                </div>
            """)
    }
  }

  fun testMethodRenameFromJavaReferencedByArrayLikeDoublequoted() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String ${CARET}getField() { return ""; } }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean["field"]}
                </div>
            """)
    }
    renameTo("getRenamed")
    after {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String getRenamed() { return ""; } }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean["renamed"]}
                </div>
            """)
    }
  }

  fun testFieldRenameFromJava() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String ${CARET}field; }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.field}
                </div>
            """)
    }
    renameTo("renamed")
    after {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String renamed; }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.renamed}
                </div>
            """)
    }
  }

  fun testRenameClassReferencedViaAttribute() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test; public class ${CARET}Bean {}
            """)
      addHtml("test.html", "<div data-sly-use.bean='com.test.Bean'></div>")
    }
    renameTo("Renamed")
    after {
      addClass("com/test/Renamed.java", """
                package com.test; public class Renamed {}
            """)
      addHtml("test.html", "<div data-sly-use.bean='com.test.Renamed'></div>")
    }
  }

  fun testRenameClassReferencedViaEl() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test; public class ${CARET}Bean {}
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="$DOLLAR{'com.test.Bean'}"></div>
            """)
    }
    renameTo("Renamed")
    after {
      addClass("com/test/Renamed.java", """
                package com.test; public class Renamed {}
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="$DOLLAR{'com.test.Renamed'}"></div>
            """)
    }
  }

  fun testRenameClassReferencedViaElDoublequoted() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test; public class ${CARET}Bean {}
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="$DOLLAR{"com.test.Bean"}"></div>
            """)
    }
    renameTo("Renamed")
    after {
      addClass("com/test/Renamed.java", """
                package com.test; public class Renamed {}
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="$DOLLAR{"com.test.Renamed"}"></div>
            """)
    }
  }

}
