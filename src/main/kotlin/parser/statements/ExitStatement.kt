package parser.statements

import errors.generator.WrongParameterTypeException
import generator.ASMBuilder
import generator.ConstantValue
import generator.Register
import generator.types.U64Descriptor
import parser.nodes.ExpressionNode

class ExitStatement(private val expression: ExpressionNode) : Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        // The type of the exit code
        val exitCodeType = this.expression.evaluateOntoStack(asmBuilder)

        // Check if exit code is u64
        when (exitCodeType) {
            is U64Descriptor -> {}
            else -> throw WrongParameterTypeException(U64Descriptor(), exitCodeType)
        }

        // Exit program
        asmBuilder.mov(
            Register.Rax, ConstantValue(60)
        )

        // Move the value of the expression into the exit code
        asmBuilder.pop(
            Register.Rdi
        )

        // Execute
        asmBuilder.syscall()
    }

    override fun toString(): String {
        return "exit $expression"
    }
}