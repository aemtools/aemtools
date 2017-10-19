package com.aemtools.ide.refactoring

import com.aemtools.blocks.rename.BaseRenameTest

/**
 * @author Dmytro Troynikov
 */
class HtlToJavaRenameTest : BaseRenameTest() {

  fun testMethodRenameFromHtl() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean {
                    public String getField() { return ""; }
                }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.${CARET}field}
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

  fun testMethodRenameFromHtlByFullName() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean {
                    public String getField() { return ""; }
                }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.${CARET}getField}
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
                    $DOLLAR{bean.getRenamed}
                </div>
            """)
    }
  }

  fun testMethodRenameFromHtlArrayLikeSinglequoted() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String getField() { return ""; } }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean['${CARET}field']}
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

  fun testMethodRenameFromHtlArrayLikeDoublequoted() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String getField() { return ""; } }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean["${CARET}field"]}
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

  fun testFieldRenameFromHtl() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test;
                public class Bean { public String field; }
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Bean">
                    $DOLLAR{bean.${CARET}field}
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

  fun testRenameUseClassViaAttribute() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test; public class Bean {}
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.${CARET}Bean"></div>
            """)
    }
    renameTo("Renamed")
    after {
      addClass("com/test/Renamed.java", """
                package com.test; public class Renamed {}
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="com.test.Renamed"></div>
            """)
    }
  }

  fun testRenameUseClassViaEl() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test; public class Bean {}
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="$DOLLAR{'com.test.${CARET}Bean'}"></div>
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

  fun testRenameUseClassViaElDoublequoted() = renameCase {
    before {
      addClass("com/test/Bean.java", """
                package com.test; public class Bean {}
            """)
      addHtml("test.html", """
                <div data-sly-use.bean="$DOLLAR{"com.test.${CARET}Bean"}"></div>
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
