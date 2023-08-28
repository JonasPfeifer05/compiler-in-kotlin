package parser.statements

import errors.generator.WrongTypeException
import generator.ASMBuilder
import generator.types.TypeDescriptor
import parser.nodes.ExpressionNode

class LetStatement(private val name: String, private val type: TypeDescriptor, private val expression: ExpressionNode?): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        // See if the let statement has an initial value
        if (this.expression == null) {
            // If not just allocate the space
            asmBuilder.growStack(this.type.sizeOf())
        } else {
            // If yes evaluate it on the stack
            val valueType = this.expression.evaluateOntoStack(asmBuilder)

            // Check if the value has the correct type
            if (valueType != type) throw WrongTypeException(this.type, valueType)
        }

        // Register the variable
        asmBuilder.registerVariable(name, type)
    }

    override fun toString(): String {
        if (this.expression == null) return "let $name: $type;"
        return "let $name: $type = $expression;"
    }
}