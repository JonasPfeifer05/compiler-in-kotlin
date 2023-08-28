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
        val addressType = this.toAccess.evaluateOntoStack(asmBuilder)
        if (addressType !is PointerDescriptor) throw YouCanOnlyAccessPointersException()

        // get the type that gets accessed
        val elementType = when (addressType.pointsTo) {
            is ArrayDescriptor -> addressType.pointsTo.content
            else -> throw YouCanOnlyAccessArraysException()
        }

        val indexType = this.index.evaluateOntoStack(asmBuilder)
        if (indexType != U64Descriptor()) throw IndexCanOnlyBeNumberException()

        // Move index into rax
        asmBuilder.pop("rax")

        // Multiply the index by the size of the elements to receive the correct offset
        asmBuilder.imul("rax", elementType.sizeOf().toString())

        // Move address into rbx
        asmBuilder.pop("rbx")

        // Add the offset to the base address
        asmBuilder.append("lea rax, [rbx + rax]")

        // Push the address of the access element onto the stack
        asmBuilder.push("rax")

        return PointerDescriptor(elementType)
    }

    override fun toString(): String {
        return "${this.toAccess}[${this.index}]"
    }
}