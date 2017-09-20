package com.aemtools.lang.htl.file

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

/**
 * @author Dmytro Troynikov
 */
class HtlFileTypeFactory : FileTypeFactory() {
  override fun createFileTypes(consumer: FileTypeConsumer) {
    consumer.consume(HtlFileType, HtlFileType.defaultExtension)
  }
}
