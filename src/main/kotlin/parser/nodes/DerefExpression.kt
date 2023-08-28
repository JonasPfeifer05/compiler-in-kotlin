package parser.nodes

import errors.generator.CanOnlyDerefPointersException
import generator.*
import generator.types.PointerDescriptor
import generator.types.TypeDescriptor

class DerefExpression(private val pointer: ExpressionNode): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        val addressType = this.pointer.evaluateOntoStack(asmBuilder)
        if (addressType !is PointerDescriptor) throw CanOnlyDerefPointersException()

        // Move address into rax
        asmBuilder.pop(
            Register.Rax
        )

        // Grow the stack by the size of the target type
        asmBuilder.growStack(addressType.pointsTo.sizeOf())

        // Copy the value onto the stack
        addressType.pointsTo.copyTo(
            AddressFrom(Register.Rsp), AddressFrom(Register.Rax), asmBuilder
        )

        return addressType.pointsTo
    }

    override fun toString(): String {
        return "*${this.pointer}"
    }
}