package com.aemtools.index

import com.aemtools.constant.const.JCR_ROOT_SEPARATED
import com.aemtools.index.dataexternalizer.TemplateDefinitionExternalizer
import com.aemtools.index.indexer.HtlTemplateIndexer
import com.aemtools.index.model.TemplateDefinition
import com.aemtools.util.OpenApiUtil
import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.xml.index.XmlIndex

/**
 * @author Dmytro_Troynikov
 */
class HtlTemplateIndex : XmlIndex<TemplateDefinition>() {

    companion object {
        val HTL_TEMPLATE_ID: ID<String, TemplateDefinition>
                = ID.create<String, TemplateDefinition>("HtlTemplateIndex")
    }

    override fun getIndexer(): DataIndexer<String, TemplateDefinition, FileContent>
            = HtlTemplateIndexer

    override fun getInputFilter(): FileBasedIndex.InputFilter
            = FileBasedIndex.InputFilter {
        it.fileType == HtmlFileType.INSTANCE
                // index all html files in tests
                && (OpenApiUtil.iAmTest() ||
                it.path.contains(JCR_ROOT_SEPARATED)) }

    override fun getValueExternalizer(): DataExternalizer<TemplateDefinition>
            = TemplateDefinitionExternalizer

    override fun getName(): ID<String, TemplateDefinition>
            = HTL_TEMPLATE_ID

}
