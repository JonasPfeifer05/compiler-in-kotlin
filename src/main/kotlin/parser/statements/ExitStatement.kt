package parser.statements

import general.Register
import generator.ASMBuilder
import parser.nodes.Expression

class ExitStatement(private val expression: Expression) : Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        this.expression.evaluate(asmBuilder, Register("rdi"))
        asmBuilder.mov("rax", "60")
        asmBuilder.append("syscall")
    }

    override fun toString(): String {
        return "exit $expression"
    }
}