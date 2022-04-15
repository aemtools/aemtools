package com.test;

public class Test {

    public void foo() {
        String test1 = "jcr:primaryType";
        String test2 = " / "; // should not be reported
    }

}
