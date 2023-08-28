package parser.nodes

import errors.generator.CanOnlyDerefPointersException
import generator.ASMBuilder
import generator.types.PointerDescriptor
import generator.types.TypeDescriptor

class DerefExpression(private val pointer: ExpressionNode): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        val pointerType = this.pointer.evaluateOntoStack(asmBuilder)
        if (pointerType !is PointerDescriptor) throw CanOnlyDerefPointersException()

        asmBuilder.pop("rax")

        asmBuilder.growStack(pointerType.pointsTo.sizeOf())
        pointerType.pointsTo.copyTo(0, "rax", asmBuilder)

        return pointerType.pointsTo
    }

    override fun toString(): String {
        return "*${this.pointer}"
    }
}