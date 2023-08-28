package generator

import errors.generator.UnknownVariableException
import general.unreachable
import generator.types.TypeDescriptor
import kotlin.math.absoluteValue

class ASMBuilder(private val asmBuffer: StringBuilder) {
    private val stack = VirtualStackDescriptor()
    private val lookupTable = VariableLookupTable(stack)

    fun memcpy(to: DataSource, from: DataSource, bytesToCopy: Int) {
        // To address
        when (to) {
            is ConstantValue -> this.lea(Register.Rdi, Register.Rsp, to)
            is Register -> this.mov(Register.Rdi, to)
            is Offset, is AddressFrom -> unreachable()
        }
        // From address
        when (from) {
            is ConstantValue -> this.lea(Register.Rsi, Register.Rsp, from)
            is Register -> this.mov(Register.Rsi, from)
            is Offset, is AddressFrom -> unreachable()
        }
        // Amount of bytes
        this.mov(Register.Rdx, ConstantValue(bytesToCopy))
        this.call("memcpy")
    }

    fun call(name: String) {
        this.append("call $name")
    }

    fun syscall() {
        this.append("syscall")
    }

    fun lea(into: Register, from: Register, offset: DataSource) {
        this.append("lea $into, ${pointerWithOffset(from, offset)}")
    }

    fun growStack(bytes: Int) {
        this.sub(Register.Rsp, ConstantValue(bytes))
        this.stack.grow(bytes)
    }

    fun shrinkStack(bytes: Int) {
        this.add(Register.Rsp, ConstantValue(bytes))
        this.stack.shrink(bytes)
    }

    fun getVariable(name: String): Pair<Int, TypeDescriptor> {
        if (!this.lookupTable.doesVariablesExits(name))
            throw UnknownVariableException(name)

        return this.lookupTable.getVariable(name)
    }

    fun registerVariable(name: String, type: TypeDescriptor) {
        this.lookupTable.registerNewVariable(name, type)
    }

    private fun stackSize(): Int = this.stack.getCurrentStackSize()

    fun offsetToVariable(variable: Pair<Int, TypeDescriptor>): Int {
        return this.stackSize() - variable.first
    }

    fun pointerWithOffset(from: Register, offset: DataSource, size: MemorySizes? = null): String {
        val sizeString = size?.toString() ?: ""
        return when (offset) {
            is ConstantValue -> if (offset.value >= 0) {
                "$sizeString [$from + ${offset.value.absoluteValue}]"
            } else {
                "$sizeString [$from - ${offset.value.absoluteValue}]"
            }
            is Register -> "$sizeString [$from + $offset]"
            is Offset, is AddressFrom -> unreachable()
        }
    }

    fun mov(into: DataSource, from: DataSource) = this.append("mov $into, $from")

    fun pop(into: Register) {
        this.append("pop $into")
        this.stack.popNumber()
    }

    fun push(from: DataSource) {
        this.append("push $from")
        this.stack.pushNumber()
    }

    fun add(into: Register, from: DataSource) {
        this.append("add $into, $from")
    }

    fun sub(into: Register, from: DataSource) {
        this.append("sub $into, $from")
    }

    fun imul(into: Register, from: DataSource) {
        this.append("imul $into, $from")
    }

    fun div(from: DataSource) {
        this.append("div $from")
    }

    fun xor(into: Register, from: DataSource) {
        this.append("xor $into, $from")
    }

    fun append(asmLine: String) {
        this.asmBuffer.append('\t').appendLine(asmLine)
    }

    override fun toString(): String = this.asmBuffer.toString()
    fun appendWithoutIndent(text: String) {
        this.asmBuffer.appendLine(text)
    }
}