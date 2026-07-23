package com.aemtools.index.dataexternalizer

import com.aemtools.index.model.AemComponentDefinition
import com.aemtools.index.model.ClientlibraryModel
import com.aemtools.index.model.LocalizationModel
import com.aemtools.index.model.OSGiConfigurationIndexModel
import com.aemtools.index.model.TemplateDefinition
import com.aemtools.index.model.dialog.AemComponentClassicDialogDefinition
import com.aemtools.index.model.dialog.AemComponentTouchUIDialogDefinition
import com.aemtools.index.model.dialog.parameter.ClassicDialogParameterDeclaration
import com.aemtools.index.model.dialog.parameter.TouchUIDialogParameterDeclaration
import com.intellij.util.io.DataExternalizer
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

/**
 * Tests for stable index value externalizers.
 *
 * @author Dmytro Primshyts
 */
class IndexExternalizerTest {

  @Test
  fun `template definition should round trip`() {
    val fixture = TemplateDefinition("path", "name", listOf("param1", "param2"))

    assertEquals(fixture, roundTrip(fixture, TemplateDefinitionExternalizer))
  }

  @Test
  fun `AEM component definition should round trip`() {
    val fixture = AemComponentDefinition(
        title = "Title",
        description = "Description",
        fullPath = "/apps/site/components/title/.content.xml",
        resourceSuperType = "core/wcm/components/title/v3/title",
        componentGroup = "Site",
        isContainer = true,
        cqIcon = "text"
    )

    assertEquals(fixture, roundTrip(fixture, AemComponentDeclarationExternalizer))
  }

  @Test
  fun `classic dialog definition should round trip`() {
    val fixture = AemComponentClassicDialogDefinition(
        fullPath = "/apps/site/components/title/dialog.xml",
        resourceType = "site/components/title",
        myParameters = listOf(
            ClassicDialogParameterDeclaration("textfield", "./jcr:title"),
            ClassicDialogParameterDeclaration("checkbox", "./hideInNav")
        )
    )

    assertEquals(fixture, roundTrip(fixture, AemComponentClassicDialogDefinitionExternalizer))
  }

  @Test
  fun `touch UI dialog definition should round trip`() {
    val fixture = AemComponentTouchUIDialogDefinition(
        fullPath = "/apps/site/components/title/_cq_dialog/.content.xml",
        resourceType = "site/components/title",
        myParameters = listOf(
            TouchUIDialogParameterDeclaration("granite/ui/components/coral/foundation/form/textfield", "./jcr:title"),
            TouchUIDialogParameterDeclaration("granite/ui/components/coral/foundation/form/checkbox", "./hideInNav")
        )
    )

    assertEquals(fixture, roundTrip(fixture, AemComponentTouchUIDialogDefinitionExternalizer))
  }

  @Test
  fun `client library model should round trip`() {
    val fixture = ClientlibraryModel(
        channels = listOf("touch"),
        categories = listOf("site.base"),
        dependencies = listOf("granite.jquery"),
        embed = listOf("site.vendor"),
        filePath = "/apps/site/clientlibs/.content.xml"
    )

    assertEquals(fixture, roundTrip(fixture, ClientlibraryExternalizer()))
  }

  @Test
  fun `localization model should round trip`() {
    val fixture = LocalizationModel(
        fileName = "/apps/site/i18n/en/.content.xml",
        language = "en",
        key = "hello",
        message = "Hello {0}"
    )

    assertEquals(fixture, roundTrip(fixture, LocalizationModelExternalizer))
  }

  @Test
  fun `OSGi configuration model should round trip`() {
    val fixture = OSGiConfigurationIndexModel(
        path = "/apps/site/config/org.example.Service.config",
        parameters = mapOf(
            "enabled" to "true",
            "optional" to null
        )
    )

    assertEquals(fixture, roundTrip(fixture, OSGiConfigurationExternalizer))
  }

  private fun <T> roundTrip(value: T, externalizer: DataExternalizer<T>): T {
    val bytes = ByteArrayOutputStream()
    DataOutputStream(bytes).use { externalizer.save(it, value) }
    return DataInputStream(ByteArrayInputStream(bytes.toByteArray())).use { externalizer.read(it) }
  }
}
