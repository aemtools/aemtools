package com.aemtools.test.sdk

import com.intellij.pom.java.LanguageLevel
import com.intellij.testFramework.IdeaTestUtil

/**
 * @author Kostiantyn Diachenko
 */
class TestSdk {

    companion object {
        @JvmStatic
        fun getSdk() = IdeaTestUtil.getMockJdk11()

        @JvmStatic
        fun getSdkLanguageLevel() = LanguageLevel.JDK_11
    }
}
