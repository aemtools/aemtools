package com.aemtools.index.component

import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.xml.index.XmlIndex

/**
 * @author Dmytro Troynikov
 */
class ClassicDialogXmlIndex : XmlIndex<ClassicDialogDefinition>() {

    companion object {
        val CLASSIC_DIALOG_INDEX_ID: ID<String, ClassicDialogDefinition>
            = ID.create<String, ClassicDialogDefinition>("ClassicDialogIndex")
    }

    override fun getName(): ID<String, ClassicDialogDefinition> = CLASSIC_DIALOG_INDEX_ID

    override fun getValueExternalizer(): DataExternalizer<ClassicDialogDefinition> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getIndexer(): DataIndexer<String, ClassicDialogDefinition, FileContent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}