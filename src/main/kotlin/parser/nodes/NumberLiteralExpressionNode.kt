package parser.nodes

import generator.ASMBuilder
import generator.types.TypeDescriptor
import generator.types.U64Descriptor

class NumberLiteralExpressionNode(private val value: String): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        asmBuilder.push(value)
        return U64Descriptor()
    }

    override fun toString(): String {
        return this.value
    }
}