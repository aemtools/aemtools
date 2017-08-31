package com.aemtools.index.dataexternalizer

import com.aemtools.index.model.TemplateDefinition
import com.aemtools.util.ObjectSerializer
import com.aemtools.util.serializeToByteArray
import com.intellij.util.io.DataExternalizer
import java.io.ByteArrayOutputStream
import java.io.DataInput
import java.io.DataOutput

/**
 * @author Dmytro_Troynikov
 */
object TemplateDefinitionExternalizer : BaseExternalizer<TemplateDefinition>()

