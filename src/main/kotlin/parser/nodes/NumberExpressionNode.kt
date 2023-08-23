package parser.nodes

import general.Register
import generator.ASMBuilder

class NumberExpressionNode(private val value: String): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder, evalRegister: Register) {
        asmBuilder.mov(evalRegister.register, value)
    }
}