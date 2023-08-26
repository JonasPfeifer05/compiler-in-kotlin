package parser.nodes

import errors.generator.IndexCanOnlyBeNumberException
import errors.generator.YouCanOnlyAccessArraysInVariablesException
import generator.ASMBuilder
import generator.types.ArrayDescriptor
import generator.types.TypeDescriptor
import generator.types.U64Descriptor

class AccessExpressionNode(private val toAccess: ExpressionNode, private val index: ExpressionNode): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder): TypeDescriptor {
        when (toAccess) {
            is IdentifierLiteralExpressionNode -> {}
            else -> throw YouCanOnlyAccessArraysInVariablesException()
        }
        val variable = asmBuilder.getVariable(this.toAccess.name)
        if (variable.second !is ArrayDescriptor) throw YouCanOnlyAccessArraysInVariablesException()

        val elementSize = (variable.second as ArrayDescriptor).content.sizeOf()
        asmBuilder.growStack(elementSize)

        val offsetToVariable = asmBuilder.offsetToVariable(variable)

        val indexType = this.index.evaluate(asmBuilder)
        if (indexType::class != U64Descriptor::class) throw IndexCanOnlyBeNumberException()

        asmBuilder.pop("rax")
        asmBuilder.imul("rax", "$elementSize")

        asmBuilder.append("lea rax, [rsp + $offsetToVariable + rax]")

        asmBuilder.memcpy(0, "rax", elementSize)

        return variable.second
    }

    override fun toString(): String {
        return "${this.toAccess}[${this.index}]"
    }
}