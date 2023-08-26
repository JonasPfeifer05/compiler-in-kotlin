package parser.statements

import generator.ASMBuilder
import parser.nodes.Expression

class ExitStatement(private val expression: Expression) : Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        this.expression.evaluate(asmBuilder)
        asmBuilder.mov("rax", "60")
        asmBuilder.pop("rdi")
        asmBuilder.append("syscall")
    }

    override fun toString(): String {
        return "exit $expression"
    }
}