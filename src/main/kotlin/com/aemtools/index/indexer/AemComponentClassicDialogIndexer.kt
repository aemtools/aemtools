package com.aemtools.index.indexer

import com.aemtools.index.model.AemComponentClassicDialogDefinition
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent

/**
 * @autor Dmytro Troynikov
 */
object AemComponentClassicDialogIndexer : DataIndexer<String, AemComponentClassicDialogDefinition, FileContent> {
    override fun map(inputData: FileContent): MutableMap<String, AemComponentClassicDialogDefinition> {

        return mutableMapOf()
    }
}