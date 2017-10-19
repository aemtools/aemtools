package com.aemtools.lang.clientlib

import com.aemtools.lang.clientlib.editor.CdCommenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * @author Dmytro Troynikov
 */
class CdCommenterUnitTest {

  @Test
  fun `getCommentedBlockCommentPrefix should return null`() =
      assertThat(CdCommenter().commentedBlockCommentPrefix)
          .isNull()

  @Test
  fun `getCommentedBlockCommentSuffix should return null`() =
      assertThat(CdCommenter().commentedBlockCommentSuffix)
          .isNull()

  @Test
  fun `getBlockCommentPrefix should return null`() =
      assertThat(CdCommenter().blockCommentPrefix)
          .isNull()

  @Test
  fun `getBlockCommentSuffix should return null`() =
      assertThat(CdCommenter().blockCommentSuffix)
          .isNull()

  @Test
  fun `getLineCommentPrefix should return "# "`() {
    assertThat(CdCommenter().lineCommentPrefix)
        .isEqualTo("# ")
  }

}
