package com.aemtools.inspection.html

import com.aemtools.inspection.service.IInspectionService
import com.aemtools.test.util.anyKotlin
import com.aemtools.test.util.mock
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttribute
import io.kotest.core.spec.style.ShouldSpec
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.*

/**
 * Specification for [MessedDataSlyAttributeInspection].
 *
 * @author Dmytro Primshyts
 */
object MessedDataSlyAttributeInspectionSpec : ShouldSpec({
  val tested = MessedDataSlyAttributeInspection()

  context("style check") {
    should("have correct group display name") {
      assertThat(tested.groupDisplayName)
        .isEqualTo("HTL")
    }

    should("have correct display name") {
      assertThat(tested.displayName)
        .isEqualTo("data-sly-attribute with prohibited attributes")
    }

    should("have correct static description") {
      assertThat(tested.staticDescription)
        .isEqualTo(
          """
<html>
<body>
This inspection verifies that <i>data-sly-attribute</i> is
<b>not</b> used with prohibited attributes, such as <b>style</b> or event attributes i.e.
attributes that take JavaScript as input (e.g. onclick, onmousemove, etc).
</body>
</html>""".trimIndent()
        )
    }
  }

  context("check attribute") {
    val project: Project = mock()
    val attribute: XmlAttribute = mock()
    val holder: ProblemsHolder = mock()
    val inspectionService: IInspectionService = mock()
    beforeEach {
      reset(project, attribute, holder, inspectionService)
      `when`(attribute.project)
        .thenReturn(project)
      `when`(project.getService(IInspectionService::class.java))
        .thenReturn(inspectionService)
    }

    context("wrong attribute") {
      val wrongAttributes = listOf(
        "style",
        "onafterprint",
        "onbeforeprint",
        "onbeforeunload",
        "onerror",
        "onhashchange",
        "onload",
        "onmessage",
        "onoffline",
        "ononline",
        "onpagehide",
        "onpageshow",
        "onpopstate",
        "onresize",
        "onstorage",
        "onunload",
        "onblur",
        "onchange",
        "oncontextmenu",
        "onfocus",
        "oninput",
        "oninvalid",
        "onreset",
        "onsearch",
        "onselect",
        "onsubmit",
        "onkeydown",
        "onkeypress",
        "onkeyup",
        "onclick",
        "ondblclick",
        "onmousedown",
        "onmousemove",
        "onmouseout",
        "onmouseover",
        "onmouseup",
        "onmousewheel",
        "onwheel",
        "ondrag",
        "ondragend",
        "ondragenter",
        "ondragleave",
        "ondragover",
        "ondragstart",
        "ondrop",
        "onscroll",
        "oncopy",
        "oncut",
        "onpaste",
        "onabort",
        "oncanplay",
        "oncanplaythrough",
        "oncuechange",
        "ondurationchange",
        "onemptied",
        "onended",
        "onerror",
        "onloadeddata",
        "onloadedmetadata",
        "onloadstart",
        "onpause",
        "onplay",
        "onplaying",
        "onprogress",
        "onratechange",
        "onseeked",
        "onseeking",
        "onstalled",
        "onsuspend",
        "ontimeupdate",
        "onvolumechange",
        "onwaiting",
        "onshow",
        "ontoggle"
      )

      wrongAttributes.forEach { wrongAttribute ->
        should("report $wrongAttribute") {
          `when`(attribute.name)
            .thenReturn("data-sly-attribute.$wrongAttribute")
          `when`(inspectionService.validTarget(anyKotlin()))
            .thenReturn(true)

          tested.checkAttribute(attribute, holder, false)

          verify(inspectionService)
            .messedDataSlyAttribute(
              holder,
              attribute,
              wrongAttribute
            )
        }
      }

    }

    context("correct attribute") {
      `when`(inspectionService.validTarget(attribute))
        .thenReturn(true)
      `when`(attribute.name)
        .thenReturn("data-sly-attribute.correct")
      should("do nothing") {
        tested.checkAttribute(attribute, holder, false)

        verify(inspectionService, never())
          .messedDataSlyAttribute(
            anyKotlin(), anyKotlin(), anyKotlin()
          )
      }
    }

    context("not data-sly-attribute") {
      `when`(inspectionService.validTarget(attribute))
        .thenReturn(true)
      `when`(attribute.name)
        .thenReturn("style")
      should("do nothing") {
        tested.checkAttribute(attribute, holder, false)

        verify(inspectionService, never())
          .messedDataSlyAttribute(
            anyKotlin(), anyKotlin(), anyKotlin()
          )
      }
    }

    context("inspection disabled") {
      `when`(inspectionService.validTarget(attribute))
        .thenReturn(false)
      should("do nothing") {
        tested.checkAttribute(attribute, holder, false)

        verify(inspectionService, never())
          .messedDataSlyAttribute(
            anyKotlin(), anyKotlin(), anyKotlin()
          )
      }
    }

  }

})
