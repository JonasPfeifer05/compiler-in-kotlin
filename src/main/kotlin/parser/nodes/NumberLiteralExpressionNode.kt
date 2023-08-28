package parser.nodes

import generator.ASMBuilder
import generator.ConstantValue
import generator.types.TypeDescriptor
import generator.types.U64Descriptor

class NumberLiteralExpressionNode(private val value: String): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        // Push the value on the stack
        asmBuilder.push(
            ConstantValue(this.value.toInt())
        )
        return U64Descriptor()
    }


    override fun toString(): String {
        return this.value
    }
}