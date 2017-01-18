package com.aemtools.completion.htl.callchain.rawchainprocessor

import com.aemtools.completion.htl.callchain.elements.CallChain
import com.aemtools.completion.htl.callchain.elements.CallChainElement
import com.aemtools.completion.htl.callchain.elements.CallChainSegment
import com.aemtools.completion.htl.callchain.elements.helper.chainSegment
import com.aemtools.completion.htl.callchain.typedescriptor.JavaPsiClassTypeDescriptor
import com.aemtools.completion.htl.callchain.typedescriptor.PredefinedVariantsTypeDescriptor
import com.aemtools.completion.htl.callchain.typedescriptor.TypeDescriptor
import com.aemtools.completion.htl.completionprovider.FileVariablesResolver
import com.aemtools.completion.htl.completionprovider.PredefinedVariables
import com.aemtools.lang.htl.psi.chain.RawChainUnit
import com.aemtools.lang.htl.psi.mixin.VariableNameMixin
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration
import java.util.*

/**
 * @author Dmytro_Troynikov
 */
object JavaRawCallChainProcessor : RawCallChainProcessor {
    override fun processChain(rawChain: LinkedList<RawChainUnit>): CallChain {
        if (rawChain.isEmpty()) {
            return CallChain.empty()
        }

        val segments = ArrayList<CallChainSegment>()

        val firstElement: RawChainUnit = rawChain.pop()

        segments.add(extractFirstSegment(firstElement))

        return CallChain.empty()
    }

    private fun extractFirstSegment(rawChainUnit: RawChainUnit): CallChainSegment {
        val chainUnitDeclaration = rawChainUnit.myDeclaration
        val elements = rawChainUnit.myCallChain

        if (rawChainUnit.hasPredefinedVariants()) {
            return constructPredefinedChainSegment(rawChainUnit)
        }

        val inputType = resolveFirstType(rawChainUnit)

        return CallChainSegment.empty()
    }

    /**
     * Resolve first [PsiClass].
     */
    private fun resolveFirstType(rawChainUnit: RawChainUnit): TypeDescriptor {
        val elements = rawChainUnit.myCallChain
        val firstElement = if (elements.isNotEmpty()) {
            elements.first() as? VariableNameMixin
        } else {
            null
        } ?: return TypeDescriptor.empty()

        val psiClass: PsiClass =
                // take predefined class
                rawChainUnit.myDeclaration?.resolutionResult?.psiClass
                // try to find class from variables declared in current file
            ?: FileVariablesResolver.resolveVariable(firstElement).psiClass
                // try to find class predefined context objects
            ?: PredefinedVariables.resolveByIdentifier(firstElement.variableName(), firstElement)
            ?: return TypeDescriptor.empty()

        return JavaPsiClassTypeDescriptor(psiClass)
    }



    private fun mapChain(inputType: TypeDescriptor,
                         rawChain: LinkedList<PsiElement>)
        : List<CallChainElement> {
        if (inputType.isEmpty()) {
            return listOf()
        }


        return listOf()
    }

    /**
     * Current implementation resolves only one level of predefined
     * completion variants.
     */
    private fun constructPredefinedChainSegment(rawChainUnit: RawChainUnit): CallChainSegment
            = chainSegment {

        inputType = TypeDescriptor.empty()

        val variants = rawChainUnit
                .myDeclaration
                ?.resolutionResult
                ?.predefined
                ?: listOf()

        // we do not try to resolve current chain
        // the predefined set of variants is the resulting set
        outputType = PredefinedVariantsTypeDescriptor(variants)

        chain = listOf()
    }

}

