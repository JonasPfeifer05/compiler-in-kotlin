package parser.nodes

import generator.ASMBuilder
import generator.types.TypeDescriptor

class BracketExpressionNode(private val value: ExpressionNode): ExpressionNode() {
    override fun evaluateOntoStack(asmBuilder: ASMBuilder): TypeDescriptor {
        return this.value.evaluateOntoStack(asmBuilder)
    }

    override fun toString(): String {
        return "($value)"
    }
}