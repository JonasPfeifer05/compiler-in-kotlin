package parser.statements

import generator.ASMBuilder
import parser.nodes.ExpressionNode

class ExitStatement(private val expression: ExpressionNode) : Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        this.expression.evaluateOntoStack(asmBuilder)
        asmBuilder.mov("rax", "60")
        asmBuilder.pop("rdi")
        asmBuilder.append("syscall")
    }

    override fun toString(): String {
        return "exit $expression"
    }
}