package parser.statements

import generator.ASMBuilder
import parser.nodes.Expression

class LetStatement(private val name: String, private val expression: Expression): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        this.expression.evaluate(asmBuilder)
        asmBuilder.push("rax")
        asmBuilder.lookupTable.registerNewVariable(name)
    }

    override fun toString(): String {
        return "let $name = $expression;"
    }
}