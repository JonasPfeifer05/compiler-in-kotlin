package parser.nodes

import general.push

class NumberNode(private val value: String): ExprNode() {
    override fun evaluate(append: (String) -> Unit) {
        append(
            push(value)
        )
    }
}