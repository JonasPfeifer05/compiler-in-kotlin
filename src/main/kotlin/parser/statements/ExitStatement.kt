package parser.statements

import general.Register
import generator.ASMBuilder
import parser.nodes.ExpressionNode

class ExitStatement(private val expressionNode: ExpressionNode) : Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        this.expressionNode.evaluate(asmBuilder, Register("rdi"))
        asmBuilder.mov("rax", "60")
        asmBuilder.append("syscall")
    }
}