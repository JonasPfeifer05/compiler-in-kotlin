package parser.statements

import generator.ASMBuilder
import generator.ConstantValue
import generator.Register
import parser.nodes.ExpressionNode

class PrintStatement(private val expression: ExpressionNode) : Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        val type = this.expression.evaluateOntoStack(asmBuilder)

        // Write
        asmBuilder.mov(
            Register.Rax, ConstantValue(1)
        )

        // To stdout
        asmBuilder.mov(
            Register.Rdi, ConstantValue(1)
        )

        // Start address
        asmBuilder.mov(
            Register.Rsi, Register.Rsp
        )

        // Amount of bytes to print
        asmBuilder.mov(
            Register.Rdx, ConstantValue(type.sizeOf())
        )

        // Execute
        asmBuilder.syscall()
    }

    override fun toString(): String {
        return "print($expression);"
    }
}