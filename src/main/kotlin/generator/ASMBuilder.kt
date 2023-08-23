package generator

class ASMBuilder(private val asmBuffer: StringBuilder) {
    fun init() {
        this.asmBuffer.appendLine("global _start")
        this.asmBuffer.appendLine("_start:")
    }

    fun mov(into: String, from: String) = this.append("mov $into, $from")

    fun pop(into: String) = this.append("pop $into")

    fun push(from: String) = this.append("push $from")

    fun append(asmLine: String) {
        this.asmBuffer.append('\t').appendLine(asmLine)
    }

    override fun toString(): String = this.asmBuffer.toString()
}