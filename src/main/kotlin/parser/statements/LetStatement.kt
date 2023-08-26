package parser.statements

import generator.ASMBuilder
import parser.nodes.Expression

class LetStatement(private val name: String, private val expression: Expression): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        val type = this.expression.evaluate(asmBuilder)

        asmBuilder.registerVariable(name, type)
    }

    override fun toString(): String {
        return "let $name = $expression;"
    }
}