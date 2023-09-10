package generator.types

import generator.ASMBuilder
import generator.AddressFrom

class CharDescriptor: TypeDescriptor() {
    override fun sizeOf(): Int = 1
    override fun copyTo(to: AddressFrom, from: AddressFrom, asmBuilder: ASMBuilder) {
        asmBuilder.append("mov al, BYTE $from")
        asmBuilder.append("mov BYTE $to, al")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "char"
    }
}