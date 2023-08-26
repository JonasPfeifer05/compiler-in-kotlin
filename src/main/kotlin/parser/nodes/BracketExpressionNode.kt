package parser.nodes

import generator.ASMBuilder
import generator.types.TypeDescriptor

class BracketExpressionNode(private val value: ExpressionNode): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder): TypeDescriptor {
        return this.value.evaluate(asmBuilder)
    }

    override fun toString(): String {
        return "($value)"
    }
}