package parser.nodes

import generator.ASMBuilder

class BracketExpressionNode(private val value: ExpressionNode): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder) {
        this.value.evaluate(asmBuilder)
    }

    override fun toString(): String {
        return "($value)"
    }
}