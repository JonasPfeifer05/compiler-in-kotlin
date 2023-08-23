package parser.nodes

import general.Register
import generator.ASMBuilder

class LiteralExpressionNode(private val name: String): ExpressionNode() {
    override fun evaluate(asmBuilder: ASMBuilder, evalRegister: Register) {
        asmBuilder.mov(
            evalRegister.register,
            asmBuilder.stackAddrWithOffset(
                asmBuilder.getVariableOffset(this.name)
            )
        )
    }
}