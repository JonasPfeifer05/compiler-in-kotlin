package parser.nodes

import errors.generator.IndexCanOnlyBeNumberException
import errors.generator.YouCanOnlyAccessArraysException
import errors.generator.YouCanOnlyAccessPointersException
import generator.ASMBuilder
import generator.types.ArrayDescriptor
import generator.types.PointerDescriptor
import generator.types.TypeDescriptor
import generator.types.U64Descriptor

class AccessExpressionNode(private val toAccess: ExpressionNode, private val index: ExpressionNode): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        val type = this.toAccess.evaluateOntoStack(asmBuilder)
        if (type !is PointerDescriptor) throw YouCanOnlyAccessPointersException()

        val elementType = when (type.pointsTo) {
            is ArrayDescriptor -> type.pointsTo.content
            else -> throw YouCanOnlyAccessArraysException()
        }

        val indexType = this.index.evaluateOntoStack(asmBuilder)
        if (indexType::class != U64Descriptor::class) throw IndexCanOnlyBeNumberException()
        asmBuilder.pop("rax")
        asmBuilder.imul("rax", elementType.sizeOf().toString())

        asmBuilder.pop("rbx")

        asmBuilder.append("lea rax, [rbx + rax]")

        asmBuilder.push("rax")

        return PointerDescriptor(elementType)
    }

    override fun toString(): String {
        return "${this.toAccess}[${this.index}]"
    }
}