package com.aemtools.completion.util

import com.aemtools.lang.clientlib.psi.CdBasePath
import com.aemtools.lang.clientlib.psi.CdInclude

/**
 * @author Dmytro_Troynikov
 */

/**
 * Find [CdBasePath] element related to current include.
 *
 * @return related base path element, _null_ if no such element exists
 */
fun CdInclude.basePathElement(): CdBasePath? =
        generateSequence { this.prevSibling }
                .map { it as CdBasePath }
                .firstOrNull()
