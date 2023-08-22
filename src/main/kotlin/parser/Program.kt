package parser

import parser.nodes.StmtNode

class Program(val nodes: List<StmtNode>) {
    override fun toString(): String {
        return "Program: ${nodes.joinToString()}"
    }
}