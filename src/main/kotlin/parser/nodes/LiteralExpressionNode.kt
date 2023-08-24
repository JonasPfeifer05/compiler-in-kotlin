package parser.nodes

import generator.ASMBuilder

class LiteralExpressionNode(private val name: String): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder) {
        asmBuilder.push(
            asmBuilder.stackAddrWithOffset(
                asmBuilder.getVariableOffset(this.name)
            )
        )
    }

    override fun toString(): String {
        return this.name
    }
}