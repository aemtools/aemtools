package com.aemtools.codeinsight.xml.ns

enum class AemNamespaces(val prefix: String, val namespace: String) {
  CQ("cq", "http://www.day.com/jcr/cq/1.0"),
  JCR("jcr", "http://www.jcp.org/jcr/1.0"),
  CRX("crx", "http://www.day.com/crx/1.0"),
  GRANITE("granite", "http://www.adobe.com/jcr/granite/1.0"),
  NT("nt", "http://www.jcp.org/jcr/nt/1.0"),
  OAK("oak", "http://jackrabbit.apache.org/oak/ns/1.0"),
  SLING("sling", "http://sling.apache.org/jcr/sling/1.0"),
  MIX("mix", "http://www.jcp.org/jcr/mix/1.0");
}
