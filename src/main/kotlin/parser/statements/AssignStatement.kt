package parser.statements

import errors.generator.CanOnlyAssignToPointerException
import generator.ASMBuilder
import generator.types.PointerDescriptor
import parser.nodes.ExpressionNode

class AssignStatement(private val assignee: ExpressionNode, private val expression: ExpressionNode): Statement() {
    override fun toAssembly(asmBuilder: ASMBuilder) {
        val type = this.expression.evaluateOntoStack(asmBuilder)

        val assigneeType = assignee.evaluateOntoStack(asmBuilder)
        if (assigneeType !is PointerDescriptor) throw CanOnlyAssignToPointerException()
        asmBuilder.pop("rax")

        asmBuilder.memcpy("rax", "rsp", type.sizeOf())

        asmBuilder.shrinkStack(type.sizeOf())
    }

    override fun toString(): String {
        return "$assignee = $expression;"
    }
}