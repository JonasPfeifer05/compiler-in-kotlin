package parser.statements

import errors.generator.CanOnlyAssignToPointerException
import errors.generator.WrongTypeException
import generator.ASMBuilder
import generator.AddressFrom
import generator.Register
import generator.types.PointerDescriptor
import parser.nodes.ExpressionNode

class AssignStatement(private val assignee: ExpressionNode, private val expression: ExpressionNode): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        // The value to store in the pointer
        val valueType = this.expression.evaluateOntoStack(asmBuilder)

        // The pointer to assign to
        val assigneeType = assignee.evaluateOntoStack(asmBuilder)

        // Check if the pointer really is a pointer
        when (assigneeType) {
            is PointerDescriptor -> if (assigneeType.pointsTo != valueType) throw WrongTypeException(assigneeType.pointsTo, valueType)
            else -> throw CanOnlyAssignToPointerException()
        }

        // Store the pointer in rax
        asmBuilder.pop(
            Register.Rax
        )

        // Copy from stack top to pointer
        valueType.copyTo(
            AddressFrom(Register.Rax), AddressFrom(Register.Rsp), asmBuilder
        )

        // Free the space of the value on the stack
        asmBuilder.shrinkStack(valueType.sizeOf())
    }

    override fun toString(): String {
        return "$assignee = $expression;"
    }
}