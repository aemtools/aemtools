package com.aemtools.lang.clientlib.file

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

/**
 * @author Dmytro_Troynikov
 */
class CdFileTypeFactory : FileTypeFactory() {
  override fun createFileTypes(consumer: FileTypeConsumer) {
    consumer.consume(CdFileType, CdFileType.defaultExtension)
  }
}
