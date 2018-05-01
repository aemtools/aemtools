package com.aemtools.completion.small.reppolicy.provider

import com.aemtools.test.completion.CompletionBaseLightTest
import com.aemtools.test.completion.model.ICompletionTestFixture
import org.intellij.lang.annotations.Language

/**
 * Test for [RepPolicyCompletionContributor].
 *
 * @author Dmytro Primshyts
 */
class RepPolicyCompletionProviderTest : CompletionBaseLightTest(false) {

  fun testPrimaryTypeUnknown() = completionTest {
    repPolicy("""
      <unknownName jcr:primaryType="$CARET"
    """)

    shouldContain(
        "rep:GrantACE",
        "rep:DenyACE"
    )
  }

  fun testRootLevelAttributes() = completionTest {
    repPolicy("""
       <allow3 $CARET
    """)

    shouldContain(
        "jcr:primaryType",
        "rep:principalName",
        "rep:privileges"
    )
  }

  fun testPrivileges() = completionTest {
    repPolicy("""
        <allow1
         jcr:primaryType="rep:GrantACE"
         rep:principalName="testuser"
         rep:privileges="{Name}[$CARET]" />
    """)
    shouldContain(
        "jcr:versionManagement",
        "jcr:addChildNodes",
        "jcr:readAccessControl",
        "jcr:nodeTypeDefinitionManagement",
        "jcr:lifecycleManagement",
        "jcr:retentionManagement",
        "rep:alterProperties",
        "jcr:removeNode",
        "rep:readProperties",
        "rep:indexDefinitionManagement",
        "jcr:write",
        "rep:privilegeManagement",
        "jcr:workspaceManagement",
        "rep:removeProperties",
        "jcr:modifyAccessControl",
        "jcr:namespaceManagement",
        "rep:userManagement",
        "jcr:read",
        "rep:write",
        "rep:addProperties",
        "jcr:lockManagement",
        "jcr:nodeTypeManagement",
        "jcr:removeChildNodes",
        "jcr:modifyProperties",
        "rep:readNodes",
        "crx:replicate"
    )
  }

  fun testPrivilegesSecondPosition() = completionTest {
    repPolicy("""
        <allow1
         jcr:primaryType="rep:GrantACE"
         rep:principalName="testuser"
         rep:privileges="{Name}[crx:replicate, $CARET]" />
    """)

    shouldContain(
        "jcr:versionManagement",
        "jcr:addChildNodes",
        "jcr:readAccessControl",
        "jcr:nodeTypeDefinitionManagement",
        "jcr:lifecycleManagement",
        "jcr:retentionManagement",
        "rep:alterProperties",
        "jcr:removeNode",
        "rep:readProperties",
        "rep:indexDefinitionManagement",
        "jcr:write",
        "rep:privilegeManagement",
        "jcr:workspaceManagement",
        "rep:removeProperties",
        "jcr:modifyAccessControl",
        "jcr:namespaceManagement",
        "rep:userManagement",
        "jcr:read",
        "rep:write",
        "rep:addProperties",
        "jcr:lockManagement",
        "jcr:nodeTypeManagement",
        "jcr:removeChildNodes",
        "jcr:modifyProperties",
        "rep:readNodes"
    )
  }

  fun testRestrictions() = completionTest {
    repPolicy("""
       <allow0>
         <rep:restrictions
            $CARET
       </allow0>
    """)

    shouldContain(
        "jcr:primaryType",
        "rep:glob"
    )
  }

  fun testRootLevelCompletionAllow() = completionTest {
    repPolicy("""
            <a$CARET
        """)
    shouldContain(
        "allow"
    )
  }

  fun testRootLevelCompletionAllow2() = completionTest {
    repPolicy("""
           <allow></allow>
           <a$CARET
        """)
    shouldContain(
        "allow",
        "allow0"
    )
  }

  fun testRootLevelCompletionDeny() = completionTest {
    repPolicy("""
            <d$CARET
        """)
    shouldContain(
        "deny"
    )
  }

  fun testRootLevelCompletionDeny2() = completionTest {
    repPolicy("""
           <deny></deny>
           <d$CARET
        """)
    shouldContain("deny2")
  }

  fun testSecondLevel() = completionTest {
    repPolicy("""
            <allow0>
                <re$CARET
            </allow0>
        """)
    shouldContain("rep:restrictions")
  }

  private fun ICompletionTestFixture.repPolicy(@Language("XML") body: String) {
    addXml("_rep_policy.xml", """
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:rep="internal"
      jcr:primaryType="rep:ACL">
    $body
</jcr:root>
    """.trimIndent())
  }

}
