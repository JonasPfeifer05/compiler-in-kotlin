package parser.statements

import generator.ASMBuilder
import parser.nodes.ExpressionNode

class AssignStatement(private val name: String, private val expression: ExpressionNode): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        this.expression.evaluate(asmBuilder)
        asmBuilder.mov(
            asmBuilder.stackAddrWithOffset(
                asmBuilder.getVariableOffset(name)
            ),
            "rax"
        )
    }
}