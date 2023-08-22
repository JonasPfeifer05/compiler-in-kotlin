package parser.nodes

abstract class ExprNode {
    abstract fun evaluate(append: (String) -> Unit);
}