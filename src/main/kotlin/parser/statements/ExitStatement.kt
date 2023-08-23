package parser.statements

import generator.ASMBuilder
import parser.nodes.ExpressionNode

class ExitStatement(private val expressionNode: ExpressionNode) : Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        this.expressionNode.evaluate(asmBuilder)
        asmBuilder.mov("rax", "60")
        asmBuilder.pop("rdi")
        asmBuilder.append("syscall")
    }
}