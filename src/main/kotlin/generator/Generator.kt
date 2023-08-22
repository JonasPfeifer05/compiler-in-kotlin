package generator

import parser.Program

class Generator(private val program: Program) {

    private val asmBuffer = StringBuilder();

    init {
        this.asmBuffer.appendLine("global _start")
        this.asmBuffer.appendLine("_start:")
    }

    fun generateAssembly(): String {

        for (node in this.program.nodes) {
            node.toAssembly {
                this.asmBuffer.append('\t').appendLine(it);
            };
        }

        return asmBuffer.toString();
    }
}