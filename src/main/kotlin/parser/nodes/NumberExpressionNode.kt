package parser.nodes

import generator.ASMBuilder

class NumberExpressionNode(private val value: String): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder) {
        asmBuilder.push(value)
    }

    override fun toString(): String {
        return this.value
    }
}