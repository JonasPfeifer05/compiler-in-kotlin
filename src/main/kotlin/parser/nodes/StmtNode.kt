package parser.nodes

abstract class StmtNode {
    abstract fun toAssembly(append: (String) -> Unit);
}