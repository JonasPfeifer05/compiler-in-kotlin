package parser.statements

import errors.generator.WrongTypeException
import generator.ASMBuilder
import parser.nodes.Expression

class AssignStatement(private val name: String, private val expression: Expression): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        val variable = asmBuilder.getVariable(name)
        val type = this.expression.evaluate(asmBuilder)

        if (variable.second::class != type::class)
            throw WrongTypeException(variable.second, type)

        asmBuilder.memcpy(
            asmBuilder.offsetToVariable(variable), 0, type.sizeOf()
        )
        asmBuilder.shrinkStack(type.sizeOf())
    }

    override fun toString(): String {
        return "$name = $expression;"
    }
}