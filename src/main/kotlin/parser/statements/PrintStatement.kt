package parser.statements

import generator.ASMBuilder
import parser.nodes.ExpressionNode

class PrintStatement(private val expression: ExpressionNode): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        val type = this.expression.evaluateOntoStack(asmBuilder)

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