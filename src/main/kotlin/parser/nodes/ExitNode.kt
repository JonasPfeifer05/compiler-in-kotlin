package parser.nodes

import general.mov
import general.pop

class ExitNode(private val expression: ExprNode): StmtNode() {
    override fun toAssembly(append: (String) -> Unit) {
        this.expression.evaluate(append);
        append(
            mov("rax", "60")
        );
        append(
            pop("rdi")
        );
        append("syscall")
    }
}