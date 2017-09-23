package com.aemtools.blocks.fixture

import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture

/**
 * @author Dmytro Troynikov
 */
interface TestClassesMixin {
  fun JavaCodeInsightTestFixture.addClasses() {
    addClass("""
      package com.aemtools.completion.htl.fixtures.classes;

      import com.aemtools.completion.htl.fixtures.classes.*;
      import org.apache.sling.api.resource.*;
      import org.apache.sling.models.annotations.*;

      import java.util.*;

      /**
       * Documentation of CustomSlingModel.
       */
      @Model(adaptables = Resource.class)
      public class CustomSlingModel {

      /**
       * Documentation for publicString.
       */
      public String publicString;
      /**
       * Documentation for publicStringArray.
       */
      public String[] publicStringArray;
      /**
       * Documentation for publicStringList.
       */
      public List<String> publicStringList;
      /**
       * Documentation for publicStringMap.
       */
      public Map<String, String> publicStringMap;
      /**
       * Documentation for publicBoolean.
       */
      public boolean publicBoolean;

      private String stringField;
      private String[] stringArray;
      private List<String> stringList;
      private Map<String, String> stringMap;
      private boolean booleanField;

      private List<MyModel> modelList;

      public List<MyModel> getModelList() {
          return modelList;
      }

      /**
       * Documentation for getStringField.
       *
       * @return the stringField
       */
      public String getStringField() {
          return stringField;
      }

      /**
       * Documentation for getStringArray.
       * @return the stringArray
       */
      public String[] getStringArray() {
          return stringArray;
      }

      /**
       * Documentation for getStringList.
       * @return the stringList
       */
      public List<String> getStringList() {
          return stringList;
      }

      /**
       * Documentation for getStringMap.
       * @return the stringMap
       */
      public Map<String, String> getStringMap() {
          return stringMap;
      }

      /**
       * Documentation for getBooleanField.
       * @return the booleanField
       */
      public boolean getBooleanField() {
          return booleanField;
      }

      /**
       * Documentation for isBooleanField.
       * @return the boolean field
       */
      public boolean isBooleanField() {
          return booleanField;
      }
      }
    """)
    addClass("""
      package com.aemtools.completion.htl.fixtures.classes;

      import io.sightly.java.api.Use;

      /**
       * Documentation for CustomUseClass
       */
      public class CustomUseClass implements Use {

          /**
           * Public String field
           */
          public String publicStringField;

      }
    """)
    addClass("""
      package com.aemtools.completion.htl.fixtures.classes;

      import com.adobe.cq.sightly.WCMUse;

      /**
       * Documentation for CustomUseClass
       */
      public class CustomWcmUseClass extends WCMUse {

        /**
         * Public String field
         */
        public String publicStringField;

      }
    """)

    addClass("""
      package com.aemtools.completion.htl.fixtures.classes;

      import org.apache.sling.models.annotations.Model;
      import com.aemtools.completion.htl.fixtures.classes.MyModel;
      import com.aemtools.completion.htl.fixtures.classes.MapModel;

      import java.util.List;
      import java.util.Map;

      /**
       * Documentation of IterableModel.
       */
      @Model(adaptables = Resource.class)
      public class IterableModel {

        public MyModel[] fieldModelArray;
        public List<MyModel> fieldModelList;
        public Map<MyModel, MapModel> fieldModelMap;

        public MyModel[] getMethodModelArray() {
            return null;
        }

        public List<MyModel> getMethodModelList() {
            return null;
        }

        public Map<MyModel, MapModel> getMethodModelMap() {
            return null;
        }

      }
    """)

    addClass("""
      package com.aemtools.completion.htl.fixtures.classes;

      public class MapModel {

        public String mapModelField;

        public Boolean mapModelBooleanField;

        public String getMethodMapModelString() {
            return null;
        }

        public Boolean getMethodMapModelBoolean() {
            return null;
        }

      }
    """)

    addClass("""
      package com.aemtools.completion.htl.fixtures.classes;

      public class MyModel {

          public String modelFieldString;

          public Boolean modelFieldBoolean;

          public String getMethodModelString() {
              return null;
          }

          public Boolean getMethodModelBoolean() {
              return null;
          }

      }
    """)
  }
}
