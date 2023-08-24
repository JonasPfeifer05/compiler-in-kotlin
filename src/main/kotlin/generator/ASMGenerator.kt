package generator

import parser.nodes.Expression
import parser.nodes.NumberExpressionNode
import parser.statements.ExitStatement
import parser.statements.Statement

class ASMGenerator(private val statements: List<Statement>) {
    private val asmBuilder = ASMBuilder(StringBuilder())

    init {
        asmBuilder.init()
    }

    fun generateAssembly(): String {

        for (statement in this.statements) {
            asmBuilder.append("; $statement")
            statement.toAssembly(asmBuilder)
            asmBuilder.append("")
        }

        // Add exit with code 0 so that it exits with 0 if there was no other exit before that
        ExitStatement(Expression(NumberExpressionNode("0"))).toAssembly(asmBuilder)

        return asmBuilder.toString()
    }
}