package com.aemtools.lang.el.file

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

/**
 * @author Dmytro Primshyts
 */
class ElFileTypeFactory : FileTypeFactory() {
  override fun createFileTypes(consumer: FileTypeConsumer) {
    // consumer.consume(ElFileType, ElFileType.defaultExtension)
  }
}
