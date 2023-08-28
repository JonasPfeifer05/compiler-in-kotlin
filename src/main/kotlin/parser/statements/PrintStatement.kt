package parser.statements

import generator.ASMBuilder
import parser.nodes.ExpressionNode

class PrintStatement(private val expression: ExpressionNode): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        val type = this.expression.evaluateOntoStack(asmBuilder)

        // Write
        asmBuilder.mov("rax", "1")

        // To stdout
        asmBuilder.mov("rdi", "1")

        // Start address
        asmBuilder.mov("rsi", "rsp")

        // Amount of bytes to print
        asmBuilder.mov("rdx", "${type.sizeOf()}")

        // Execute
        asmBuilder.append("syscall")
    }

    override fun toString(): String {
        return "print($expression);"
    }
}