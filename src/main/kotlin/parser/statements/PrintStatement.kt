package parser.statements

import generator.ASMBuilder
import parser.nodes.Expression

class PrintStatement(private val expression: Expression): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        val type = this.expression.evaluate(asmBuilder)

        asmBuilder.mov("rax", "1")
        asmBuilder.mov("rdi", "1")
        asmBuilder.mov("rsi", "rsp")
        asmBuilder.mov("rdx", "${type.sizeOf()}")
        asmBuilder.append("syscall")
    }

    override fun toString(): String {
        return "print($expression);"
    }
}