package parser.statements

import errors.generator.WrongTypeException
import generator.ASMBuilder
import generator.types.TypeDescriptor
import parser.nodes.Expression

class LetStatement(private val name: String, private val type: TypeDescriptor, private val expression: Expression?): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        if (this.expression == null) {
            asmBuilder.growStack(this.type.sizeOf())
        } else {
            val type = this.expression.evaluate(asmBuilder)

            if (type::class != this.type::class)
                throw WrongTypeException(this.type, type)
        }

        asmBuilder.registerVariable(name, type)
    }

    override fun toString(): String {
        return "let $name = $expression;"
    }
}