package parser.statements

import errors.generator.WrongParameterTypeException
import generator.ASMBuilder
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
        asmBuilder.mov("rax", "60")

        // Move the value of the expression into the exit code
        asmBuilder.pop("rdi")

        // Execute
        asmBuilder.append("syscall")
    }

    override fun toString(): String {
        return "exit $expression"
    }
}